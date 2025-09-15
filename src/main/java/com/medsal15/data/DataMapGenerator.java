package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import com.medsal15.datamaps.ReactorFuel;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public final class DataMapGenerator extends DataMapProvider {
    public DataMapGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void gather() {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(ESItems.WOODEN_SHIELD, new FurnaceFuel(200), false)
                .add(ESItems.ROLLING_PIN, new FurnaceFuel(200), false);

        builder(ReactorFuel.REACTOR_MAP)
                .add(MSItems.ENERGY_CORE, new ReactorFuel(128, ESItems.EMPTY_ENERGY_CORE.toStack()), false)
                .add(MSItems.PLUSH_MUTATED_CAT, new ReactorFuel(0, ESItems.NORMAL_CAT_PLUSH.toStack()), false);
    }
}
