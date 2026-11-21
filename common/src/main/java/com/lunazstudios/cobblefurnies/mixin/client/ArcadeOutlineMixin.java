package com.lunazstudios.cobblefurnies.mixin.client;

import com.lunazstudios.cobblefurnies.client.outline.ArcadeOutlineRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Custom block outlines inspired by Highlight and ResourcefulLib (Team Resourceful).
 *
 * @see <a href="https://github.com/Team-Resourceful/Highlight">Highlight</a>
 * @see <a href="https://github.com/Team-Resourceful/ResourcefulLib/tree/1.21.1">ResourcefulLib</a>
 */
@Mixin(LevelRenderer.class)
public abstract class ArcadeOutlineMixin {
    @Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
    private void cobblefurnies$renderArcadeOutline(PoseStack poseStack, VertexConsumer consumer, Entity entity,
                                                   double cameraX, double cameraY, double cameraZ,
                                                   BlockPos pos, BlockState state, CallbackInfo callback) {
        if (ArcadeOutlineRenderer.INSTANCE.render(state, pos, cameraX, cameraY, cameraZ, poseStack, consumer)) {
            callback.cancel();
        }
    }
}
