package com.medsal15.data.loot_tables;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;

public class ESLootTableProvider extends LootTableProvider {
    public ESLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
            List<SubProviderEntry> tables) {
        super(output, Set.of(), tables, lookupProvider);
    }
}
