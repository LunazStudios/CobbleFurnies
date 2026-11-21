package com.lunazstudios.cobblefurnies.compat;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import com.lunazstudios.cobblefurnies.recipe.FurniRecipes;
import java.util.List;
import java.util.stream.Collectors;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;

@JeiPlugin
public class JEICobbleFurniesPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(CobbleFurnies.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FurniCrafterRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<FurniCraftingRecipe> furniCraftingRecipes = FurniRecipes.onClient().stream()
                .map(RecipeHolder::value)
                .collect(Collectors.toList());

        registration.addRecipes(FurniCrafterRecipeCategory.RECIPE_TYPE, furniCraftingRecipes);
    }
}
