package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public final class ESItemTags extends ItemTagsProvider {
    public ESItemTags(PackOutput output, CompletableFuture<Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, ExistingFileHelper fileHelper) {
        super(output, lookupProvider, blockTags, ExtraStuck.MODID, fileHelper);
    }

    @Override
    protected void addTags(@Nonnull Provider provider) {
        for (var shield : ESItems.getShields()) {
            tagShield(shield);
        }

        for (var arrow : ESItems.getArrows()) {
            tag(ItemTags.ARROWS).add(arrow.get());
        }

        tagShield(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE);

        tag(ItemTags.PIGLIN_LOVED).add(ESItems.GOLD_SHIELD.get());

        tag(Tags.Items.TOOLS_SPEAR).add(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD.get(),
                ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
        tag(Tags.Items.RANGED_WEAPON_TOOLS).add(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD.get(),
                ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
        tag(ItemTags.TRIDENT_ENCHANTABLE).add(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD.get(),
                ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
        tag(ItemTags.BREAKS_DECORATED_POTS).add(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
    }

    private void tagShield(DeferredItem<Item> item) {
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(item.get());
        tag(ItemTags.VANISHING_ENCHANTABLE).add(item.get());
        tag(Tags.Items.TOOLS_SHIELD).add(item.get());
    }
}
