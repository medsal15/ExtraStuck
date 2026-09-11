package com.medsal15.items.components;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;

public enum PanCakeSliceColor implements StringRepresentable {
    MAGENTA,
    YELLOW,
    CYAN,
    TRIPLE;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static PanCakeSliceColor fromName(String string) {
        for (PanCakeSliceColor color : PanCakeSliceColor.values()) {
            if (color.name().toLowerCase().equals(string))
                return color;
        }
        throw new IllegalArgumentException("Invalid pan cake color " + string);
    }
}
