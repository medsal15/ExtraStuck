package com.medsal15.data;

import com.medsal15.ExtraStuck;
import com.medsal15.blocks.ESBlocks;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ESBlockStateProvider extends BlockStateProvider {
    public ESBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExtraStuck.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ESBlocks.CUT_GARNET.get());
        stairsBlock(ESBlocks.CUT_GARNET_STAIRS.get(), modLoc("block/cut_garnet"));
        slabBlock(ESBlocks.CUT_GARNET_SLAB.get(), modLoc("block/cut_garnet"), modLoc("block/cut_garnet"));
        wallBlock(ESBlocks.CUT_GARNET_WALL.get(), modLoc("block/cut_garnet"));
        simpleBlock(ESBlocks.GARNET_BRICKS.get());
        stairsBlock(ESBlocks.GARNET_BRICK_STAIRS.get(), modLoc("block/garnet_bricks"));
        slabBlock(ESBlocks.GARNET_BRICK_SLAB.get(), modLoc("block/garnet_bricks"), modLoc("block/garnet_bricks"));
        wallBlock(ESBlocks.GARNET_BRICK_WALL.get(), modLoc("block/garnet_bricks"));
        simpleBlock(ESBlocks.CHISELED_GARNET_BRICKS.get());

        simpleBlock(ESBlocks.CUT_RUBY.get());
        stairsBlock(ESBlocks.CUT_RUBY_STAIRS.get(), modLoc("block/cut_ruby"));
        slabBlock(ESBlocks.CUT_RUBY_SLAB.get(), modLoc("block/cut_ruby"), modLoc("block/cut_ruby"));
        wallBlock(ESBlocks.CUT_RUBY_WALL.get(), modLoc("block/cut_ruby"));
        simpleBlock(ESBlocks.RUBY_BRICKS.get());
        stairsBlock(ESBlocks.RUBY_BRICK_STAIRS.get(), modLoc("block/ruby_bricks"));
        slabBlock(ESBlocks.RUBY_BRICK_SLAB.get(), modLoc("block/ruby_bricks"), modLoc("block/ruby_bricks"));
        wallBlock(ESBlocks.RUBY_BRICK_WALL.get(), modLoc("block/ruby_bricks"));
        simpleBlock(ESBlocks.CHISELED_RUBY_BRICKS.get());
    }
}
