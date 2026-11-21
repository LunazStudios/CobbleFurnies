package com.lunazstudios.cobblefurnies.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record CountedIngredient(Ingredient ingredient, int count) {
    public static final Codec<CountedIngredient> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("item").forGetter(CountedIngredient::ingredient),
                    Codec.intRange(1, Integer.MAX_VALUE).fieldOf("count").forGetter(CountedIngredient::count)
            ).apply(instance, CountedIngredient::new)
    );

    public ItemStack[] alternatives() {
        return ingredient.items().map(ItemStack::new).toArray(ItemStack[]::new);
    }
}
