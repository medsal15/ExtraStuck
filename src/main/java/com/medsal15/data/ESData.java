package com.medsal15.data;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.medsal15.ESDamageTypes;
import com.medsal15.ExtraStuck;
import com.medsal15.data.loot_tables.ESBlockLootSubProvider;
import com.medsal15.data.loot_tables.ESGLMProvider;
import com.medsal15.data.loot_tables.ESLootSubProvider;
import com.medsal15.data.loot_tables.ESLootTableProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = ExtraStuck.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ESData {
    @SubscribeEvent
    public static final void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        gen.addProvider(event.includeClient(), new ESLangProvider(output));
        gen.addProvider(event.includeClient(), new ESBlockStateProvider(output, fileHelper));
        gen.addProvider(event.includeClient(), new ESItemModelProvider(output, fileHelper));
        gen.addProvider(event.includeClient(), new ESSoundDefinitions(output, fileHelper));

        gen.addProvider(
                event.includeServer(),
                new DatapackBuiltinEntriesProvider(output, lookupProvider,
                        new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, bootstrap -> {
                            bootstrap.register(ESDamageTypes.CAPTAIN_JUSTICE_PROJECTILE,
                                    new DamageType(ESDamageTypes.CAPTAIN_JUSTICE_PROJECTILE
                                            .location().toString(),
                                            DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
                                            .1F,
                                            DamageEffects.HURT,
                                            DeathMessageType.DEFAULT));
                            bootstrap.register(ESDamageTypes.THORN_SHIELD,
                                    new DamageType(ESDamageTypes.THORN_SHIELD
                                            .location().toString(),
                                            DamageScaling.NEVER,
                                            .1F,
                                            DamageEffects.THORNS,
                                            DeathMessageType.DEFAULT));
                        }), Set.of(ExtraStuck.MODID)));
        ESBlockTags blocktags = gen.addProvider(event.includeServer(),
                new ESBlockTags(output, lookupProvider, fileHelper));
        gen.addProvider(event.includeServer(), new ESRecipeProvider(output, lookupProvider));
        gen.addProvider(event.includeServer(),
                new ESItemTags(output, lookupProvider, blocktags.contentsGetter(), fileHelper));
        gen.addProvider(event.includeServer(), new DataMapGenerator(output, lookupProvider));
        gen.addProvider(event.includeServer(), new ESEntityTypeTags(output, lookupProvider, fileHelper));
        gen.addProvider(event.includeServer(),
                (DataProvider.Factory<ESLootTableProvider>) (o -> new ESLootTableProvider(o,
                        lookupProvider,
                        List.of(new SubProviderEntry(ESBlockLootSubProvider::new, LootContextParamSets.BLOCK),
                                new SubProviderEntry(ESLootSubProvider::new, LootContextParamSets.CHEST)))));
        gen.addProvider(event.includeServer(), new ESGLMProvider(output, lookupProvider));
    }
}
