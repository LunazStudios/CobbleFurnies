package com.lunazstudios.cobblefurnies.compat;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.recipe.CountedIngredient;
import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import com.lunazstudios.cobblefurnies.registry.CFBlocks;
import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FurniCrafterRecipeCategory implements IRecipeCategory<FurniCraftingRecipe> {
    public static final Identifier UID = Identifier.fromNamespaceAndPath(CobbleFurnies.MOD_ID, "furni_crafting");
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(CobbleFurnies.MOD_ID, "textures/gui/jei/furnicrafter.png");

    public static final RecipeType<FurniCraftingRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, FurniCraftingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public FurniCrafterRecipeCategory(IGuiHelper helper) {
        this.background = helper.drawableBuilder(TEXTURE, 0, 0, 150, 49)
                .setTextureSize(150, 49)
                .build();
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CFBlocks.FURNI_CRAFTER.get()));
    }

    @Override
    public RecipeType<FurniCraftingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("FurniCrafter");
    }

    @Override
    public int getWidth() { return background.getWidth(); }

    @Override
    public int getHeight() { return background.getHeight(); }

    @Override
    public void draw(FurniCraftingRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView slots, net.minecraft.client.gui.GuiGraphicsExtractor graphics, double x, double y) {
        background.draw(graphics);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FurniCraftingRecipe recipe, IFocusGroup focuses) {
        List<CountedIngredient> ingredients = recipe.getMaterials();
        int slotCount = ingredients.size();
        int slotWidth = 18;
        int spacing = 2;
        int totalWidth = slotCount * slotWidth + (slotCount - 1) * spacing;
        int startX = (100 - totalWidth) / 2;
        int slotY = 16;

        for (int i = 0; i < slotCount; i++) {
            int slotX = startX + i * (slotWidth + spacing);
            CountedIngredient ci = ingredients.get(i);
            ItemStack[] alternatives = ci.alternatives();
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack stack : alternatives) {
                ItemStack copy = stack.copy();
                copy.setCount(ci.count());
                stacks.add(copy);
            }
            builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY)
                    .addItemStacks(stacks);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 115, 16)
                .addItemStack(recipe.getResult());
    }
}
