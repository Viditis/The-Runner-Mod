package com.viditagarwal.running.armor;

import com.viditagarwal.running.CustomArmorTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ChronoBootsArmor extends ArmorItem implements CustomArmorTexture {

    public ChronoBootsArmor(ArmorMaterial material, Item.Settings settings) {
        super(material, Type.BOOTS, settings);
    }

    // Optional override for a hardcoded texture path

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "running:textures/models/armor/chrono_layer_1.png";
    }



}
