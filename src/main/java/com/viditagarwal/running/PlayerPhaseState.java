package com.viditagarwal.running;

import com.viditagarwal.running.network.PhaseTogglePacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerPhaseState {

    private static final Map<UUID, Boolean> states = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> timers = new ConcurrentHashMap<>();

    private static final int MAX_PHASE_TICKS = 20 * 5; // 5 seconds in ticks

    public static boolean isPhasing(ServerPlayerEntity player) {
        return states.getOrDefault(player.getUuid(), false);
    }

    public static void setPhasing(ServerPlayerEntity player, boolean value) {
        states.put(player.getUuid(), value);
        if (value) {
            timers.put(player.getUuid(), MAX_PHASE_TICKS);
        } else {
            timers.remove(player.getUuid());
        }
    }

    public static void togglePhasing(ServerPlayerEntity player) {
        boolean newState = !isPhasing(player);
        setPhasing(player, newState);
        System.out.println("[DEBUG] Player " + player.getName().getString() + " phasing toggled: " + newState);
    }

    public static void tick(ServerPlayerEntity player) {
        if (!isPhasing(player)) return;

        UUID uuid = player.getUuid();
        int timeLeft = timers.getOrDefault(uuid, 0);

        if (timeLeft <= 0) {
            System.out.println("[DEBUG] Timer expired for " + player.getName().getString());
            forceExitPhase(player);
            PhaseTogglePacket.sendPhaseState(player, false); // tell client we’re done
        } else {
            timers.put(uuid, timeLeft - 1);
            PhaseTogglePacket.sendPhaseTime(player, timeLeft - 1); // update HUD
        }
    }


    public static int getPhaseTime(ServerPlayerEntity player) {
        return timers.getOrDefault(player.getUuid(), 0);
    }

    public static void forceExitPhase(ServerPlayerEntity player) {
        setPhasing(player, false);

        var abilities = player.getAbilities();
        abilities.allowFlying = false;
        abilities.flying = false;
        abilities.setFlySpeed(0.05f);
        player.sendAbilitiesUpdate();

        player.noClip = false;
        player.setNoGravity(false);

        if (!player.isOnGround()) {
            player.teleport(player.getX(), player.getY() + 1, player.getZ());
            player.setVelocity(0, 0, 0);
        }
    }

}
