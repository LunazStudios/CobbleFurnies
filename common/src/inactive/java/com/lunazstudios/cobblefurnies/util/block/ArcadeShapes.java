package com.lunazstudios.cobblefurnies.util.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class ArcadeShapes {

    private static final double SLICE_HEIGHT = 1.0;
    private static final Map<Direction, VoxelShape> LOWER = rotate(createHalf(0));
    private static final Map<Direction, VoxelShape> UPPER = rotate(createHalf(16));

    private ArcadeShapes() {
    }

    public static VoxelShape get(Direction facing, DoubleBlockHalf half) {
        return (half == DoubleBlockHalf.LOWER ? LOWER : UPPER).get(facing);
    }

    private static Map<Direction, VoxelShape> rotate(List<ShapeBox> boxes) {
        Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            VoxelShape shape = join(boxes, direction, 0, boxes.size());
            shapes.put(direction, shape.optimize());
        }
        return shapes;
    }

    private static VoxelShape join(List<ShapeBox> boxes, Direction direction, int from, int to) {
        if (to - from == 1) {
            return boxes.get(from).rotate(direction);
        }
        int middle = (from + to) / 2;
        return Shapes.joinUnoptimized(join(boxes, direction, from, middle),
                join(boxes, direction, middle, to), BooleanOp.OR);
    }

    private static List<ShapeBox> createHalf(double offset) {
        List<ShapeBox> boxes = new ArrayList<>();
        addBox(boxes, offset, 0, 12, -3, 16, 15, 16);
        addBox(boxes, offset, 11, 14, -1, 12, 17, 0);
        addBox(boxes, offset, 10.5, 17, -1.5, 12.5, 19, 0.5);
        addBox(boxes, offset, 6, 15, -1.5, 8, 16, 0.5);
        addBox(boxes, offset, 3, 15, -0.5, 5, 16, 1.5);
        addBox(boxes, offset, 0, 31, -1, 16, 32, 16);
        addBox(boxes, offset, 0, 15, 6, 16, 29, 16);
        addBox(boxes, offset, 0, 29, -1, 16, 30, 16);
        addInclinedBox(boxes, offset, 0, 23.33836, -11.42274, 16, 26.35836, 1.59726);
        addInclinedBox(boxes, offset, 0, 11.34836, -2.41274, 16, 26.34836, 1.58726);
        addInclinedBox(boxes, offset, 14, 10.33836, -5.42274, 16, 26.35836, -2.40274);
        addInclinedBox(boxes, offset, 0, 10.33836, -5.42274, 2, 26.35836, -2.40274);
        addBox(boxes, offset, 0, 30, -1, 2, 31, 16);
        addBox(boxes, offset, 14, 30, -1, 16, 31, 16);
        addBox(boxes, offset, 2, 30, 0, 14, 31, 0.125);
        addBox(boxes, offset, 0, 12, -4, 2, 15, -3);
        addBox(boxes, offset, 0, 11, -4, 2, 12, 0);
        addBox(boxes, offset, 14, 11, -4, 16, 12, 0);
        addBox(boxes, offset, 14, 12, -4, 16, 15, -3);
        addBox(boxes, offset, 0, 0, 0, 2, 12, 16);
        addBox(boxes, offset, 14, 0, 0, 16, 12, 16);
        addBox(boxes, offset, 2, 0, 1, 14, 12, 16);
        return boxes;
    }

    private static void addBox(List<ShapeBox> boxes, double offset, double minX, double minY, double minZ,
                               double maxX, double maxY, double maxZ) {
        double bottom = Math.max(minY, offset);
        double top = Math.min(maxY, offset + 16);
        if (top > bottom) {
            boxes.add(new ShapeBox(minX, bottom - offset, minZ, maxX, top - offset, maxZ));
        }
    }

    private static void addInclinedBox(List<ShapeBox> boxes, double offset, double minX, double minY,
                                       double minZ, double maxX, double maxY, double maxZ) {
        double angle = Math.toRadians(22.5);
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double[] y = new double[4];
        double[] z = new double[4];
        double[] originalY = {minY, minY, maxY, maxY};
        double[] originalZ = {minZ, maxZ, maxZ, minZ};
        double bottom = offset + 16;
        double top = offset;
        for (int index = 0; index < 4; index++) {
            y[index] = originalY[index] * cos - (originalZ[index] - 8) * sin;
            z[index] = 8 + originalY[index] * sin + (originalZ[index] - 8) * cos;
            bottom = Math.min(bottom, y[index]);
            top = Math.max(top, y[index]);
        }
        bottom = Math.max(bottom, offset);
        top = Math.min(top, offset + 16);
        for (double sliceBottom = bottom; sliceBottom < top;) {
            double sliceTop = Math.min((Math.floor(sliceBottom / SLICE_HEIGHT) + 1) * SLICE_HEIGHT, top);
            double minSliceZ = Double.POSITIVE_INFINITY;
            double maxSliceZ = Double.NEGATIVE_INFINITY;
            for (int index = 0; index < 4; index++) {
                int next = (index + 1) % 4;
                if (y[index] >= sliceBottom && y[index] <= sliceTop) {
                    minSliceZ = Math.min(minSliceZ, z[index]);
                    maxSliceZ = Math.max(maxSliceZ, z[index]);
                }
                for (double boundary : new double[]{sliceBottom, sliceTop}) {
                    if ((y[index] < boundary && y[next] > boundary)
                            || (y[index] > boundary && y[next] < boundary)) {
                        double intersectionZ = z[index]
                                + (z[next] - z[index]) * (boundary - y[index]) / (y[next] - y[index]);
                        minSliceZ = Math.min(minSliceZ, intersectionZ);
                        maxSliceZ = Math.max(maxSliceZ, intersectionZ);
                    }
                }
            }
            if (maxSliceZ > minSliceZ) {
                addBox(boxes, offset, minX, sliceBottom, minSliceZ, maxX, sliceTop, maxSliceZ);
            }
            sliceBottom = sliceTop;
        }
    }

    private record ShapeBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        private VoxelShape rotate(Direction direction) {
            return switch (direction) {
                case EAST -> Block.box(16 - maxZ, minY, minX, 16 - minZ, maxY, maxX);
                case SOUTH -> Block.box(16 - maxX, minY, 16 - maxZ, 16 - minX, maxY, 16 - minZ);
                case WEST -> Block.box(minZ, minY, 16 - maxX, maxZ, maxY, 16 - minX);
                default -> Block.box(minX, minY, minZ, maxX, maxY, maxZ);
            };
        }
    }
}
