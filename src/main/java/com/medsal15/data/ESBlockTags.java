package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.blocks.ESBlocks;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class ESBlockTags extends BlockTagsProvider {
    public ESBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper fileHelper) {
        super(output, lookupProvider, ExtraStuck.MODID, fileHelper);
    }

    @Override
    protected void addTags(@Nonnull Provider arg0) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ESBlocks.GARNET_BRICKS.get(), ESBlocks.GARNET_BRICK_STAIRS.get(),
                ESBlocks.GARNET_BRICK_SLAB.get(), ESBlocks.GARNET_BRICK_WALL.get());
        tag(BlockTags.WALLS).add(ESBlocks.GARNET_BRICK_WALL.get());
    }
}
