package com.lunazstudios.cobblefurnies.client.model.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public final class CFClientModelsImpl {
    private CFClientModelsImpl() {
    }

    public static BakedModel getModel(ResourceLocation location) {
        return Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(location));
    }
}
