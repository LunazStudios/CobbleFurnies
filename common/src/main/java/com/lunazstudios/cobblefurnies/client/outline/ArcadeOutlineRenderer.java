package com.lunazstudios.cobblefurnies.client.outline;

import com.google.gson.JsonParser;
import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.block.ArcadeBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.slf4j.Logger;

import java.io.Reader;
import java.util.List;

/**
 * Custom block outlines inspired by Highlight and ResourcefulLib (Team Resourceful).
 *
 * @see <a href="https://github.com/Team-Resourceful/Highlight">Highlight</a>
 * @see <a href="https://github.com/Team-Resourceful/ResourcefulLib/tree/1.21.1">ResourcefulLib</a>
 */
public final class ArcadeOutlineRenderer implements ResourceManagerReloadListener {
    public static final ArcadeOutlineRenderer INSTANCE = new ArcadeOutlineRenderer();

    private static final ResourceLocation OUTLINE = CobbleFurnies.id("outlines/arcade.json");
    private static final Logger LOGGER = LogUtils.getLogger();

    private volatile ArcadeOutlineGeometry geometry;

    private ArcadeOutlineRenderer() {
    }

    @Override
    public void onResourceManagerReload(ResourceManager resources) {
        geometry = null;
        try (Reader reader = resources.getResourceOrThrow(OUTLINE).openAsReader()) {
            geometry = ArcadeOutlineGeometry.fromJson(JsonParser.parseReader(reader).getAsJsonObject());
        } catch (Exception exception) {
            LOGGER.warn("Unable to load arcade outline {}", OUTLINE, exception);
        }
    }

    public boolean render(BlockState state, BlockPos pos, double cameraX, double cameraY, double cameraZ,
                          PoseStack poseStack, VertexConsumer consumer) {
        ArcadeOutlineGeometry outline = geometry;
        if (!(state.getBlock() instanceof ArcadeBlock) || outline == null) {
            return false;
        }

        int turns = switch (state.getValue(ArcadeBlock.FACING)) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        };
        List<ArcadeOutlineGeometry.Segment> segments = outline.segments(turns);
        if (segments.isEmpty()) {
            return false;
        }

        float offsetX = (float) (pos.getX() - cameraX);
        float offsetY = (float) (pos.getY() - cameraY
                - (state.getValue(ArcadeBlock.HALF) == DoubleBlockHalf.UPPER ? 1.0 : 0.0));
        float offsetZ = (float) (pos.getZ() - cameraZ);
        PoseStack.Pose pose = poseStack.last();
        for (ArcadeOutlineGeometry.Segment segment : segments) {
            consumer.addVertex(pose.pose(), offsetX + (float) segment.startX(),
                            offsetY + (float) segment.startY(), offsetZ + (float) segment.startZ())
                    .setColor(0.0F, 0.0F, 0.0F, 0.4F)
                    .setNormal(pose, (float) segment.normalX(), (float) segment.normalY(), (float) segment.normalZ());
            consumer.addVertex(pose.pose(), offsetX + (float) segment.endX(),
                            offsetY + (float) segment.endY(), offsetZ + (float) segment.endZ())
                    .setColor(0.0F, 0.0F, 0.0F, 0.4F)
                    .setNormal(pose, (float) segment.normalX(), (float) segment.normalY(), (float) segment.normalZ());
        }
        return true;
    }
}
