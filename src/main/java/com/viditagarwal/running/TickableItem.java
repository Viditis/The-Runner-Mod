package com.viditagarwal.running;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface TickableItem {
    void onArmorTick(ItemStack stack, World world, PlayerEntity player);

    String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type);
}
