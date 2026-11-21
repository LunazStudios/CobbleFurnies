package com.lunazstudios.cobblefurnies.block.entity;

import com.lunazstudios.cobblefurnies.block.StoveBlock;
import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import com.lunazstudios.cobblefurnies.registry.CFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class DecorativeStoveBlockEntity extends BlockEntity {
    private static final List<String> BLOCK_ENTITY_METADATA = List.of("id", "x", "y", "z", "components", "keepPacked");

    private CompoundTag storedData = new CompoundTag();

    public DecorativeStoveBlockEntity(BlockPos pos, BlockState state) {
        super(CFBlockEntityTypes.STOVE.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        storedData = tag.copy();
        BLOCK_ENTITY_METADATA.forEach(storedData::remove);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.merge(storedData);
    }

    public void preserveContents(ItemStack stack) {
        BlockState state = getBlockState();
        if (state.getValue(CFBlockStateProperties.HAS_POT)) {
            BlockItemStateProperties properties = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
            stack.set(DataComponents.BLOCK_STATE, properties
                    .with(CFBlockStateProperties.HAS_POT, state)
                    .with(CFBlockStateProperties.POT_COLOR, state)
                    .with(StoveBlock.LID, state)
                    .with(StoveBlock.COOKING, state));
        }
        if (!storedData.isEmpty()) {
            BlockItem.setBlockEntityData(stack, getType(), storedData.copy());
        }
    }
}
