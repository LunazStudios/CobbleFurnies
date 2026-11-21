package com.lunazstudios.cobblefurnies.block.entity;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ArcadeBlockEntity extends BlockEntity {
    public static final int PLAY_DURATION_TICKS = 40;

    private static final String ANIMATION_STARTED_AT = "AnimationStartedAt";
    private static final int SOUND_COOLDOWN_TICKS = 90;

    private long animationStartedAt = -1L;
    private long soundAvailableAt;

    public ArcadeBlockEntity(BlockPos pos, BlockState state) {
        super(BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional(CobbleFurnies.id("arcade")).orElseThrow(), pos, state);
    }

    public boolean startPlaying() {
        if (level == null || level.isClientSide || isRemoved() || getAnimationTime(0.0F) >= 0.0F) {
            return false;
        }

        animationStartedAt = level.getGameTime();
        setChanged();
        BlockState state = getBlockState();
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
        if (animationStartedAt >= soundAvailableAt) {
            soundAvailableAt = animationStartedAt + SOUND_COOLDOWN_TICKS;
            level.playSound(null, worldPosition.getX() + 0.5, worldPosition.getY() + 1.0,
                    worldPosition.getZ() + 0.5, BuiltInRegistries.SOUND_EVENT.getOptional(CobbleFurnies.id("block.arcade.play")).orElseThrow(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return true;
    }

    public float getAnimationTime(float partialTick) {
        if (level == null || animationStartedAt < 0L) {
            return -1.0F;
        }

        long elapsedTicks = level.getGameTime() - animationStartedAt;
        if (elapsedTicks < 0L || elapsedTicks >= PLAY_DURATION_TICKS) {
            return -1.0F;
        }

        return elapsedTicks + partialTick;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong(ANIMATION_STARTED_AT, animationStartedAt);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        animationStartedAt = tag.contains(ANIMATION_STARTED_AT) ? tag.getLong(ANIMATION_STARTED_AT) : -1L;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
