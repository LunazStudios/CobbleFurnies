package com.lunazstudios.cobblefurnies.block;

import com.lunazstudios.cobblefurnies.block.entity.DecorativeStoveBlockEntity;
import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import com.lunazstudios.cobblefurnies.compat.CobblemonCompat;
import com.lunazstudios.cobblefurnies.registry.CFBlockEntityTypes;
import com.lunazstudios.cobblefurnies.registry.CFBlockTags;
import com.lunazstudios.cobblefurnies.util.block.ConnectedCounterShapes;
import com.lunazstudios.cobblefurnies.util.item.PotColor;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StoveBlock extends BaseEntityBlock {
    public static final MapCodec<StoveBlock> CODEC = simpleCodec(StoveBlock::create);
    public MapCodec<StoveBlock> codec() {
        return CODEC;
    }
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty CONNECTED_LEFT = CFBlockStateProperties.CONNECTED_LEFT;
    public static final BooleanProperty CONNECTED_RIGHT = CFBlockStateProperties.CONNECTED_RIGHT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LID = CFBlockStateProperties.LID;
    public static final BooleanProperty COOKING = CFBlockStateProperties.COOKING;

    protected static final VoxelShape TOP_SHAPE_NORTH = Shapes.or(
            Block.box(0, 11, 0, 16, 16, 16),
            Block.box(5, 16, 5, 11, 17, 11),
            Block.box(4, 16.5, 8, 12, 18.5, 8),
            Block.box(4, 16.5, 8, 12, 18.5, 8)
    );
    private static final ConnectedCounterShapes SHAPES = new ConnectedCounterShapes(TOP_SHAPE_NORTH);

    public StoveBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(CONNECTED_LEFT, false)
                .setValue(CONNECTED_RIGHT, false)
                .setValue(WATERLOGGED, false)
                .setValue(LID, false)
                .setValue(COOKING, false)
                .setValue(CFBlockStateProperties.HAS_POT, false)
                .setValue(CFBlockStateProperties.POT_COLOR, PotColor.RED));
    }

    public static StoveBlock create(Properties properties) {
        return CobblemonCompat.LOADED ? new CookingStoveBlock(properties) : new StoveBlock(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return CFBlockEntityTypes.STOVE.get().create(pos, state);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof DecorativeStoveBlockEntity stove) {
            for (ItemStack drop : drops) {
                if (drop.is(asItem())) {
                    stove.preserveContents(drop);
                    break;
                }
            }
        }
        return drops;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING), state.getValue(CONNECTED_LEFT), state.getValue(CONNECTED_RIGHT));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        Direction facing = context.getHorizontalDirection().getOpposite();

        return getConnections(this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(WATERLOGGED, waterlogged)
                .setValue(COOKING, false)
                .setValue(LID, true), context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return getConnections(state, level, currentPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONNECTED_LEFT, CONNECTED_RIGHT, WATERLOGGED, LID, COOKING,
                CFBlockStateProperties.HAS_POT, CFBlockStateProperties.POT_COLOR);
    }

    private BlockState getConnections(BlockState state, @Nullable LevelAccessor level, @Nullable BlockPos pos) {
        if (level == null || pos == null) {
            return state.setValue(CONNECTED_LEFT, false).setValue(CONNECTED_RIGHT, false);
        }

        Direction facing = state.getValue(FACING);
        boolean left = validConnection(level.getBlockState(pos.relative(facing.getCounterClockWise())));
        boolean right = validConnection(level.getBlockState(pos.relative(facing.getClockWise())));
        return state.setValue(CONNECTED_LEFT, left).setValue(CONNECTED_RIGHT, right);
    }

    private boolean validConnection(BlockState state) {
        return state.is(CFBlockTags.SINKS_CONNECTABLE);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        Direction newFacing = rotation.rotate(state.getValue(FACING));
        return getConnections(
                state.setValue(FACING, newFacing),
                null,
                null
        );
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return this.rotate(state, mirror.getRotation(state.getValue(FACING)));
    }
}
