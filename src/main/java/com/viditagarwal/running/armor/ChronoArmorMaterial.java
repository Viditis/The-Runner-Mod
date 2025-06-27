package com.viditagarwal.running.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class ChronoArmorMaterial implements ArmorMaterial {

    public static final ChronoArmorMaterial INSTANCE = new ChronoArmorMaterial();

    private ChronoArmorMaterial() {}

    @Override
    public int getDurability(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> 500;
            default -> 0;
        };
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> 3;
            default -> 0;
        };
    }

    // Remove getProtectionAmount if present.

    @Override
    public int getEnchantability() {
        return 10;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(net.minecraft.item.Items.IRON_INGOT);
    }

    @Override
    public String getName() {
        return "chrono";
    }

    @Override
    public float getToughness() {
        return 0f;
    }

    @Override
    public float getKnockbackResistance() {
        return 0f;
    }
}
