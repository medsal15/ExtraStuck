package com.medsal15.utils;

import com.medsal15.ExtraStuck;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ESTags {
    public static final class Items {
        public static final TagKey<Item> AMMO = ItemTags.create(ExtraStuck.modid("ammo"));
        public static final TagKey<Item> AMMO_HANDGUN = ItemTags.create(ExtraStuck.modid("ammo/handgun"));
        public static final TagKey<Item> TOOLS_ROLLING_PIN = ItemTags.create(common("tools/rolling_pin"));
        public static final TagKey<Item> SHOW_VALUE = ItemTags.create(ExtraStuck.modid("show_value"));
        public static final TagKey<Item> URANIUM_RODS = ItemTags.create(common("rods/uranium"));
        public static final TagKey<Item> BRASS_NUGGETS = ItemTags.create(common("nuggets/brass"));
        public static final TagKey<Item> BEE_EGGS = ItemTags.create(common("eggs/bee"));
        public static final TagKey<Item> DRINKS = ItemTags.create(common("drinks"));

        public static final TagKey<Item> IGNORE_BYPRODUCT_CUTTING = ItemTags
                .create(ExtraStuck.modid("interpreters_ignore_byproduct/cutting"));
    }

    public static final class Blocks {
        public static final TagKey<Block> INCORRECT_FOR_COPPER_TIER = BlockTags
                .create(ExtraStuck.modid("incorrect_for_copper_tier"));
        public static final TagKey<Block> MINEABLE_WITH_DICE = BlockTags.create(ExtraStuck.modid("mineable_with_dice"));
    }

    private static ResourceLocation common(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }
}
