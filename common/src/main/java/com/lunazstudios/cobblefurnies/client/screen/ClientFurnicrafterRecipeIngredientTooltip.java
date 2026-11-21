package com.lunazstudios.cobblefurnies.client.screen;

import com.lunazstudios.cobblefurnies.menu.FurniCrafterMenu;
import com.lunazstudios.cobblefurnies.recipe.CountedIngredient;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;

public class ClientFurnicrafterRecipeIngredientTooltip implements ClientTooltipComponent {
    private final FurniCrafterMenu menu;
    private final CountedIngredient material;
    private final Map<Integer, Integer> counted;

    public ClientFurnicrafterRecipeIngredientTooltip(FurniCrafterMenu menu, CountedIngredient material, Map<Integer, Integer> counted) {
        this.menu = menu;
        this.material = material;
        this.counted = counted;
    }

    @Override
    public int getHeight(Font font) {
        return 18;
    }

    @Override
    public int getWidth(Font font) {
        
        return 18 + font.width(getStack().getDisplayName());
    }

    @Override
    public void extractImage(Font font, int start, int top, int width, int height, GuiGraphicsExtractor graphics) {
        ItemStack stack = getStack().copy();
        stack.setCount(this.material.count());
        
        graphics.fakeItem(stack, start, top);
        graphics.itemDecorations(font, stack, start, top);
        
        MutableComponent name = stack.getHoverName().copy().withStyle(ChatFormatting.GRAY);
        graphics.text(font, name, start + 18 + 5, top + 4, 0xFFFFFFFF);

        
        boolean hasEnough = this.menu.hasMaterials(this.material, this.counted);
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, FurniCrafterScreen.TEXTURE, start, top, hasEnough ? 246 : 240, 40, 6, 5, 256, 256);
    }

    private ItemStack getStack() {
        ItemStack[] items = this.material.alternatives();
        int index = (int) ((Util.getMillis() / 1000) % items.length);
        return items[index];
    }
}
