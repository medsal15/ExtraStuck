package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ESItems;
import com.medsal15.ExtraStuck;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ESItemTags extends ItemTagsProvider {
    public ESItemTags(PackOutput output, CompletableFuture<Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, ExistingFileHelper fileHelper) {
        super(output, lookupProvider, blockTags, ExtraStuck.MODID, fileHelper);
    }

    @Override
    protected void addTags(@Nonnull Provider provider) {
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(ESItems.WOODEN_SHIELD.get());
        tag(ItemTags.VANISHING_ENCHANTABLE).add(ESItems.WOODEN_SHIELD.get());
        tag(Tags.Items.TOOLS_SHIELD).add(ESItems.WOODEN_SHIELD.get());
    }
}
