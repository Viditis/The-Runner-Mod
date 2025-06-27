package com.viditagarwal.running;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TickFreezeHandler {

    private static final int FREEZE_RADIUS = 512;

    private static class ProjectileData {
        final PersistentProjectileEntity projectile;
        final Vec3d frozenVelocity;

        ProjectileData(PersistentProjectileEntity projectile, Vec3d frozenVelocity) {
            this.projectile = projectile;
            this.frozenVelocity = frozenVelocity;
        }
    }

    // Map from projectile UUID to its data (entity ref + frozen velocity)
    private static final Map<UUID, ProjectileData> frozenProjectiles = new ConcurrentHashMap<>();

    // Map from player UUID to whether they are currently in combat mode (to detect mode switches)
    private static final Map<UUID, Boolean> playerCombatStatus = new ConcurrentHashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(TickFreezeHandler::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            boolean wearing = ChronoBootsItem.isWearing(player);
            int mode = ChronoBootsItem.getMode(player);
            UUID playerId = player.getUuid();

            boolean currentlyInCombat = wearing && (mode == ChronoBootsItem.MODE_COMBAT);
            Boolean previouslyInCombat = playerCombatStatus.getOrDefault(playerId, false);

            // If player just left combat mode, restore their frozen projectiles' velocities
            if (previouslyInCombat && !currentlyInCombat) {
                restoreProjectilesForPlayer(playerId);
            }

            playerCombatStatus.put(playerId, currentlyInCombat);

            if (!currentlyInCombat) {
                // Not in combat, skip freezing logic
                continue;
            }

            Box area = player.getBoundingBox().expand(FREEZE_RADIUS);

            for (Entity entity : player.getWorld().getEntitiesByClass(Entity.class, area, e -> true)) {
                if (entity == player) continue;

                if (entity instanceof LivingEntity living) {
                    // Freeze living entities by zeroing velocity
                    living.setVelocity(0, 0, 0);
                    living.velocityModified = true;
                } else if (entity instanceof PersistentProjectileEntity projectile) {
                    UUID projId = projectile.getUuid();

                    // If not already frozen, store velocity & entity reference
                    if (!frozenProjectiles.containsKey(projId)) {
                        frozenProjectiles.put(projId, new ProjectileData(projectile, projectile.getVelocity()));
                    }

                    // Freeze projectile velocity
                    projectile.setVelocity(0, 0, 0);
                    projectile.velocityModified = true;
                } else {
                    // Freeze other entities by zeroing velocity
                    entity.setVelocity(0, 0, 0);
                    entity.velocityModified = true;
                }
            }

            // Clean up any projectiles that have been removed
            cleanupRemovedProjectiles();
        }
    }

    private static void restoreProjectilesForPlayer(UUID playerId) {
        // Iterate over all frozen projectiles and restore velocities for those associated with this player
        // Since we don't directly associate projectiles with players,
        // we restore *all* frozen projectiles.
        // Alternatively, you can implement a more complex association if needed.

        for (Map.Entry<UUID, ProjectileData> entry : frozenProjectiles.entrySet()) {
            ProjectileData data = entry.getValue();
            PersistentProjectileEntity projectile = data.projectile;
            if (!projectile.isRemoved()) {
                projectile.setVelocity(data.frozenVelocity);
                projectile.velocityModified = true;
            }
        }
        frozenProjectiles.clear();
    }

    private static void cleanupRemovedProjectiles() {
        frozenProjectiles.entrySet().removeIf(entry -> entry.getValue().projectile.isRemoved());
    }
}
