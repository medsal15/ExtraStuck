package com.medsal15.data;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.medsal15.ExtraStuck;
import com.medsal15.data.ESLootTableProvider.TableSubProvider;
import com.medsal15.items.ESItemTypes;
import com.medsal15.loot.modifiers.BoondollarLootModifier;
import com.medsal15.loot.modifiers.ESLandLootModifier;
import com.mraof.minestuck.Minestuck;

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
        add("medium_heat", new ESLandLootModifier(
                new LootItemCondition[] { LootTableIdCondition.builder(Minestuck.id("chests/medium_basic")).build() },
                TableSubProvider.INJECT_MEDIUM.location(), Optional.empty(), Optional.empty()), List.of());

        add("perfectly_unique_spellbook", new ESLandLootModifier(
                new LootItemCondition[] { LootTableIdCondition.builder(Minestuck.id("chests/medium_basic")).build() },
                TableSubProvider.INJECT_PUSB.location(), Optional.empty(), Optional.empty()),
                List.of(new ModLoadedCondition("irons_spellbooks")));

        add("boondollar_mining", new BoondollarLootModifier(
                new LootItemCondition[] { new CanItemPerformAbility(ESItemTypes.BOONDOLLAR_MINING) }, 0.5F));
    }
}
