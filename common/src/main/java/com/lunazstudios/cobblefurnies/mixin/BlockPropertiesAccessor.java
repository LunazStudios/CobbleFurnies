package com.lunazstudios.cobblefurnies.mixin;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.Properties.class)
public interface BlockPropertiesAccessor {
    @Accessor("requiredFeatures")
    void cobblefurnies$setRequiredFeatures(FeatureFlagSet features);
}
