package com.medsal15.world.land.terrains;

import com.medsal15.ExtraStuck;
import com.medsal15.blocks.ESBlocks;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.TurtleVillagePieces;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.neoforged.neoforge.common.Tags;

public class DarkLandTerrain extends TerrainLandType {
    public static final String DEEP = ExtraStuck.MODID + ".terrain.deep";
    public static final String DARK = ExtraStuck.MODID + ".terrain.dark";

    public DarkLandTerrain() {
        super(new Builder(MSEntityTypes.TURTLE).names(DEEP, DARK).fogColor(0, 0, 0.1).skylight(0));
    }

    @Override
    public void registerBlocks(StructureBlockRegistry registry) {
        registry.setBlock(StructureBlockRegistry.CRUXITE_ORE, ESBlocks.DEEPSLATE_CRUXITE_ORE);
        registry.setBlock(StructureBlockRegistry.URANIUM_ORE, MSBlocks.DEEPSLATE_URANIUM_ORE);
        registry.setBlock(StructureBlockRegistry.GROUND, Blocks.DEEPSLATE);
        registry.setBlock(StructureBlockRegistry.UPPER, Blocks.DEEPSLATE);
        registry.setBlock(StructureBlockRegistry.SURFACE, Blocks.COBBLED_DEEPSLATE);
        registry.setBlock(StructureBlockRegistry.SAND, MSBlocks.BLACK_SAND);

        registry.setBlock(StructureBlockRegistry.STRUCTURE_PRIMARY, Blocks.DEEPSLATE_BRICKS);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PRIMARY_DECORATIVE, Blocks.POLISHED_DEEPSLATE);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PRIMARY_CRACKED, Blocks.CRACKED_DEEPSLATE_BRICKS);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PRIMARY_COLUMN, ESBlocks.DEEPSLATE_PILLAR);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PRIMARY_STAIRS, Blocks.DEEPSLATE_BRICK_STAIRS);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PRIMARY_SLAB, Blocks.DEEPSLATE_BRICK_SLAB);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PRIMARY_WALL, Blocks.DEEPSLATE_BRICK_WALL);

        registry.setBlock(StructureBlockRegistry.STRUCTURE_SECONDARY, Blocks.DEEPSLATE_TILES);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_SECONDARY_DECORATIVE, Blocks.CHISELED_DEEPSLATE);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_SECONDARY_STAIRS, Blocks.DEEPSLATE_TILE_STAIRS);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_SECONDARY_SLAB, Blocks.DEEPSLATE_TILE_SLAB);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_SECONDARY_WALL, Blocks.DEEPSLATE_TILE_WALL);

        registry.setBlock(StructureBlockRegistry.STRUCTURE_WOOD, Blocks.DARK_OAK_WOOD);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_LOG, Blocks.DARK_OAK_LOG);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_STRIPPED_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_STRIPPED_LOG, Blocks.STRIPPED_DARK_OAK_LOG);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PLANKS, Blocks.DARK_OAK_PLANKS);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PLANKS_STAIRS, Blocks.DARK_OAK_STAIRS);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PLANKS_SLAB, Blocks.DARK_OAK_SLAB);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PLANKS_FENCE, Blocks.DARK_OAK_FENCE);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PLANKS_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PLANKS_DOOR, Blocks.DARK_OAK_DOOR);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_PLANKS_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR);

        registry.setBlock(StructureBlockRegistry.STRUCTURE_WOOL_1, Blocks.GRAY_WOOL);
        registry.setBlock(StructureBlockRegistry.STRUCTURE_WOOL_3, Blocks.CYAN_WOOL);

        registry.setBlock(StructureBlockRegistry.STRUCTURE_GROUND_COVER, Blocks.SCULK_VEIN);

        registry.setBlock(StructureBlockRegistry.VILLAGE_PATH, Blocks.POLISHED_DEEPSLATE);
        registry.setBlock(StructureBlockRegistry.VILLAGE_FENCE, Blocks.POLISHED_DEEPSLATE_WALL);

        registry.setBlock(StructureBlockRegistry.LIGHT_BLOCK, MSBlocks.GLOWY_GOOP);
        registry.setBlock(StructureBlockRegistry.TORCH, Blocks.SOUL_TORCH);
        registry.setBlock(StructureBlockRegistry.WALL_TORCH, Blocks.SOUL_WALL_TORCH);
        registry.setBlock(StructureBlockRegistry.STAINED_GLASS_1, Blocks.BLACK_STAINED_GLASS);
        registry.setBlock(StructureBlockRegistry.STAINED_GLASS_2, Blocks.TINTED_GLASS);
    }

    @Override
    public void addExtensions(Provider provider, StructureBlockRegistry blocks) {
        HolderLookup.RegistryLookup<PlacedFeature> features = provider.lookupOrThrow(Registries.PLACED_FEATURE);
        HolderLookup.RegistryLookup<Structure> structures = provider.lookupOrThrow(Registries.STRUCTURE);
        HolderLookup.RegistryLookup<ConfiguredWorldCarver<?>> carvers = provider
                .lookupOrThrow(Registries.CONFIGURED_CARVER);

        addFeatureExtension(features, GenerationStep.Decoration.LOCAL_MODIFICATIONS, CavePlacements.AMETHYST_GEODE);
        addFeatureExtension(features, GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.SCULK_VEIN);
        addFeatureExtension(features, GenerationStep.Decoration.UNDERGROUND_DECORATION,
                CavePlacements.SCULK_PATCH_DEEP_DARK);

        addFeatureExtension(features, GenerationStep.Decoration.VEGETAL_DECORATION,
                MSPlacedFeatures.SPARSE_GLOWING_MUSHROOM_PATCH, LandBiomeType.NORMAL);
        addFeatureExtension(features, GenerationStep.Decoration.VEGETAL_DECORATION,
                MSPlacedFeatures.GLOWING_MUSHROOM_PATCH, LandBiomeType.ROUGH);
        addFeatureExtension(features, GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.GLOW_LICHEN,
                LandBiomeType.ROUGH, LandBiomeType.NORMAL);

        addFeatureExtension(GenerationStep.Decoration.UNDERGROUND_ORES,
                MSPlacedFeatures.inline(Feature.ORE,
                        new OreConfiguration(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33),
                        CountPlacement.of(10), InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)),
                        BiomeFilter.biome()),
                LandBiomeType.any());
        addFeatureExtension(GenerationStep.Decoration.UNDERGROUND_ORES,
                MSPlacedFeatures.inline(Feature.ORE,
                        new OreConfiguration(blocks.getGroundType(), Blocks.DEEPSLATE_IRON_ORE.defaultBlockState(), 8),
                        CountPlacement.of(40), InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)),
                        BiomeFilter.biome()),
                LandBiomeType.any());
        addFeatureExtension(GenerationStep.Decoration.UNDERGROUND_ORES,
                MSPlacedFeatures.inline(Feature.ORE,
                        new OreConfiguration(blocks.getGroundType(), Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState(), 9),
                        CountPlacement.of(12), InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)),
                        BiomeFilter.biome()),
                LandBiomeType.any());
        addFeatureExtension(GenerationStep.Decoration.UNDERGROUND_ORES,
                MSPlacedFeatures.inline(Feature.ORE,
                        new OreConfiguration(blocks.getGroundType(), Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState(),
                                6),
                        CountPlacement.of(11), InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(24)),
                        BiomeFilter.biome()),
                LandBiomeType.any());

        addStructureExtension(new StructureSet(structures.getOrThrow(BuiltinStructures.ANCIENT_CITY),
                new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, 880027512)));

        addCarverExtension(GenerationStep.Carving.AIR,
                WorldCarver.CAVE.configured(new CaveCarverConfiguration(0.08F,
                        UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(180)),
                        UniformFloat.of(0.1F, 0.9F), VerticalAnchor.aboveBottom(8),
                        BuiltInRegistries.BLOCK.getOrCreateTag(Tags.Blocks.STONES), UniformFloat.of(0.7F, 4.4F),
                        UniformFloat.of(0.8F, 4.3F), UniformFloat.of(-1.0F, -0.4F))),
                LandBiomeType.any());
        // No cave generates (despite this explicitly say so)
        addCarverExtension(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE), LandBiomeType.any());
        addCarverExtension(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND),
                LandBiomeType.any());
        addCarverExtension(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON), LandBiomeType.any());
    }

    @Override
    public void setProperties(LandProperties properties) {
        properties.forceRain = LandProperties.ForceType.DEFAULT;
    }

    @Override
    public void setGenSettings(LandGenSettings settings) {
        settings.oceanThreshold = -0.3F;
    }

    @Override
    public void addVillageCenters(CenterRegister register) {
        TurtleVillagePieces.addCenters(register);
    }

    @Override
    public void addVillagePieces(PieceRegister register, RandomSource random) {
        TurtleVillagePieces.addPieces(register, random);
    }
}
