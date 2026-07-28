package com.medsal15.data;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.medsal15.ESDamageTypes;
import com.medsal15.ExtraStuck;
import com.medsal15.world.structures.ESStructures;
import com.mraof.minestuck.util.MSTags;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.AncientCityStructurePieces;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = ExtraStuck.MODID)
public final class ESData {
    @SubscribeEvent
    public static final void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        LanguageProvider langProvider = gen.addProvider(event.includeClient(), new ESLangProvider(output));
        gen.addProvider(event.includeClient(), new ESBlockStateProvider(output, fileHelper));
        gen.addProvider(event.includeClient(), new ESItemModelProvider(output, fileHelper));
        gen.addProvider(event.includeClient(), new ESSoundDefinitions(output, fileHelper));
        gen.addProvider(event.includeClient(), new ESParticleDescriptions(output, fileHelper));

        DatapackBuiltinEntriesProvider datapackProvider = gen.addProvider(
                event.includeServer(),
                new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(),
                        new RegistrySetBuilder()
                                .add(Registries.DAMAGE_TYPE, ESData::registerDamageTypes)
                                .add(Registries.STRUCTURE, ESData::registerStructures),
                        Set.of(ExtraStuck.MODID)));
        CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();

        gen.addProvider(event.includeServer(), new ESRecipeProvider(output, lookupProvider));
        gen.addProvider(event.includeServer(), new DataMapGenerator(output, lookupProvider));
        gen.addProvider(event.includeServer(),
                (DataProvider.Factory<ESLootTableProvider>) (o -> new ESLootTableProvider(o, lookupProvider)));
        gen.addProvider(event.includeServer(), new ESGLMProvider(output, lookupProvider));
        gen.addProvider(event.includeServer(), new ESPriceProvider(output));
        gen.addProvider(event.includeServer(), new ESVisionEffectsProvider(output, ExtraStuck.MODID));
        gen.addProvider(event.includeServer(),
                ESAdvancementsProvider.create(output, lookupProvider, fileHelper));
        gen.addProvider(event.includeServer(), new ESCassetteSongsProvider(output));
        gen.addProvider(event.includeServer(),
                ESDialoguesProvider.consort(output, langProvider, event.getLookupProvider()));
        gen.addProvider(event.includeServer(),
                ESDialoguesProvider.generalShop(output, langProvider, event.getLookupProvider()));
        gen.addProvider(event.includeServer(),
                ESDialoguesProvider.foodShop(output, langProvider, event.getLookupProvider()));
        gen.addProvider(event.includeServer(), new ESLandTypeExtensionProvider(output, lookupProvider));

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

    public static void registerStructures(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        context.register(ESStructures.ANCIENT_CITY,
                new JigsawStructure(
                        new Structure.StructureSettings.Builder(biomes.getOrThrow(MSTags.Biomes.LAND))
                                .spawnOverrides(Arrays.stream(MobCategory.values())
                                        .collect(Collectors.toMap(category -> (MobCategory) category,
                                                category -> new StructureSpawnOverride(
                                                        StructureSpawnOverride.BoundingBoxType.STRUCTURE,
                                                        WeightedRandomList.create()))))
                                .generationStep(GenerationStep.Decoration.UNDERGROUND_DECORATION)
                                .terrainAdapation(TerrainAdjustment.BEARD_BOX).build(),
                        pools.getOrThrow(AncientCityStructurePieces.START),
                        Optional.of(ResourceLocation.withDefaultNamespace("city_anchor")), 7,
                        ConstantHeight.of(VerticalAnchor.absolute(-27)), false, Optional.empty(), 116, List.of(),
                        JigsawStructure.DEFAULT_DIMENSION_PADDING, JigsawStructure.DEFAULT_LIQUID_SETTINGS));
    }
}
