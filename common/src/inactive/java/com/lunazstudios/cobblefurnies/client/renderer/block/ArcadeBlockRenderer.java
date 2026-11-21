package com.lunazstudios.cobblefurnies.client.renderer.block;

import com.lunazstudios.cobblefurnies.block.ArcadeBlock;
import com.lunazstudios.cobblefurnies.block.entity.ArcadeBlockEntity;
import com.lunazstudios.cobblefurnies.client.model.CFClientModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArcadeBlockRenderer implements BlockEntityRenderer<ArcadeBlockEntity> {
    private final ModelBlockRenderer modelRenderer;
    private final Map<Block, ModelParts> modelParts = new HashMap<>();

    public ArcadeBlockRenderer(BlockEntityRendererProvider.Context context) {
        modelRenderer = context.getBlockRenderDispatcher().getModelRenderer();
    }

    public static List<ResourceLocation> getModelLocations() {
        List<ResourceLocation> locations = new ArrayList<>();
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block instanceof ArcadeBlock) {
                ModelParts parts = ModelParts.forBlock(block);
                locations.add(parts.lever());
                locations.add(parts.buttonOne());
                locations.add(parts.buttonTwo());
                locations.add(parts.neon());
            }
        }
        return locations;
    }

    @Override
    public void render(ArcadeBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        if (blockEntity.getLevel() == null) {
            return;
        }

        BlockState state = blockEntity.getBlockState();
        ModelParts parts = modelParts.computeIfAbsent(state.getBlock(), ModelParts::forBlock);
        float time = blockEntity.getAnimationTime(partialTick);
        float envelope = time < 0.0F ? 0.0F : Mth.sin(Mth.PI * time / ArcadeBlockEntity.PLAY_DURATION_TICKS);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.getValue(ArcadeBlock.FACING).toYRot()));
        poseStack.translate(-0.5, 0.0, -0.5);

        poseStack.pushPose();
        poseStack.translate(11.5 / 16.0, 15.5 / 16.0, -0.5 / 16.0);
        poseStack.mulPose(Axis.XP.rotationDegrees(envelope * 12.0F * Mth.sin(time * 0.55F)));
        poseStack.mulPose(Axis.ZP.rotationDegrees(envelope * 10.0F * Mth.sin(time * 0.72F)));
        poseStack.translate(-11.5 / 16.0, -15.5 / 16.0, 0.5 / 16.0);
        renderPart(parts.lever(), state, poseStack, buffers, RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS),
                packedLight, packedOverlay);
        poseStack.popPose();

        renderButton(parts.buttonOne(), state, poseStack, buffers, packedLight, packedOverlay,
                envelope * Math.max(0.0F, Mth.sin(time * 0.9F)));
        renderButton(parts.buttonTwo(), state, poseStack, buffers, packedLight, packedOverlay,
                envelope * Math.max(0.0F, Mth.sin(time * 0.9F + Mth.PI)));
        renderPart(parts.neon(), state, poseStack, buffers, RenderType.eyes(TextureAtlas.LOCATION_BLOCKS),
                LightTexture.FULL_BRIGHT, packedOverlay);
        poseStack.popPose();
    }

    private void renderButton(ResourceLocation model, BlockState state, PoseStack poseStack,
                              MultiBufferSource buffers, int packedLight, int packedOverlay, float pressed) {
        poseStack.pushPose();
        poseStack.translate(0.0, -pressed * 0.5 / 16.0, 0.0);
        renderPart(model, state, poseStack, buffers, RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS),
                packedLight, packedOverlay);
        poseStack.popPose();
    }

    private void renderPart(ResourceLocation model, BlockState state, PoseStack poseStack,
                            MultiBufferSource buffers, RenderType renderType, int packedLight, int packedOverlay) {
        modelRenderer.renderModel(poseStack.last(), buffers.getBuffer(renderType), state,
                CFClientModels.getModel(model), 1.0F, 1.0F, 1.0F, packedLight, packedOverlay);
    }

    @Override
    public boolean shouldRenderOffScreen(ArcadeBlockEntity blockEntity) {
        return true;
    }

    private record ModelParts(ResourceLocation lever, ResourceLocation buttonOne,
                              ResourceLocation buttonTwo, ResourceLocation neon) {
        private static ModelParts forBlock(Block block) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            String path = "block/arcade/" + id.getPath();
            return new ModelParts(
                    id.withPath(path + "_lever"),
                    id.withPath(path + "_button_1"),
                    id.withPath(path + "_button_2"),
                    id.withPath(path + "_neon")
            );
        }
    }
}
