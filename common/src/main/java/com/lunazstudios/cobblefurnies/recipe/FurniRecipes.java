package com.lunazstudios.cobblefurnies.recipe;

import java.util.Comparator;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeHolder;

public final class FurniRecipes {
    private static List<RecipeHolder<FurniCraftingRecipe>> clientRecipes = List.of();

    public static List<RecipeHolder<FurniCraftingRecipe>> onServer(ServerLevel level) {
        return level.recipeAccess().getRecipes().stream()
                .filter(holder -> holder.value() instanceof FurniCraftingRecipe)
                .map(holder -> new RecipeHolder<>(holder.id(), (FurniCraftingRecipe) holder.value()))
                .filter(holder -> holder.value().getResult().isItemEnabled(level.enabledFeatures()))
                .sorted(Comparator.comparing(holder -> holder.id().identifier()))
                .toList();
    }

    public static List<RecipeHolder<FurniCraftingRecipe>> onClient() { return clientRecipes; }

    public static void sync(List<RecipeHolder<FurniCraftingRecipe>> recipes) { clientRecipes = List.copyOf(recipes); }
}
