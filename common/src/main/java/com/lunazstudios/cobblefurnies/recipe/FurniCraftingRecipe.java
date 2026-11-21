package com.lunazstudios.cobblefurnies.recipe;

import com.lunazstudios.cobblefurnies.registry.CFRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FurniCraftingRecipe implements Recipe<SingleRecipeInput> {
    private final List<CountedIngredient> materials;
    private final ItemStackTemplate result;
    private final boolean notification;

    public FurniCraftingRecipe(List<CountedIngredient> materials, ItemStackTemplate result, boolean notification) {
        this.materials = materials;
        this.result = result;
        this.notification = notification;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        ItemStack inputStack = input.getItem(0);
        if (inputStack.isEmpty()) return false;

        for (CountedIngredient ci : materials) {
            if (ci.ingredient().test(inputStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(SingleRecipeInput input) {
        return this.result.create();
    }

    @Override
    public boolean showNotification() { return notification; }

    @Override
    public String group() { return ""; }

    @Override
    public boolean isSpecial() { return true; }

    @Override
    public PlacementInfo placementInfo() { return PlacementInfo.NOT_PLACEABLE; }

    @Override
    public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.CRAFTING_MISC; }

    @Override
    public @NotNull RecipeSerializer<FurniCraftingRecipe> getSerializer() {
        return CFRecipes.FURNI_CRAFTING_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<FurniCraftingRecipe> getType() {
        return CFRecipes.FURNI_CRAFTING_RECIPE_TYPE;
    }

    public List<CountedIngredient> getMaterials() {
        return this.materials;
    }

    
    public ItemStack[] getMaterialStacks() {
        return this.materials.stream()
                .map(ci -> {
                    ItemStack[] stacks = ci.alternatives();
                    if (stacks.length > 0) {
                        ItemStack stack = stacks[0].copy();
                        stack.setCount(ci.count());
                        return stack;
                    } else {
                        return ItemStack.EMPTY;
                    }
                }).toArray(ItemStack[]::new);
    }

    public ItemStack getResult() {
        return this.result.create();
    }

    
    public static class Serializer {

        public static final MapCodec<FurniCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        CountedIngredient.CODEC.listOf().fieldOf("materials").forGetter(recipe -> recipe.materials),
                        ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.BOOL.optionalFieldOf("show_notification", false).forGetter(recipe -> recipe.notification)
                ).apply(instance, FurniCraftingRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, FurniCraftingRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, recipe) -> {
                    buf.writeVarInt(recipe.materials.size());
                    for (CountedIngredient ci : recipe.materials) {
                        buf.writeVarInt(ci.count());
                        
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ci.ingredient());
                    }
                    ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.result);
                    buf.writeBoolean(recipe.notification);
                },
                buf -> {
                    int size = buf.readVarInt();
                    List<CountedIngredient> materials = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        int count = buf.readVarInt();
                        Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                        materials.add(new CountedIngredient(ingredient, count));
                    }
                    ItemStackTemplate result = ItemStackTemplate.STREAM_CODEC.decode(buf);
                    boolean notification = buf.readBoolean();
                    return new FurniCraftingRecipe(materials, result, notification);
                }
        );

    }
}
