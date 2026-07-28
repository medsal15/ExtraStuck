package com.medsal15.world.structures;

import com.medsal15.ExtraStuck;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public final class ESStructures {
    /** Slightly altered version of the vanilla ancient city to allow lands */
    public static final ResourceKey<Structure> ANCIENT_CITY = key("ancient_city");

    private static ResourceKey<Structure> key(String name) {
        return ResourceKey.create(Registries.STRUCTURE, ExtraStuck.modid(name));
    }
}
