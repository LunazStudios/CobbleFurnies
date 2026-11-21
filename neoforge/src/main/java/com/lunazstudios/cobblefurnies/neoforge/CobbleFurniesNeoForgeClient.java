package com.lunazstudios.cobblefurnies.neoforge;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.client.renderer.block.UpperCabinetBlockRenderer;
import com.lunazstudios.cobblefurnies.registry.CFBlockEntityTypes;
import com.lunazstudios.cobblefurnies.registry.CFEntityTypes;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = CobbleFurnies.MOD_ID, value = Dist.CLIENT)
public class CobbleFurniesNeoForgeClient {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CFEntityTypes.SEAT.get(), NoopRenderer::new);
        event.registerBlockEntityRenderer(CFBlockEntityTypes.UPPER_CABINET.get(), UpperCabinetBlockRenderer::new);

    }
}
