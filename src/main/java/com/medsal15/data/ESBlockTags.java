package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class ESBlockTags extends BlockTagsProvider {
    public ESBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper fileHelper) {
        super(output, lookupProvider, ExtraStuck.MODID, fileHelper);
    }

    @Override
    protected void addTags(@Nonnull Provider arg0) {
        // empty because I need tags for items
    }
}
