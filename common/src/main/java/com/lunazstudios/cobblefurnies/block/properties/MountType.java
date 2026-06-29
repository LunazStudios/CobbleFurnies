package com.lunazstudios.cobblefurnies.block.properties;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum MountType implements StringRepresentable {
    FLOOR, WALL;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
