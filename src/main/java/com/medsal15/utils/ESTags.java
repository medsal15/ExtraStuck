package com.medsal15.utils;

import com.medsal15.ExtraStuck;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluid;

public final class ESTags {
    public static final class Items {
        public static final TagKey<Item> AMMO = ItemTags.create(ExtraStuck.modid("ammo"));
        public static final TagKey<Item> AMMO_HANDGUN = ItemTags.create(ExtraStuck.modid("ammo/handgun"));
        public static final TagKey<Item> SHOW_VALUE = ItemTags.create(ExtraStuck.modid("show_value"));
        public static final TagKey<Item> IGNORE_BYPRODUCT_CUTTING = ItemTags
                .create(ExtraStuck.modid("interpreters_ignore_byproduct/cutting"));
        public static final TagKey<Item> DROPS_BOONDOLLARS = ItemTags.create(ExtraStuck.modid("drops_boondollars"));
        public static final TagKey<Item> COSMIC_PLAGUE_ARMOR = ItemTags.create(ExtraStuck.modid("armor/cosmic_plague"));
        public static final TagKey<Item> COSMIC_PLAGUE_CURIOS = ItemTags
                .create(ExtraStuck.modid("curios/cosmic_plague"));
        public static final TagKey<Item> VISION = ItemTags.create(ExtraStuck.modid("vision"));
        public static final TagKey<Item> ACTIVE_VISION = ItemTags.create(ExtraStuck.modid("vision/active"));
        public static final TagKey<Item> CANDY_WEAPONS = ItemTags.create(ExtraStuck.modid("candy_weapons"));
        public static final TagKey<Item> MAKE_IT_RAIN_FORBIDDEN = ItemTags
                .create(ExtraStuck.modid("make_it_rain_forbidden"));

        public static final TagKey<Item> TOOLS_ROLLING_PIN = ItemTags.create(common("tools/rolling_pin"));
        public static final TagKey<Item> URANIUM_RODS = ItemTags.create(common("rods/uranium"));
        public static final TagKey<Item> BRASS_NUGGETS = ItemTags.create(common("nuggets/brass"));
        public static final TagKey<Item> BEE_EGGS = ItemTags.create(common("eggs/bee"));
        public static final TagKey<Item> DRINKS = ItemTags.create(common("drinks"));
        public static final TagKey<Item> LEMON_FRUITS = ItemTags.create(common("foods/fruit/lemon"));
        public static final TagKey<Item> LEMON_CROPS = ItemTags.create(common("crops/lemon"));

        public static final TagKey<Item> ISS_SPELLBOOKS = ItemTags
                .create(ResourceLocation.fromNamespaceAndPath("curios", "spellbook"));
        public static final TagKey<Item> ISS_MITHRIL_ORES = ItemTags.create(common("ores/mithril"));
        public static final TagKey<Item> ISS_STAVES = ItemTags
                .create(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "staff"));
        public static final TagKey<Item> FD_KNIVES = ItemTags
                .create(ResourceLocation.fromNamespaceAndPath("farmersdelight", "tools/knives"));
    }

    public static final class Blocks {
        public static final TagKey<Block> PRYABLE = BlockTags.create(ExtraStuck.modid("pryable"));
        public static final TagKey<Block> MINEABLE_WITH_DICE = BlockTags.create(ExtraStuck.modid("mineable_with_dice"));

        public static final TagKey<Block> INCORRECT_FOR_COPPER_TIER = BlockTags
                .create(ExtraStuck.modid("incorrect_for_copper_tier"));
        public static final TagKey<Block> INCORRECT_FOR_AMETHYST_TIER = BlockTags
                .create(ExtraStuck.modid("incorrect_for_amethyst_tier"));
    }

    public static final class Fluids {
        public static final TagKey<Fluid> REACTOR_FLUIDS = FluidTags.create(ExtraStuck.modid("reactor_fluids"));
    }

    public static final class EntityTypes {
        public static final TagKey<EntityType<?>> BEENADE_ACCEPTS = create("beenade_accepts");
        public static final TagKey<EntityType<?>> COSMIC_PLAGUE_IMMUNE = create("cosmic_plague_immune");

        private static TagKey<EntityType<?>> create(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ExtraStuck.modid(name));
        }
    }

    public static final class MobEffects {
        public static final TagKey<MobEffect> COSMIC_PLAGUE_IMMUNITY = create("cosmic_plague/immunity");
        public static final TagKey<MobEffect> COSMIC_PLAGUE_PARTIAL_IMMUNITY = create("cosmic_plague/partial_immunity");
        public static final TagKey<MobEffect> RAINBOW_BOW_EFFECTS = create("rainbowbow_effects");

        private static TagKey<MobEffect> create(String name) {
            return TagKey.create(Registries.MOB_EFFECT, ExtraStuck.modid(name));
        }
    }

    public static final class DimensionTypes {
        public static final TagKey<DimensionType> COSMIC_DIMENSION_TYPES = create("cosmic_dimension_types");

        private static TagKey<DimensionType> create(String name) {
            return TagKey.create(Registries.DIMENSION_TYPE, ExtraStuck.modid(name));
        }
    }

    private static ResourceLocation common(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }
}
