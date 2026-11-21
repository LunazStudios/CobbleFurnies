package com.lunazstudios.cobblefurnies.compat;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.mixin.BlockPropertiesAccessor;
import dev.architectury.platform.Platform;
import net.minecraft.world.flag.FeatureFlagRegistry;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class CobblemonCompat {
    public static final boolean LOADED = Platform.isModLoaded("cobblemon");
    private static final FeatureFlagSet UNAVAILABLE = FeatureFlagSet.of(new FeatureFlagRegistry.Builder("cobblefurnies")
            .create(CobbleFurnies.id("requires_cobblemon")));

    private CobblemonCompat() {}

    public static BlockBehaviour.Properties unavailable(BlockBehaviour.Properties properties) {
        ((BlockPropertiesAccessor) properties).cobblefurnies$setRequiredFeatures(UNAVAILABLE);
        return properties;
    }
}
