package com.medsal15.data.loot_tables;

import static com.medsal15.ExtraStuck.modid;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.medsal15.ExtraStuck;
import com.medsal15.loot_modifiers.ESTerrainLootModifier;
import com.medsal15.loot_modifiers.ESTitleLootModifier;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.LandTypes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

public class ESGLMProvider extends GlobalLootModifierProvider {
    public ESGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ExtraStuck.MODID);
    }

    @Override
    protected void start() {
        // #region Land Terrains
        add("medium_heat", new ESTerrainLootModifier(new LootItemCondition[0], modid("chests/inject/medium_heat"),
                Minestuck.id("chests/medium_basic"), LandTypes.HEAT.get()), List.of());
        add("medium_frost", new ESTerrainLootModifier(new LootItemCondition[0], modid("chests/inject/medium_frost"),
                Minestuck.id("chests/medium_basic"), LandTypes.FROST.get()), List.of());
        add("medium_end", new ESTerrainLootModifier(new LootItemCondition[0], modid("chests/inject/medium_end"),
                Minestuck.id("chests/medium_basic"), LandTypes.END.get()), List.of());
        add("medium_rain", new ESTerrainLootModifier(new LootItemCondition[0], modid("chests/inject/medium_rain"),
                Minestuck.id("chests/medium_basic"), LandTypes.RAIN.get()), List.of());
        add("medium_rock", new ESTerrainLootModifier(new LootItemCondition[0], modid("chests/inject/medium_rock"),
                Minestuck.id("chests/medium_basic"), LandTypes.ROCK.get()), List.of());
        // #endregion Land Terrains

        // #region Land Titles
        add("medium_thunder", new ESTitleLootModifier(new LootItemCondition[0], modid("chests/inject/medium_thunder"),
                Minestuck.id("chests/medium_basic"), LandTypes.THUNDER.get()), List.of());
        // #endregion Land Titles
    }
}
