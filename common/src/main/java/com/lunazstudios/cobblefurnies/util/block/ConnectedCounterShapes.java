package com.lunazstudios.cobblefurnies.util.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;

public final class ConnectedCounterShapes {
    private final EnumMap<Direction, Connections> shapes = new EnumMap<>(Direction.class);

    public ConnectedCounterShapes(VoxelShape northTop) {
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            VoxelShape top = rotate(northTop, facing);
            shapes.put(facing, new Connections(
                    Shapes.or(top, Block.box(1, 0, 1, 15, 11, 15)),
                    Shapes.or(top, rotate(Block.box(0, 0, 1, 15, 11, 15), facing)),
                    Shapes.or(top, rotate(Block.box(1, 0, 1, 16, 11, 15), facing)),
                    Shapes.or(top, rotate(Block.box(0, 0, 1, 16, 11, 15), facing))
            ));
        }
    }

    public VoxelShape get(Direction facing, boolean left, boolean right) {
        Connections options = shapes.get(facing);
        if (left) return right ? options.middle() : options.left();
        return right ? options.right() : options.single();
    }

    private static VoxelShape rotate(VoxelShape shape, Direction facing) {
        return facing == Direction.NORTH ? shape : ShapeUtil.rotateShape(shape, facing);
    }

    private record Connections(VoxelShape single, VoxelShape left, VoxelShape right, VoxelShape middle) {}
}
