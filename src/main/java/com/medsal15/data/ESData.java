package com.medsal15.data;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.medsal15.ESDamageTypes;
import com.medsal15.ExtraStuck;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = ExtraStuck.MODID)
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
        gen.addProvider(event.includeClient(), new ESParticleDescriptions(output, fileHelper));

        DatapackBuiltinEntriesProvider datapackProvider = gen.addProvider(
                event.includeServer(),
                new DatapackBuiltinEntriesProvider(output, lookupProvider,
                        new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, ESData::registerDamageTypes),
                        Set.of(ExtraStuck.MODID)));
        gen.addProvider(event.includeServer(), new ESRecipeProvider(output, lookupProvider));
        gen.addProvider(event.includeServer(), new DataMapGenerator(output, lookupProvider));
        gen.addProvider(event.includeServer(),
                (DataProvider.Factory<ESLootTableProvider>) (o -> new ESLootTableProvider(o, lookupProvider)));
        gen.addProvider(event.includeServer(), new ESGLMProvider(output, lookupProvider));
        gen.addProvider(event.includeServer(), new ESPriceProvider(output));
        gen.addProvider(event.includeServer(), new ESVisionEffectsProvider(output, ExtraStuck.MODID));
        gen.addProvider(event.includeServer(), ESAdvancementsProvider.create(output, lookupProvider, fileHelper));
        gen.addProvider(event.includeServer(), new ESCassetteSongsProvider(output));

        ESTagsProvider.gatherData(gen, output, lookupProvider, fileHelper, datapackProvider);
    }

    private static void registerDamageTypes(BootstrapContext<DamageType> bootstrap) {
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
        bootstrap.register(ESDamageTypes.COSMIC_PLAGUE,
                new DamageType(ESDamageTypes.COSMIC_PLAGUE
                        .location().toString(),
                        DamageScaling.NEVER,
                        .2F,
                        DamageEffects.HURT,
                        DeathMessageType.DEFAULT));
    }
}
