package com.lunazstudios.cobblefurnies.block;

import com.lunazstudios.cobblefurnies.block.entity.StoveBlockEntity;
import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import com.lunazstudios.cobblefurnies.registry.CFBlockEntityTypes;
import com.lunazstudios.cobblefurnies.util.item.PotColor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public final class CookingStoveBlock extends StoveBlock {
    public CookingStoveBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof StoveBlockEntity stoveBE)) return InteractionResult.PASS;

        if (state.getValue(CFBlockStateProperties.HAS_POT)) {
            if (player.isShiftKeyDown()) {
                PotColor color = state.getValue(CFBlockStateProperties.POT_COLOR);
                ItemStack potStack = new ItemStack(color.getItem());

                Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, potStack);

                level.setBlock(pos, state
                        .setValue(CFBlockStateProperties.HAS_POT, false), 3);

                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1.2f);
                return InteractionResult.CONSUME;
            }

            player.openMenu(stoveBE);
            PiglinAi.angerNearbyPiglins(player, true);
            return InteractionResult.CONSUME;
        }

        player.displayClientMessage(Component.translatable("gui.cobblefurnies.add_campfire_pot"), true);
        return InteractionResult.SUCCESS;
    }
    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;

        if (!(level.getBlockEntity(pos) instanceof StoveBlockEntity)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (state.getValue(CFBlockStateProperties.HAS_POT)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        PotColor color = PotColor.fromItem(itemStack.getItem());
        if (color == null) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!player.isCreative()) {
            itemStack.shrink(1);
        }

        BlockState newState = state
                .setValue(CFBlockStateProperties.HAS_POT, true)
                .setValue(CFBlockStateProperties.POT_COLOR, color);

        level.setBlock(pos, newState, 3);

        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1f, 1f);

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide
                ? BaseEntityBlock.createTickerHelper(type, CFBlockEntityTypes.Cooking.STOVE.get(), StoveBlockEntity::clientTick)
                : BaseEntityBlock.createTickerHelper(type, CFBlockEntityTypes.Cooking.STOVE.get(), StoveBlockEntity::serverTick);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock())) return;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof StoveBlockEntity stoveBE) {
            stoveBE.handleSafeDrop();
            Containers.dropContents(level, pos, stoveBE);
            level.updateNeighbourForOutputSignal(pos, this);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        if (!level.isClientSide && state.getValue(CFBlockStateProperties.HAS_POT)) {
            PotColor color = state.getValue(CFBlockStateProperties.POT_COLOR);
            ItemStack potStack = new ItemStack(color.getItem());

            Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, potStack);
        }
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StoveBlockEntity(pos, state);
    }
}
