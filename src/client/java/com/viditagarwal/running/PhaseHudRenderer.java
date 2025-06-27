package com.viditagarwal.running;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class PhaseHudRenderer implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        if (ClientPhaseState.isPhasing()) {
            int timeLeftTicks = ClientPhaseState.getTimeLeft();
            int secondsLeft = timeLeftTicks / 20; // 20 ticks per second

            String message = "Phasing: " + secondsLeft + "s";
            int x = 10;
            int y = 10;

            context.drawTextWithShadow(client.textRenderer, Text.literal(message), x, y, 0xFFFFFF);
        }
    }
}
