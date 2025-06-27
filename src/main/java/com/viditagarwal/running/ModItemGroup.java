package com.viditagarwal.running;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {

    public static ItemGroup RUNNING;

    public static void register() {
        RUNNING = Registry.register(
                Registries.ITEM_GROUP,
                new Identifier("running", "main"),
                FabricItemGroup.builder()
                        .displayName(Text.translatable("itemGroup.running"))
                        .icon(() -> new ItemStack(ModItems.CHRONO_BOOTS))
                        .entries((context, entries) -> {
                            entries.add(ModItems.CHRONO_BOOTS);
                        })
                        .build()
        );
    }
}
