package com.medsal15.items.components;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;

public enum MoonCakeSliceColor implements StringRepresentable {
    DUAL,
    DERSE,
    PROSPIT;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static MoonCakeSliceColor fromName(String string) {
        for (MoonCakeSliceColor layer : MoonCakeSliceColor.values()) {
            if (layer.name().toLowerCase().equals(string))
                return layer;
        }
        throw new IllegalArgumentException("Invalid moon cake color " + string);
    }
}
