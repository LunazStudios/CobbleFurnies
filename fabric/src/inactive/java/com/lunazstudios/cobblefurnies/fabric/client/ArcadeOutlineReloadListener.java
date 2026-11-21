package com.lunazstudios.cobblefurnies.fabric.client;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.client.outline.ArcadeOutlineRenderer;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public final class ArcadeOutlineReloadListener implements SimpleSynchronousResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return CobbleFurnies.id("arcade_outline");
    }

    @Override
    public void onResourceManagerReload(ResourceManager resources) {
        ArcadeOutlineRenderer.INSTANCE.onResourceManagerReload(resources);
    }
}
