package com.medsal15.utils;

import net.neoforged.fml.ModList;

public final class ModDependant {
    /**
     * Returns the first value if the mod is loaded
     */
    public static <T> T otherModNeeded(String modid, T present, T missing) {
        if (ModList.get().isLoaded(modid))
            return present;
        return missing;
    }
}
