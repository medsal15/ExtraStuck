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
import net.minecraft.tags.TagKey;
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

    public static final TagKey<Item> AMMO = ItemTags.create(ExtraStuck.modid("ammo"));
    public static final TagKey<Item> AMMO_HANDGUN = ItemTags.create(ExtraStuck.modid("ammo/handgun"));

    @Override
    protected void addTags(@Nonnull Provider provider) {
        for (DeferredItem<Item> shield : ESItems.getShields()) {
            tagShield(shield);
        }

        for (DeferredItem<Item> arrow : ESItems.getArrows()) {
            tag(ItemTags.ARROWS).add(arrow.get());
        }

        for (DeferredItem<Item> weapon : ESItems.getMeleeWeapons()) {
            tagWeapon(weapon);
        }

        for (DeferredItem<Item> tool : ESItems.getMiningTools()) {
            tag(ItemTags.MINING_ENCHANTABLE).add(tool.get());
            tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(tool.get());
        }

        tag(AMMO).addTag(AMMO_HANDGUN);
        tag(AMMO_HANDGUN).add(ESItems.HANDGUN_BULLET.get(), ESItems.HEAVY_HANDGUN_BULLET.get());

        tagShield(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE);

        tag(ItemTags.PIGLIN_LOVED).add(ESItems.GOLD_SHIELD.get());

        tag(Tags.Items.TOOLS_SPEAR).add(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD.get(),
                ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
        tag(Tags.Items.RANGED_WEAPON_TOOLS).add(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD.get(),
                ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
        tag(ItemTags.TRIDENT_ENCHANTABLE).add(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD.get(),
                ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
        tag(ItemTags.BREAKS_DECORATED_POTS).add(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());

        tag(Tags.Items.FOODS).add(ESItems.SWEET_TOOTH.get());

        tag(ItemTags.DOORS).add(ESItems.COBALT_DOOR.get());
        tag(ItemTags.TRAPDOORS).add(ESItems.COBALT_TRAPDOOR.get());
    }

    private void tagShield(DeferredItem<Item> item) {
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(item.get());
        tag(ItemTags.VANISHING_ENCHANTABLE).add(item.get());
        tag(Tags.Items.TOOLS_SHIELD).add(item.get());
    }

    private void tagWeapon(DeferredItem<Item> item) {
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(item.get());
        tag(ItemTags.VANISHING_ENCHANTABLE).add(item.get());
        tag(ItemTags.SWORD_ENCHANTABLE).add(item.get());
        tag(ItemTags.WEAPON_ENCHANTABLE).add(item.get());
        tag(Tags.Items.MELEE_WEAPON_TOOLS).add(item.get());
    }
}
