package com.medsal15.data;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.medsal15.ExtraStuck;
import com.medsal15.data.ESLootTableProvider.TableSubProvider;
import com.medsal15.items.ESItemTypes;
import com.medsal15.loot_modifiers.BoondollarLootModifier;
import com.medsal15.loot_modifiers.ESLandLootModifier;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.LandTypes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.CanItemPerformAbility;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

public class ESGLMProvider extends GlobalLootModifierProvider {
    public ESGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ExtraStuck.MODID);
    }

    @Override
    protected void start() {
        // #region Land Terrains
        add("medium_heat", new ESLandLootModifier(
                new LootItemCondition[] { LootTableIdCondition.builder(Minestuck.id("chests/medium_basic")).build() },
                TableSubProvider.INJECT_TERRAIN_HEAT.location(),
                Optional.of(LandTypes.HEAT.get()), Optional.empty()), List.of());
        add("medium_frost", new ESLandLootModifier(
                new LootItemCondition[] { LootTableIdCondition.builder(Minestuck.id("chests/medium_basic")).build() },
                TableSubProvider.INJECT_TERRAIN_FROST.location(),
                Optional.of(LandTypes.FROST.get()), Optional.empty()), List.of());
        add("medium_end", new ESLandLootModifier(
                new LootItemCondition[] { LootTableIdCondition.builder(Minestuck.id("chests/medium_basic")).build() },
                TableSubProvider.INJECT_TERRAIN_END.location(),
                Optional.of(LandTypes.END.get()), Optional.empty()), List.of());
        add("medium_rain", new ESLandLootModifier(
                new LootItemCondition[] { LootTableIdCondition.builder(Minestuck.id("chests/medium_basic")).build() },
                TableSubProvider.INJECT_TERRAIN_RAIN.location(),
                Optional.of(LandTypes.RAIN.get()), Optional.empty()), List.of());
        add("medium_rock", new ESLandLootModifier(
                new LootItemCondition[] { LootTableIdCondition.builder(Minestuck.id("chests/medium_basic")).build() },
                TableSubProvider.INJECT_TERRAIN_ROCK.location(),
                Optional.of(LandTypes.ROCK.get()), Optional.empty()), List.of());
        // #endregion Land Terrains

        // #region Land Titles
        add("medium_thunder",
                new ESLandLootModifier(
                        new LootItemCondition[] {
                                LootTableIdCondition.builder(Minestuck.id("chests/medium_basic")).build() },
                        TableSubProvider.INJECT_TITLE_THUNDER.location(),
                        Optional.empty(), Optional.of(LandTypes.THUNDER.get())),
                List.of());
        add("medium_cake",
                new ESLandLootModifier(
                        new LootItemCondition[] {
                                LootTableIdCondition.builder(Minestuck.id("chests/medium_basic")).build() },
                        TableSubProvider.INJECT_TITLE_CAKE.location(),
                        Optional.empty(), Optional.of(LandTypes.CAKE.get())),
                List.of());
        // #endregion Land Titles

        add("perfectly_unique_spellbook",
                new ESLandLootModifier(
                        new LootItemCondition[] {
                                LootTableIdCondition.builder(Minestuck.id("chests/medium_basic")).build() },
                        TableSubProvider.INJECT_PUSB.location(), Optional.empty(), Optional.empty()),
                List.of(new ModLoadedCondition("irons_spellbooks")));

        add("boondollar_mining", new BoondollarLootModifier(
                new LootItemCondition[] { new CanItemPerformAbility(ESItemTypes.BOONDOLLAR_MINING) }, 0.5F));
    }
}
