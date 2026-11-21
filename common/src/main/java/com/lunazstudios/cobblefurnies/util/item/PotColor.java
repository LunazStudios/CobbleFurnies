package com.lunazstudios.cobblefurnies.util.item;

import com.cobblemon.mod.common.CobblemonItems;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum PotColor implements StringRepresentable {
    RED, YELLOW, WHITE, PINK, GREEN, BLUE, BLACK;

    public Item getItem() {
        return Items.forColor(this);
    }

    private static final class Items {
        private static Item forColor(PotColor color) {
            return switch (color) {
                case RED -> CobblemonItems.CAMPFIRE_POT_RED;
                case YELLOW -> CobblemonItems.CAMPFIRE_POT_YELLOW;
                case WHITE -> CobblemonItems.CAMPFIRE_POT_WHITE;
                case PINK -> CobblemonItems.CAMPFIRE_POT_PINK;
                case GREEN -> CobblemonItems.CAMPFIRE_POT_GREEN;
                case BLUE -> CobblemonItems.CAMPFIRE_POT_BLUE;
                case BLACK -> CobblemonItems.CAMPFIRE_POT_BLACK;
            };
        }
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static @Nullable PotColor fromItem(Item item) {
        for (PotColor color : values()) {
            if (color.getItem() == item) return color;
        }
        return null;
    }
}
