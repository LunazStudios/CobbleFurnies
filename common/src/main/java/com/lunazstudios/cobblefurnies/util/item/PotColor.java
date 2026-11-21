package com.lunazstudios.cobblefurnies.util.item;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

public enum PotColor implements StringRepresentable {
    RED, YELLOW, WHITE, PINK, GREEN, BLUE, BLACK;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

}
