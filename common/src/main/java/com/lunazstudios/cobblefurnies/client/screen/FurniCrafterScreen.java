package com.lunazstudios.cobblefurnies.client.screen;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.menu.FurniCrafterMenu;
import com.lunazstudios.cobblefurnies.network.message.CraftRecipeMessage;
import com.lunazstudios.cobblefurnies.recipe.CountedIngredient;
import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.networking.NetworkManager;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeHolder;

public class FurniCrafterScreen extends AbstractContainerScreen<FurniCrafterMenu> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(CobbleFurnies.MOD_ID, "textures/gui/container/furnicrafter.png");

    private static final int RECIPES_PER_ROW = 6;
    private static final int BUTTON_SIZE = 20;
    private static final int GRID_X_OFFSET = 7;
    private static final int GRID_Y_OFFSET = 18;
    private static final int WINDOW_WIDTH = RECIPES_PER_ROW * BUTTON_SIZE;
    private static final int WINDOW_HEIGHT = 88;
    private static final int SCROLL_SPEED = 10;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int HANDLE_TRAVEL = WINDOW_HEIGHT - SCROLLBAR_HEIGHT;

    private static final int SCROLLBAR_TEXTURE_ENABLED_X = 176;
    private static final int SCROLLBAR_TEXTURE_DISABLED_X = 188;
    private static final int SCROLLBAR_TEXTURE_Y = 40;

    private static final int Y_OFFSET_CORRECTION = 0;

    private List<RecipeHolder<FurniCraftingRecipe>> allRecipes = new ArrayList<>();
    private List<RecipeHolder<FurniCraftingRecipe>> visibleRecipes = new ArrayList<>();

    private EditBox searchBox;
    private double scroll = 0;
    private int hoveredRecipeIndex = -1;
    private double scrollbarDragOffset = -1;
    private String lastQuery = "";

    public FurniCrafterScreen(FurniCrafterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 204);
        this.inventoryLabelY = 110;
        updateRecipes();
    }

    @Override
    protected void init() {
        super.init();
        scrollbarDragOffset = -1;

        int x = this.leftPos + 80;
        int y = this.topPos + 5;

        this.searchBox = new EditBox(
                this.font,
                x,
                y,
                WINDOW_WIDTH - 30,
                12,
                Component.translatable("gui.cobblefurnies.furnicrafter.search")
        );
        this.searchBox.setMaxLength(64);
        this.searchBox.setBordered(true);
        this.searchBox.setResponder(s -> applySearchFilter());

        this.addRenderableWidget(this.searchBox);

        applySearchFilter();
    }

    private void updateRecipes() {
        this.allRecipes = new ArrayList<>(menu.getAvailableRecipes());
        applySearchFilter();
    }

    private void applySearchFilter() {
        String query = "";
        if (searchBox != null) {
            query = searchBox.getValue().trim().toLowerCase(Locale.ROOT);
        }

        if (!query.equals(lastQuery)) {
            this.scroll = 0;
            this.hoveredRecipeIndex = -1;
            lastQuery = query;
        }

        if (query.isEmpty()) {
            this.visibleRecipes = new ArrayList<>(this.allRecipes);
        } else {
            String q = query;
            this.visibleRecipes = this.allRecipes.stream()
                    .filter(holder -> holder.value()
                            .getResult()
                            .getHoverName()
                            .getString()
                            .toLowerCase(Locale.ROOT)
                            .contains(q))
                    .toList();
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        if (hoveredRecipeIndex != -1 && isMouseWithinBounds(mouseX, mouseY, leftPos + GRID_X_OFFSET, topPos + GRID_Y_OFFSET, WINDOW_WIDTH, WINDOW_HEIGHT)) {
            renderRecipeTooltip(graphics, mouseX, mouseY, hoveredRecipeIndex);
        }
    }

    private void renderRecipeTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int recipeIndex) {
        RecipeHolder<FurniCraftingRecipe> holder = visibleRecipes.get(recipeIndex);
        FurniCraftingRecipe recipe = holder.value();

        List<ClientTooltipComponent> components = new ArrayList<>();
        components.add(new ClientTextTooltip(
                recipe.getResult().getHoverName().getVisualOrderText()
        ));

        if (!Minecraft.getInstance().hasShiftDown()) {
            components.add(new ClientFurnicrafterRecipeTooltip(menu, recipe));
            components.add(new ClientTextTooltip(
                    Component.translatable("gui.cobblefurnies.hold_shift_for_details")
                            .withStyle(net.minecraft.ChatFormatting.GRAY)
                            .getVisualOrderText()
            ));
        } else {
            Map<Integer, Integer> counted = new HashMap<>();
            for (CountedIngredient ingredient : recipe.getMaterials()) {
                components.add(new ClientFurnicrafterRecipeIngredientTooltip(menu, ingredient, counted));
            }
        }

        graphics.tooltip(this.font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null, false);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        renderRecipes(graphics, mouseX, mouseY);
        renderScrollbar(graphics);
        super.extractContents(graphics, mouseX, mouseY, partialTicks);
    }

    private void renderRecipes(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        hoveredRecipeIndex = -1;
        int clipX = leftPos + GRID_X_OFFSET;
        int clipY = topPos + GRID_Y_OFFSET;
        graphics.enableScissor(clipX, clipY, clipX + WINDOW_WIDTH, clipY + WINDOW_HEIGHT);

        int totalRecipes = visibleRecipes.size();
        int startRow = (int) (scroll / BUTTON_SIZE);
        int startIndex = startRow * RECIPES_PER_ROW;
        int rowsToDraw = (int) Math.ceil(WINDOW_HEIGHT / (double) BUTTON_SIZE) + 1;
        int endIndex = Math.min(totalRecipes, startIndex + rowsToDraw * RECIPES_PER_ROW);

        boolean mouseInGrid = isMouseWithinBounds(mouseX, mouseY, clipX, clipY, WINDOW_WIDTH, WINDOW_HEIGHT);

        for (int i = startIndex; i < endIndex; i++) {
            int row = i / RECIPES_PER_ROW;
            int col = i % RECIPES_PER_ROW;
            int x = leftPos + GRID_X_OFFSET + col * BUTTON_SIZE;
            int y = topPos + GRID_Y_OFFSET + row * BUTTON_SIZE - (int) scroll - Y_OFFSET_CORRECTION;

            FurniCraftingRecipe recipe = visibleRecipes.get(i).value();
            boolean canCraft = menu.canCraft(recipe);

            int textureU = 176 + (!canCraft ? BUTTON_SIZE : 0);
            int textureV = 0;
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, textureU, textureV, BUTTON_SIZE, BUTTON_SIZE, 256, 256);
            graphics.fakeItem(recipe.getResult(), x + 2, y + 2);

            if (mouseInGrid && mouseX >= x && mouseX < x + BUTTON_SIZE && mouseY >= y && mouseY < y + BUTTON_SIZE) {
                hoveredRecipeIndex = i;
            }
        }
        graphics.disableScissor();
    }

    private void renderScrollbar(GuiGraphicsExtractor graphics) {
        int maxScroll = getMaxScroll();
        int textureX = maxScroll > 0 ? SCROLLBAR_TEXTURE_ENABLED_X : SCROLLBAR_TEXTURE_DISABLED_X;
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, TEXTURE, scrollbarX(), handleY(maxScroll), textureX, SCROLLBAR_TEXTURE_Y, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT, 256, 256);
    }

    private int scrollbarX() {
        return leftPos + GRID_X_OFFSET + WINDOW_WIDTH + 3;
    }

    private int scrollbarY() {
        return topPos + GRID_Y_OFFSET - Y_OFFSET_CORRECTION;
    }

    private int handleY(int maxScroll) {
        return scrollbarY() + (maxScroll > 0 ? (int) (scroll / maxScroll * HANDLE_TRAVEL) : 0);
    }

    private void scrollToMouse(double mouseY) {
        double fraction = (mouseY - scrollbarY() - scrollbarDragOffset) / HANDLE_TRAVEL;
        scroll = Mth.clamp(fraction, 0.0, 1.0) * getMaxScroll();
    }

    private int getMaxScroll() {
        int totalRows = (int) Math.ceil(visibleRecipes.size() / (double) RECIPES_PER_ROW);
        return Math.max(0, totalRows * BUTTON_SIZE - WINDOW_HEIGHT);
    }

    private boolean isMouseWithinBounds(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();
        if (event.button() == InputConstants.MOUSE_BUTTON_LEFT) {
            int maxScroll = getMaxScroll();
            if (isMouseWithinBounds(mouseX, mouseY, scrollbarX(), scrollbarY(), SCROLLBAR_WIDTH, WINDOW_HEIGHT)) {
                if (maxScroll > 0) {
                    double offset = mouseY - handleY(maxScroll);
                    if (offset >= 0 && offset < SCROLLBAR_HEIGHT) {
                        scrollbarDragOffset = offset;
                    } else {
                        scrollbarDragOffset = SCROLLBAR_HEIGHT / 2.0;
                        scrollToMouse(mouseY);
                    }
                }
                return true;
            }
            int gridX = leftPos + GRID_X_OFFSET;
            int gridY = topPos + GRID_Y_OFFSET;
            if (isMouseWithinBounds(mouseX, mouseY, gridX, gridY, WINDOW_WIDTH, WINDOW_HEIGHT)) {
                int column = (int) (mouseX - gridX) / BUTTON_SIZE;
                int row = (int) (mouseY - gridY + (int) scroll + Y_OFFSET_CORRECTION) / BUTTON_SIZE;
                int index = row * RECIPES_PER_ROW + column;
                if (index < visibleRecipes.size()) {
                    craftItem(index, getRequestedAmount());
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    return true;
                }
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.searchBox != null && this.searchBox.isFocused()) {

            if (event.key() == InputConstants.KEY_ESCAPE) {
                this.searchBox.setFocused(false);
                return true;
            }

            if (this.minecraft != null
                    && this.minecraft.options != null
                    && this.minecraft.options.keyInventory.matches(event)) {
                return true;
            }

            if (this.searchBox.keyPressed(event)) {
                return true;
            }

            return true;
        }

        return super.keyPressed(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (this.searchBox != null && this.searchBox.isFocused()) {
            if (this.searchBox.charTyped(event)) {
                return true;
            }
        }
        return super.charTyped(event);
    }

    private int getRequestedAmount() {
        if (Minecraft.getInstance().hasShiftDown()) {
            return 64;
        } else if (Minecraft.getInstance().hasControlDown()) {
            return 16;
        }
        return 1;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if (event.button() == InputConstants.MOUSE_BUTTON_LEFT && scrollbarDragOffset >= 0) {
            scrollToMouse(event.y());
            return true;
        }
        return super.mouseDragged(event, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == InputConstants.MOUSE_BUTTON_LEFT && scrollbarDragOffset >= 0) {
            scrollbarDragOffset = -1;
            return true;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (scrollbarDragOffset < 0 && isMouseWithinBounds(mouseX, mouseY, leftPos + GRID_X_OFFSET, topPos + GRID_Y_OFFSET, WINDOW_WIDTH + 15, WINDOW_HEIGHT)) {
            scroll = Mth.clamp(scroll - deltaY * SCROLL_SPEED, 0, getMaxScroll());
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
    }

    private void craftItem(int recipeIndex, int amount) {
        if (recipeIndex < 0 || recipeIndex >= visibleRecipes.size()) return;

        RecipeHolder<FurniCraftingRecipe> holder = visibleRecipes.get(recipeIndex);
        int serverIndex = menu.getAvailableRecipes().indexOf(holder);
        if (serverIndex < 0) return;

        CraftRecipeMessage message = new CraftRecipeMessage(menu.containerId, serverIndex, amount);
        NetworkManager.sendToServer(message);
    }

    public void updateRecipeButtons() {
        updateRecipes();
    }
}

