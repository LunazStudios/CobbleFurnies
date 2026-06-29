package com.lunazstudios.cobblefurnies.block.properties;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum CurtainVerticalPart implements StringRepresentable {
    TOP, BOTTOM;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
