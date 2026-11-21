package com.lunazstudios.cobblefurnies.registry.fabric;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.registry.CFBlocks;
import com.lunazstudios.cobblefurnies.registry.CFRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CFTabsImpl {
    public static final CreativeModeTab CF_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            CobbleFurnies.id("tab"),
            FabricItemGroup.builder().title(Component.translatable("item_group." + CobbleFurnies.MOD_ID + ".tab"))
                    .icon(() -> new ItemStack(CFBlocks.LIGHT_BLUE_SOFA.get().asItem())).displayItems((parameters, output) -> {
                        CFRegistry.getAllModItems().stream()
                        .filter(stack -> stack.isItemEnabled(parameters.enabledFeatures()))
                        .forEach(output::accept);
                    }).build());

    public static void register() {}
}
