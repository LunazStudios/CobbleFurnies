package com.lunazstudios.cobblefurnies.client.model.fabric;

import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public final class CFClientModelsImpl {
    private CFClientModelsImpl() {
    }

    public static BakedModel getModel(ResourceLocation location) {
        FabricBakedModelManager models = Minecraft.getInstance().getModelManager();
        return models.getModel(location);
    }
}
