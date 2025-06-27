package com.viditagarwal.running;

import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ArmorItem.Type;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModItems {

    // Chrono Essence item
    public static final Item CHRONO_ESSENCE = new Item(new Item.Settings().maxCount(64));

    // Chrono Boots item (using Iron armor material as before, but you can change this)
    public static final Item CHRONO_BOOTS = new ChronoBootsItem(
            ArmorMaterials.IRON,
            Type.BOOTS,
            new Item.Settings().maxCount(1)
    );

    // Register both items here
    public static void registerItems() {
        Registry.register(Registries.ITEM, new Identifier("running", "chrono_essence"), CHRONO_ESSENCE);
        Registry.register(Registries.ITEM, new Identifier("running", "chrono_boots"), CHRONO_BOOTS);
    }
}
