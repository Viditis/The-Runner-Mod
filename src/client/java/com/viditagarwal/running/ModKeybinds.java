package com.viditagarwal.running;

import com.viditagarwal.running.network.ChronoModePacket;
import com.viditagarwal.running.network.PhaseTogglePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds implements ClientModInitializer {

    private static KeyBinding toggleChronoModeKey;
    private static KeyBinding phaseKey;

    @Override
    public void onInitializeClient() {
        toggleChronoModeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.running.toggle_chrono_mode",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.running.keybinds"
        ));

        phaseKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.running.phase",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.running.keybinds"
        ));

        ClientPhaseState.registerReceiver();



        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleChronoModeKey.wasPressed()) {
                if (client.player != null) {
                    ClientPlayNetworking.send(ChronoModePacket.TOGGLE_CHRONO_MODE, PacketByteBufs.empty());
                }
            }

            while (phaseKey.wasPressed()) {
                if (client.player != null) {
                    ClientPlayNetworking.send(PhaseTogglePacket.PHASE_KEY, PacketByteBufs.empty());
                }
            }


            HudRenderCallback.EVENT.register(new PhaseHudRenderer());

        });
    }
}
