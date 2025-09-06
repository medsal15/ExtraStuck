package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.blocks.ESBlocks;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class ESBlockTags extends BlockTagsProvider {
    public ESBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper fileHelper) {
        super(output, lookupProvider, ExtraStuck.MODID, fileHelper);
    }

    public static final TagKey<Block> INCORRECT_FOR_COPPER_TIER = BlockTags
            .create(ExtraStuck.modid("incorrect_for_copper_tier"));
    public static final TagKey<Block> MINEABLE_WITH_DICE = BlockTags.create(ExtraStuck.modid("mineable_with_dice"));

    @Override
    protected void addTags(@Nonnull Provider provider) {
        tag(INCORRECT_FOR_COPPER_TIER).addTag(BlockTags.INCORRECT_FOR_STONE_TOOL);

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ESBlocks.CUT_GARNET.get(), ESBlocks.CUT_GARNET_STAIRS.get(), ESBlocks.CUT_GARNET_SLAB.get(),
                ESBlocks.CUT_GARNET_WALL.get(), ESBlocks.GARNET_BRICKS.get(), ESBlocks.GARNET_BRICK_STAIRS.get(),
                ESBlocks.GARNET_BRICK_SLAB.get(), ESBlocks.GARNET_BRICK_WALL.get(),
                ESBlocks.CHISELED_GARNET_BRICKS.get(), ESBlocks.CUT_RUBY.get(), ESBlocks.CUT_RUBY_STAIRS.get(),
                ESBlocks.CUT_GARNET_SLAB.get(), ESBlocks.CUT_GARNET_WALL.get(), ESBlocks.RUBY_BRICKS.get(),
                ESBlocks.RUBY_BRICK_STAIRS.get(), ESBlocks.RUBY_BRICK_SLAB.get(), ESBlocks.RUBY_BRICK_WALL.get(),
                ESBlocks.CHISELED_RUBY_BRICKS.get(), ESBlocks.COBALT_BLOCK.get(), ESBlocks.COBALT_BARS.get(),
                ESBlocks.COBALT_DOOR.get(), ESBlocks.COBALT_TRAPDOOR.get(), ESBlocks.COBALT_PRESSURE_PLATE.get(),
                ESBlocks.SULFUROUS_STONE.get(), ESBlocks.SULFUROUS_STONE_STAIRS.get(),
                ESBlocks.SULFUROUS_STONE_SLAB.get(), ESBlocks.SULFUROUS_STONE_WALL.get(), ESBlocks.MARBLE.get(),
                ESBlocks.MARBLE_STAIRS.get(), ESBlocks.MARBLE_SLAB.get(), ESBlocks.MARBLE_WALL.get(),
                ESBlocks.POLISHED_MARBLE.get(), ESBlocks.POLISHED_MARBLE_STAIRS.get(),
                ESBlocks.POLISHED_MARBLE_SLAB.get(), ESBlocks.POLISHED_MARBLE_WALL.get(),
                ESBlocks.MARBLE_BRICKS.get(), ESBlocks.MARBLE_BRICK_STAIRS.get(),
                ESBlocks.MARBLE_BRICK_SLAB.get(), ESBlocks.MARBLE_BRICK_WALL.get(),
                ESBlocks.ZILLIUM_BRICKS.get(), ESBlocks.ZILLIUM_BRICK_STAIRS.get(),
                ESBlocks.ZILLIUM_BRICK_SLAB.get(), ESBlocks.ZILLIUM_BRICK_WALL.get(),
                ESBlocks.CARD_ORE.get(), ESBlocks.PRINTER.get(), ESBlocks.CHARGER.get());
        tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(ESBlocks.PRINTER.get(), ESBlocks.CHARGER.get());

        tag(BlockTags.WALLS).add(ESBlocks.CUT_GARNET_WALL.get(), ESBlocks.GARNET_BRICK_WALL.get(),
                ESBlocks.CUT_RUBY_WALL.get(), ESBlocks.RUBY_BRICK_WALL.get(), ESBlocks.SULFUROUS_STONE_WALL.get(),
                ESBlocks.MARBLE_WALL.get(), ESBlocks.POLISHED_MARBLE_WALL.get(), ESBlocks.MARBLE_BRICK_WALL.get(),
                ESBlocks.ZILLIUM_BRICK_WALL.get());

        tag(BlockTags.DOORS).add(ESBlocks.COBALT_DOOR.get());
        tag(BlockTags.TRAPDOORS).add(ESBlocks.COBALT_TRAPDOOR.get());

        tag(BlockTags.INFINIBURN_OVERWORLD).add(ESBlocks.SULFUROUS_STONE.get(), ESBlocks.SULFUROUS_STONE_STAIRS.get(),
                ESBlocks.SULFUROUS_STONE_SLAB.get(), ESBlocks.SULFUROUS_STONE_WALL.get());
        tag(BlockTags.INFINIBURN_NETHER).add(ESBlocks.SULFUROUS_STONE.get(), ESBlocks.SULFUROUS_STONE_STAIRS.get(),
                ESBlocks.SULFUROUS_STONE_SLAB.get(), ESBlocks.SULFUROUS_STONE_WALL.get());
        tag(BlockTags.INFINIBURN_END).add(ESBlocks.SULFUROUS_STONE.get(), ESBlocks.SULFUROUS_STONE_STAIRS.get(),
                ESBlocks.SULFUROUS_STONE_SLAB.get(), ESBlocks.SULFUROUS_STONE_WALL.get());
    }
}
