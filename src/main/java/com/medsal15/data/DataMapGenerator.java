package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import com.medsal15.items.ESItems;

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
                .add(ESItems.WOODEN_SHIELD, new FurnaceFuel(200), false);
    }
}
