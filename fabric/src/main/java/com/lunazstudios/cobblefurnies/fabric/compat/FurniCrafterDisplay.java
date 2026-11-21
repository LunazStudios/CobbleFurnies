package com.lunazstudios.cobblefurnies.fabric.compat;

import com.lunazstudios.cobblefurnies.recipe.CountedIngredient;
import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class FurniCrafterDisplay extends BasicDisplay {

    private final FurniCraftingRecipe recipe;
    private static final me.shedaniel.rei.api.common.display.DisplaySerializer<FurniCrafterDisplay> SERIALIZER =
            me.shedaniel.rei.api.common.display.DisplaySerializer.of(
                    FurniCraftingRecipe.Serializer.CODEC.xmap(FurniCrafterDisplay::new, display -> display.recipe),
                    FurniCraftingRecipe.Serializer.STREAM_CODEC.map(FurniCrafterDisplay::new, display -> display.recipe));

    @Override
    public me.shedaniel.rei.api.common.display.DisplaySerializer<FurniCrafterDisplay> getSerializer() {
        return SERIALIZER;
    }

    public FurniCrafterDisplay(RecipeHolder<FurniCraftingRecipe> recipeHolder) {
        this(recipeHolder.value());
    }

    private FurniCrafterDisplay(FurniCraftingRecipe recipe) {
        super(createInputs(recipe), createOutputs(recipe));
        this.recipe = recipe;
    }

    private static List<EntryIngredient> createInputs(FurniCraftingRecipe recipe) {
        List<EntryIngredient> inputs = new ArrayList<>();
        
        for (CountedIngredient ci : recipe.getMaterials()) {
            
            ItemStack[] alternatives = ci.alternatives();
            
            List<EntryStack<?>> stacks = Arrays.stream(alternatives)
                    .map(stack -> {
                        ItemStack copy = stack.copy();
                        copy.setCount(ci.count());
                        return EntryStacks.of(copy);
                    })
                    .collect(Collectors.toList());
            
            inputs.add(EntryIngredient.of(stacks));
        }
        return inputs;
    }

    private static List<EntryIngredient> createOutputs(FurniCraftingRecipe recipe) {
        return Collections.singletonList(EntryIngredient.of(EntryStacks.of(recipe.getResult())));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return FurniCrafterCategory.FURNICRAFTER;
    }
}
