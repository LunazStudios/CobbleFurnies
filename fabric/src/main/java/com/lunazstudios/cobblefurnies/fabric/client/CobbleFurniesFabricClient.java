package com.lunazstudios.cobblefurnies.fabric.client;

import com.lunazstudios.cobblefurnies.client.renderer.block.UpperCabinetBlockRenderer;
import com.lunazstudios.cobblefurnies.client.screen.FurniCrafterScreen;
import com.lunazstudios.cobblefurnies.registry.CFBlockEntityTypes;
import com.lunazstudios.cobblefurnies.registry.CFEntityTypes;
import com.lunazstudios.cobblefurnies.registry.CFMenus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.NoopRenderer;

public final class CobbleFurniesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(CFEntityTypes.SEAT.get(), NoopRenderer::new);
        BlockEntityRendererRegistry.register(CFBlockEntityTypes.UPPER_CABINET.get(), UpperCabinetBlockRenderer::new);
        MenuScreens.register(CFMenus.FURNI_CRAFTER_MENU.get(), FurniCrafterScreen::new);

    }
}
