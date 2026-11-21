package com.lunazstudios.cobblefurnies.network;

import com.lunazstudios.cobblefurnies.network.message.CraftRecipeMessage;
import com.lunazstudios.cobblefurnies.network.message.FurniRecipesMessage;
import com.lunazstudios.cobblefurnies.network.message.SyncCraftableRecipesMessage;
import com.lunazstudios.cobblefurnies.recipe.FurniRecipes;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class CFNetwork {
    public static final CustomPacketPayload.Type<CraftRecipeMessage> CRAFT_RECIPE_TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("cobblefurnies", "craft_recipe"));
    public static final CustomPacketPayload.Type<SyncCraftableRecipesMessage> CRAFTABLE_RECIPES_SYNC_TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("cobblefurnies", "craftable_recipes_sync"));

    public static void registerPackets() {
        NetworkManager.registerReceiver(
                NetworkManager.Side.C2S,
                CRAFT_RECIPE_TYPE,
                CraftRecipeMessage.CODEC,
                CraftRecipeMessage::handle
        );

        LifecycleEvent.DATAPACK_SYNC.register((player, joined) -> NetworkManager.sendToPlayer(player,
                new FurniRecipesMessage(FurniRecipes.onServer(player.level()))));

        if (Platform.getEnv() == EnvType.CLIENT) {
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, FurniRecipesMessage.TYPE, FurniRecipesMessage.CODEC,
                    (message, context) -> context.queue(() -> FurniRecipes.sync(message.recipes())));
            NetworkManager.registerReceiver(
                    NetworkManager.Side.S2C,
                    CRAFTABLE_RECIPES_SYNC_TYPE,
                    SyncCraftableRecipesMessage.CODEC,
                    SyncCraftableRecipesMessage::handle
            );
        } else {
            NetworkManager.registerS2CPayloadType(FurniRecipesMessage.TYPE, FurniRecipesMessage.CODEC);
            NetworkManager.registerS2CPayloadType(
                    CRAFTABLE_RECIPES_SYNC_TYPE,
                    SyncCraftableRecipesMessage.CODEC
            );
        }
    }
}
