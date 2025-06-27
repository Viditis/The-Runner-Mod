package com.viditagarwal.running;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class ChronoBootsItem extends ArmorItem {
    public static final int MODE_REGULAR = 0;
    public static final int MODE_TRAVEL = 1;
    public static final int MODE_COMBAT = 2;

    public ChronoBootsItem(ArmorMaterial material, Type boots, Settings settings) {
        super(material, Type.BOOTS, settings);
    }

    // Toggle ChronoBoots mode
    public static void toggleMode(PlayerEntity player) {
        ItemStack boots = player.getInventory().getArmorStack(0);
        if (!(boots.getItem() instanceof ChronoBootsItem)) return;

        int current = getMode(boots);
        int newMode = (current + 1) % 3;
        setMode(boots, newMode);

        player.sendMessage(getFormattedModeMessage(newMode), true);
    }

    public static Text getFormattedModeMessage(int mode) {
        return switch (mode) {
            case MODE_TRAVEL -> Text.literal("Chrono Mode: Travel")
                    .formatted(Formatting.GOLD, Formatting.BOLD);
            case MODE_COMBAT -> Text.literal("Chrono Mode: Combat")
                    .formatted(Formatting.RED, Formatting.BOLD);
            default -> Text.literal("Chrono Mode: Regular");
        };
    }

    public static int getMode(ItemStack stack) {
        if (!stack.hasNbt()) return MODE_REGULAR;
        return stack.getNbt().getInt("ChronoMode");
    }

    public static void setMode(ItemStack stack, int mode) {
        stack.getOrCreateNbt().putInt("ChronoMode", mode);
    }

    public static boolean isWearing(PlayerEntity player) {
        return player.getInventory().getArmorStack(0).getItem() instanceof ChronoBootsItem;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        int mode = getMode(stack);
        tooltip.add(getFormattedModeMessage(mode));
        super.appendTooltip(stack, world, tooltip, context);
    }

    public static int getMode(PlayerEntity player) {
        ItemStack boots = player.getInventory().getArmorStack(0);
        return getMode(boots);
    }
}
