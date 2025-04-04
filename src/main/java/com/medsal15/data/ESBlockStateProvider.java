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
        simpleBlock(ESBlocks.GARNET_BRICKS.get());
        stairsBlock(ESBlocks.GARNET_BRICK_STAIRS.get(), modLoc("block/garnet_bricks"));
        slabBlock(ESBlocks.GARNET_BRICK_SLAB.get(), modLoc("block/garnet_bricks"), modLoc("block/garnet_bricks"));
        wallBlock(ESBlocks.GARNET_BRICK_WALL.get(), modLoc("block/garnet_bricks"));
    }
}
