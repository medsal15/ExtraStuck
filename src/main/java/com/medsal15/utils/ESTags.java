package com.medsal15.utils;

import com.medsal15.ExtraStuck;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ESTags {
    public static final class Items {
        public static final TagKey<Item> AMMO = ItemTags.create(ExtraStuck.modid("ammo"));
        public static final TagKey<Item> AMMO_HANDGUN = ItemTags.create(ExtraStuck.modid("ammo/handgun"));
        public static final TagKey<Item> SHOW_VALUE = ItemTags.create(ExtraStuck.modid("show_value"));
        public static final TagKey<Item> IGNORE_BYPRODUCT_CUTTING = ItemTags
                .create(ExtraStuck.modid("interpreters_ignore_byproduct/cutting"));
        public static final TagKey<Item> DROPS_BOONDOLLARS = ItemTags.create(ExtraStuck.modid("drops_boondollars"));

        public static final TagKey<Item> TOOLS_ROLLING_PIN = ItemTags.create(common("tools/rolling_pin"));
        public static final TagKey<Item> URANIUM_RODS = ItemTags.create(common("rods/uranium"));
        public static final TagKey<Item> BRASS_NUGGETS = ItemTags.create(common("nuggets/brass"));
        public static final TagKey<Item> BEE_EGGS = ItemTags.create(common("eggs/bee"));
        public static final TagKey<Item> DRINKS = ItemTags.create(common("drinks"));
        public static final TagKey<Item> LEMON_FRUITS = ItemTags.create(common("foods/fruit/lemon"));
        public static final TagKey<Item> LEMON_CROPS = ItemTags.create(common("crops/lemon"));
    }

    public static final class Blocks {
        public static final TagKey<Block> PRYABLE = BlockTags.create(ExtraStuck.modid("pryable"));

        public static final TagKey<Block> INCORRECT_FOR_COPPER_TIER = BlockTags
                .create(ExtraStuck.modid("incorrect_for_copper_tier"));

        public static final TagKey<Block> MINEABLE_WITH_DICE = BlockTags.create(ExtraStuck.modid("mineable_with_dice"));
    }

    public static final class EntityTypes {
        public static final TagKey<EntityType<?>> BEENADE_ACCEPTS = create("beenade_accepts");

        private static TagKey<EntityType<?>> create(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ExtraStuck.modid(name));
        }
    }

    private static ResourceLocation common(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }
}
