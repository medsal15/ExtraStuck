package com.medsal15.items.components;

import java.util.List;
import java.util.Locale;

import net.minecraft.util.StringRepresentable;

public enum GristLayer implements StringRepresentable {
    COMMON,
    UNCOMMON,
    ANY;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static GristLayer fromName(String string) {
        for (GristLayer layer : GristLayer.values()) {
            if (layer.name().toLowerCase().equals(string))
                return layer;
        }
        throw new IllegalArgumentException("Invalid grist layer " + string);
    }

    public GristLayer next() {
        List<GristLayer> values = List.of(GristLayer.values());
        int i = (values.indexOf(this) + 1) % values.size();
        return values.get(i);
    }
}
