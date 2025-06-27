package com.viditagarwal.running;

import com.viditagarwal.running.network.PhaseTogglePacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class ClientPhaseState {
    private static boolean phasing = false;
    private static int phaseTicksLeft = 0;

    public static final Identifier PHASE_STATE_SYNC = new Identifier("running", "phase_state_sync");

    private static int timeLeft = 0;
    public static int getTimeLeft() {
        return timeLeft;
    }


    public static boolean isPhasing() {
        return phasing;
    }

    public static int getPhaseTicksLeft() {
        return phaseTicksLeft;
    }

    public static void setPhasing(boolean value) {
        phasing = value;
        if (!value) {
            phaseTicksLeft = 0;
        }
    }

    public static void registerReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(PHASE_STATE_SYNC, (client, handler, buf, responseSender) -> {
            boolean newState = buf.readBoolean();
            phasing = newState;
            if (!newState) {
                phaseTicksLeft = 0;
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(PhaseTogglePacket.PHASE_TIME_UPDATE, (client, handler, buf, responseSender) -> {
            int ticks = buf.readInt();
            timeLeft = ticks;

            if (ticks <= 0) {
                phasing = false;
            }

            System.out.println("[DEBUG] HUD time update: " + ticks + " ticks");
        });

    }
}
