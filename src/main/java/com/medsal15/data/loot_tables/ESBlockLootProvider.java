package com.medsal15.data.loot_tables;

import java.util.Set;

import com.medsal15.blocks.ESBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

public class ESBlockLootProvider extends BlockLootSubProvider {
    public ESBlockLootProvider(HolderLookup.Provider lookupProvider) {
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
        dropSelf(ESBlocks.CUT_GARNET_SLAB.get());
        dropSelf(ESBlocks.CUT_GARNET_WALL.get());
        dropSelf(ESBlocks.GARNET_BRICKS.get());
        dropSelf(ESBlocks.GARNET_BRICK_STAIRS.get());
        dropSelf(ESBlocks.GARNET_BRICK_SLAB.get());
        dropSelf(ESBlocks.GARNET_BRICK_WALL.get());
        dropSelf(ESBlocks.CHISELED_GARNET_BRICKS.get());

        dropSelf(ESBlocks.CUT_RUBY.get());
        dropSelf(ESBlocks.CUT_RUBY_STAIRS.get());
        dropSelf(ESBlocks.CUT_RUBY_SLAB.get());
        dropSelf(ESBlocks.CUT_RUBY_WALL.get());
        dropSelf(ESBlocks.RUBY_BRICKS.get());
        dropSelf(ESBlocks.RUBY_BRICK_STAIRS.get());
        dropSelf(ESBlocks.RUBY_BRICK_SLAB.get());
        dropSelf(ESBlocks.RUBY_BRICK_WALL.get());
        dropSelf(ESBlocks.CHISELED_RUBY_BRICKS.get());

        dropSelf(ESBlocks.COBALT_BLOCK.get());
        dropSelf(ESBlocks.COBALT_BARS.get());
        add(ESBlocks.COBALT_DOOR.get(), this::createDoorTable);
        dropSelf(ESBlocks.COBALT_TRAPDOOR.get());
        dropSelf(ESBlocks.COBALT_PRESSURE_PLATE.get());
    }
}
