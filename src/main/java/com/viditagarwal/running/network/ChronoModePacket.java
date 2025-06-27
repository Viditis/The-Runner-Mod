package com.viditagarwal.running.network;

import com.viditagarwal.running.ChronoBootsItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class ChronoModePacket {
    public static final Identifier TOGGLE_CHRONO_MODE = new Identifier("running", "toggle_chrono_mode");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_CHRONO_MODE, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ChronoBootsItem.toggleMode(player);
            });
        });
    }
}
