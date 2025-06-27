package com.viditagarwal.running;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Heightmap;

public class TickHandler {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                PlayerPhaseState.tick(player);

                boolean wearing = ChronoBootsItem.isWearing(player);
                int mode = ChronoBootsItem.getMode(player);
                boolean phasing = PlayerPhaseState.isPhasing(player);
                boolean active = phasing && wearing;

                var abilities = player.getAbilities();

                // Force-disable phasing if boots off or mode is REGULAR
                if ((!wearing || mode == ChronoBootsItem.MODE_REGULAR) && phasing) {
                    PlayerPhaseState.setPhasing(player, false);
                    System.out.println("[DEBUG] Phasing forcibly disabled.");
                }

                if (active) {
                    double groundY = player.getWorld().getTopY(Heightmap.Type.MOTION_BLOCKING,
                            (int) player.getX(), (int) player.getZ());
                    double maxY = groundY + 1.0;

                    if (player.getY() > maxY) {
                        player.setPosition(player.getX(), maxY, player.getZ());
                        player.setVelocity(0, 0, 0);
                    }

                    abilities.allowFlying = true;
                    abilities.flying = true;
                    abilities.setFlySpeed(0f);
                    player.noClip = true;
                    player.setNoGravity(true);
                    player.setVelocity(0, 0, 0);

                    Box area = player.getBoundingBox().expand(1.0);
                    for (LivingEntity entity : player.getWorld().getEntitiesByClass(LivingEntity.class, area, e -> e != player)) {
                        if (!entity.isRemoved() && entity.isAlive()) {
                            entity.kill();
                        }
                    }
                } else {
                    boolean isCreativeOrSpectator = abilities.creativeMode || player.isSpectator();
                    if (!isCreativeOrSpectator) {
                        abilities.allowFlying = false;
                        abilities.flying = false;
                    }
                    abilities.setFlySpeed(0.05f);
                    player.noClip = false;
                    player.setNoGravity(false);
                    player.setVelocity(0, 0, 0);
                }

                // Travel or Combat Mode
                if (wearing && (mode == ChronoBootsItem.MODE_TRAVEL || mode == ChronoBootsItem.MODE_COMBAT)) {
                    player.setStepHeight(1.2f);

                    if (mode == ChronoBootsItem.MODE_TRAVEL) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10, 100, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 10, 255, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 10, 25, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 10, 0, false, false));
                    }

                    if (mode == ChronoBootsItem.MODE_COMBAT) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10, 4, false, false));
                    }
                } else {
                    player.removeStatusEffect(StatusEffects.SPEED);
                    player.removeStatusEffect(StatusEffects.RESISTANCE);
                    player.removeStatusEffect(StatusEffects.STRENGTH);
                    player.removeStatusEffect(StatusEffects.DOLPHINS_GRACE);
                    player.removeStatusEffect(StatusEffects.WATER_BREATHING);
                    player.setStepHeight(0.6f);
                }

                player.sendAbilitiesUpdate();
            }
        });
    }
}
