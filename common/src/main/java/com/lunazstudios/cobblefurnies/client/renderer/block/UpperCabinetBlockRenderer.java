package com.lunazstudios.cobblefurnies.client.renderer.block;

import com.lunazstudios.cobblefurnies.block.UpperCabinetBlock;
import com.lunazstudios.cobblefurnies.block.entity.UpperCabinetBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class UpperCabinetBlockRenderer implements BlockEntityRenderer<UpperCabinetBlockEntity, UpperCabinetBlockRenderer.State> {
    public static class State extends BlockEntityRenderState {
        final ItemStackRenderState[] items = new ItemStackRenderState[4];
        final int[] amounts = new int[4];
        float rotation;
    }

    private final ItemModelResolver models;

    public UpperCabinetBlockRenderer(BlockEntityRendererProvider.Context context) {
        models = context.itemModelResolver();
    }

    @Override
    public State createRenderState() { return new State(); }

    @Override
    public void extractRenderState(UpperCabinetBlockEntity cabinet, State state, float partialTick, Vec3 camera, ModelFeatureRenderer.CrumblingOverlay overlay) {
        BlockEntityRenderer.super.extractRenderState(cabinet, state, partialTick, camera, overlay);
        var block = cabinet.getBlockState();
        boolean visible = block.getValue(UpperCabinetBlock.OPEN) || block.getValue(UpperCabinetBlock.HAS_GLASS);
        state.rotation = 180f - block.getValue(UpperCabinetBlock.FACING).toYRot();
        for (int i = 0; i < state.items.length; i++) {
            ItemStack stack = cabinet.getItem(i);
            state.amounts[i] = visible && !stack.isEmpty() ? getAmount(stack.getCount()) : 0;
            if (state.amounts[i] > 0) {
                state.items[i] = new ItemStackRenderState();
                models.updateForTopItem(state.items[i], stack, ItemDisplayContext.FIXED, cabinet.getLevel(), null, i);
            }
        }
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.18, 0.5);
        poseStack.mulPose(new org.joml.Matrix4f().rotation(Axis.YP.rotationDegrees(state.rotation)));
        for (int j = 0; j < state.items.length; j++) {
            int renderCount = state.amounts[j];
            for (int i = 0; i < renderCount; i++) {
                float fx = (-0.10375f * (i - 1) * 0.5f) % 0.09f;
                float fy = (-0.04375f * (i - 1) * 0.5f) % 0.09f;
                float fz = (-0.05375f * (i - 1) * 0.5f) % 0.09f;

                poseStack.pushPose();

                
                float xOffset = 0.15f - 0.4f * (j % 2);
                float zOffset = -0.225f + 0.4f * (j / 2);
                poseStack.translate(xOffset + fx, fy, zOffset + fz);
                poseStack.scale(0.375f, 0.375f, 0.375f);

                state.items[j].submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }

    
    private int getAmount(int count) {
        if (count > 48) return 5;
        if (count > 32) return 4;
        if (count > 16) return 3;
        if (count > 1) return 2;
        return 1;
    }
}
