package com.medsal15.compat;

import java.util.HashMap;
import java.util.Map;

import net.neoforged.fml.ModList;

public final class ESCompatUtils {
    private static final Map<String, Boolean> MODS_LOADED = new HashMap<>();

    public static boolean isLoaded(String modid) {
        if (!MODS_LOADED.containsKey(modid)) {
            MODS_LOADED.put(modid, ModList.get().isLoaded(modid));
        }

        return MODS_LOADED.get(modid);
    }
}
