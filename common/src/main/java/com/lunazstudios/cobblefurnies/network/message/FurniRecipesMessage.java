package com.lunazstudios.cobblefurnies.network.message;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeHolder;

public record FurniRecipesMessage(List<RecipeHolder<FurniCraftingRecipe>> recipes) implements CustomPacketPayload {
    public static final Type<FurniRecipesMessage> TYPE = new Type<>(CobbleFurnies.id("furni_recipes"));
    private static final StreamCodec<RegistryFriendlyByteBuf, RecipeHolder<FurniCraftingRecipe>> ENTRY =
            StreamCodec.composite(ResourceKey.streamCodec(Registries.RECIPE), RecipeHolder::id,
                    FurniCraftingRecipe.Serializer.STREAM_CODEC, RecipeHolder::value, RecipeHolder::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, FurniRecipesMessage> CODEC =
            ENTRY.apply(ByteBufCodecs.list()).map(FurniRecipesMessage::new, FurniRecipesMessage::recipes);

    @Override
    public Type<?> type() { return TYPE; }
}
