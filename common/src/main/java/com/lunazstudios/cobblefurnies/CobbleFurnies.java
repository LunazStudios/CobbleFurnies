package com.lunazstudios.cobblefurnies;

import com.lunazstudios.cobblefurnies.network.CFNetwork;
import com.lunazstudios.cobblefurnies.registry.*;
import net.minecraft.resources.Identifier;

public final class CobbleFurnies {
    public static final String MOD_ID = "cobblefurnies";

    public static void init() {
        CFBlocks.init();
        CFBlockTags.init();
        CFEntityTypes.init();
        CFEntityTypeTags.init();
        CFBlockEntityTypes.init();
        CFRecipes.register();
        CFMenus.register();
        CFNetwork.registerPackets();
        CFSoundEvents.init();
    }

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }
}
