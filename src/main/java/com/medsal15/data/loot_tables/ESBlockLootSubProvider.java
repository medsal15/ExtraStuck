package com.medsal15.data.loot_tables;

import java.util.Set;

import com.medsal15.blocks.ESBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

public class ESBlockLootSubProvider extends BlockLootSubProvider {
    public ESBlockLootSubProvider(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ESBlocks.BLOCKS.getEntries()
                .stream()
                // Cast to Block here, otherwise it will be a ? extends Block and Java will
                // complain.
                .map(e -> (Block) e.value())
                .toList();
    }

    @Override
    protected void generate() {
        dropSelf(ESBlocks.CUT_GARNET.get());
        dropSelf(ESBlocks.CUT_GARNET_STAIRS.get());
        add(ESBlocks.CUT_GARNET_SLAB.get(), createSlabItemTable(ESBlocks.CUT_GARNET_SLAB.get()));
        dropSelf(ESBlocks.CUT_GARNET_WALL.get());
        dropSelf(ESBlocks.GARNET_BRICKS.get());
        dropSelf(ESBlocks.GARNET_BRICK_STAIRS.get());
        add(ESBlocks.GARNET_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.GARNET_BRICK_SLAB.get()));
        dropSelf(ESBlocks.GARNET_BRICK_WALL.get());
        dropSelf(ESBlocks.CHISELED_GARNET_BRICKS.get());

        dropSelf(ESBlocks.CUT_RUBY.get());
        dropSelf(ESBlocks.CUT_RUBY_STAIRS.get());
        add(ESBlocks.CUT_RUBY_SLAB.get(), createSlabItemTable(ESBlocks.CUT_RUBY_SLAB.get()));
        dropSelf(ESBlocks.CUT_RUBY_WALL.get());
        dropSelf(ESBlocks.RUBY_BRICKS.get());
        dropSelf(ESBlocks.RUBY_BRICK_STAIRS.get());
        add(ESBlocks.RUBY_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.RUBY_BRICK_SLAB.get()));
        dropSelf(ESBlocks.RUBY_BRICK_WALL.get());
        dropSelf(ESBlocks.CHISELED_RUBY_BRICKS.get());

        dropSelf(ESBlocks.COBALT_BLOCK.get());
        dropSelf(ESBlocks.COBALT_BARS.get());
        add(ESBlocks.COBALT_DOOR.get(), this::createDoorTable);
        dropSelf(ESBlocks.COBALT_TRAPDOOR.get());
        dropSelf(ESBlocks.COBALT_PRESSURE_PLATE.get());

        dropSelf(ESBlocks.SULFUROUS_STONE.get());
        dropSelf(ESBlocks.SULFUROUS_STONE_STAIRS.get());
        add(ESBlocks.SULFUROUS_STONE_SLAB.get(), createSlabItemTable(ESBlocks.SULFUROUS_STONE_SLAB.get()));
        dropSelf(ESBlocks.SULFUROUS_STONE_WALL.get());

        dropSelf(ESBlocks.MARBLE.get());
        dropSelf(ESBlocks.MARBLE_STAIRS.get());
        add(ESBlocks.MARBLE_SLAB.get(), createSlabItemTable(ESBlocks.MARBLE_SLAB.get()));
        dropSelf(ESBlocks.MARBLE_WALL.get());
        dropSelf(ESBlocks.POLISHED_MARBLE.get());
        dropSelf(ESBlocks.POLISHED_MARBLE_STAIRS.get());
        add(ESBlocks.POLISHED_MARBLE_SLAB.get(), createSlabItemTable(ESBlocks.POLISHED_MARBLE_SLAB.get()));
        dropSelf(ESBlocks.POLISHED_MARBLE_WALL.get());
        dropSelf(ESBlocks.MARBLE_BRICKS.get());
        dropSelf(ESBlocks.MARBLE_BRICK_STAIRS.get());
        add(ESBlocks.MARBLE_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.MARBLE_BRICK_SLAB.get()));
        dropSelf(ESBlocks.MARBLE_BRICK_WALL.get());

        dropSelf(ESBlocks.ZILLIUM_BRICKS.get());
        dropSelf(ESBlocks.ZILLIUM_BRICK_STAIRS.get());
        add(ESBlocks.ZILLIUM_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.ZILLIUM_BRICK_SLAB.get()));
        dropSelf(ESBlocks.ZILLIUM_BRICK_WALL.get());

        add(ESBlocks.PIZZA.get(), noDrop());
        add(ESBlocks.CARD_ORE.get(), noDrop());
        dropSelf(ESBlocks.PRINTER.get());
    }
}
