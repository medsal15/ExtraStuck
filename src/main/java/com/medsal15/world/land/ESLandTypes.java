package com.medsal15.world.land;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;
import com.medsal15.world.land.terrains.DarkLandTerrain;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;

import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESLandTypes {
    public static final DeferredRegister<TerrainLandType> TERRAINS = DeferredRegister
            .create(LandTypes.TERRAIN_KEY.location(), ExtraStuck.MODID);

    public static final Supplier<TerrainLandType> DARK = TERRAINS.register("dark", DarkLandTerrain::new);
}
