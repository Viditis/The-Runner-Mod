package com.viditagarwal.running;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public interface CustomArmorTexture {
    String getArmorTexture(ItemStack stack, net.minecraft.entity.Entity entity, EquipmentSlot slot, String type);
}
