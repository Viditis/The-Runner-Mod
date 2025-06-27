package com.viditagarwal.running.network;

import com.viditagarwal.running.ChronoBootsItem;
import com.viditagarwal.running.PlayerPhaseState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class PhaseTogglePacket {

    public static final Identifier PHASE_KEY = new Identifier("running", "phase_key");
    public static final Identifier PHASE_STATE_SYNC = new Identifier("running", "phase_state_sync");
    public static final Identifier PHASE_TIME_UPDATE = new Identifier("running", "phase_time_update");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(PHASE_KEY, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                // Check if wearing ChronoBoots
                if (!ChronoBootsItem.isWearing(player)) return;

                // Get current mode
                int mode = ChronoBootsItem.getMode(player);

                // Block phasing if in REGULAR mode
                if (mode == ChronoBootsItem.MODE_REGULAR) {
                    System.out.println("[DEBUG] Tried to phase in Regular mode — blocked.");
                    return;
                }

                // Toggle phasing
                boolean newState = !PlayerPhaseState.isPhasing(player);
                PlayerPhaseState.setPhasing(player, newState);
                PhaseTogglePacket.sendPhaseState(player, newState);
            });
        });
    }

    public static void sendPhaseState(ServerPlayerEntity player, boolean state) {
        var buf = PacketByteBufs.create();
        buf.writeBoolean(state);
        ServerPlayNetworking.send(player, PHASE_STATE_SYNC, buf);
    }

    public static void sendPhaseTime(ServerPlayerEntity player, int ticksLeft) {
        var buf = PacketByteBufs.create();
        buf.writeInt(ticksLeft);
        ServerPlayNetworking.send(player, PHASE_TIME_UPDATE, buf);
    }
}
