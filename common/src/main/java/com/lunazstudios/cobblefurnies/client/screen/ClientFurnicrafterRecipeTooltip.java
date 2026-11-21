package com.lunazstudios.cobblefurnies.client.screen;

import com.lunazstudios.cobblefurnies.menu.FurniCrafterMenu;
import com.lunazstudios.cobblefurnies.recipe.CountedIngredient;
import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;

public class ClientFurnicrafterRecipeTooltip implements ClientTooltipComponent {
    private final FurniCrafterMenu menu;
    private final FurniCraftingRecipe recipe;

    public ClientFurnicrafterRecipeTooltip(FurniCrafterMenu menu, FurniCraftingRecipe recipe) {
        this.menu = menu;
        this.recipe = recipe;
    }

    @Override
    public int getHeight(Font font) {
        return 20;
    }

    @Override
    public int getWidth(Font font) {
        
        return this.recipe.getMaterials().size() * 18;
    }

    @Override
    public void extractImage(Font font, int start, int top, int width, int height, GuiGraphicsExtractor graphics) {
        Map<Integer, Integer> counted = new HashMap<>();
        List<CountedIngredient> materials = this.recipe.getMaterials();
        for (int i = 0; i < materials.size(); i++) {
            CountedIngredient material = materials.get(i);
            ItemStack copy = getStack(material).copy();
            copy.setCount(material.count());
            
            graphics.fakeItem(copy, start + i * 18, top);
            graphics.itemDecorations(font, copy, start + i * 18, top);

            
            
            boolean hasEnough = this.menu.hasMaterials(material, counted);
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, FurniCrafterScreen.TEXTURE, start + i * 18, top, hasEnough ? 246 : 240, 40, 6, 5, 256, 256);
        }
    }

    private ItemStack getStack(CountedIngredient material) {
        ItemStack[] items = material.alternatives();
        if (items.length == 0) {
            System.out.println("Empty ingredient stack for recipe");
            return ItemStack.EMPTY;
        }
        int index = (int) ((Util.getMillis() / 1000) % items.length);
        return items[index];
    }
}
