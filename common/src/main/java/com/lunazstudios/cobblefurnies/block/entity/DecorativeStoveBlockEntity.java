package com.lunazstudios.cobblefurnies.block.entity;

import com.lunazstudios.cobblefurnies.block.StoveBlock;
import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import com.lunazstudios.cobblefurnies.registry.CFBlockEntityTypes;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public final class DecorativeStoveBlockEntity extends BlockEntity {
    private static final List<String> COOKING_DATA = List.of("Items", "CookingProgress", "PotComponent");

    private CompoundTag storedData = new CompoundTag();

    public DecorativeStoveBlockEntity(BlockPos pos, BlockState state) {
        super(CFBlockEntityTypes.STOVE.get(), pos, state);
    }

    @Override
    protected void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        storedData = new CompoundTag();
        for (String key : COOKING_DATA) {
            tag.read(key, ExtraCodecs.NBT).ifPresent(value -> storedData.put(key, value));
        }
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        for (String key : storedData.keySet()) {
            tag.store(key, ExtraCodecs.NBT, storedData.get(key));
        }
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
            stack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(getType(), storedData.copy()));
        }
    }
}
