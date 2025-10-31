package com.medsal15.items;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import com.medsal15.ExtraStuck;
import com.medsal15.items.components.GristFilterEntry;
import com.mojang.serialization.Codec;
import com.mraof.minestuck.api.alchemy.GristType;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

//TODO move to component sub package
public final class ESDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister
            .createDataComponents(Registries.DATA_COMPONENT_TYPE, ExtraStuck.MODID);

    // Generic
    public static final Supplier<DataComponentType<Integer>> ENERGY = DATA_COMPONENTS
            .registerComponentType(
                    "energy",
                    builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final Supplier<DataComponentType<Integer>> ENERGY_STORAGE = DATA_COMPONENTS
            .registerComponentType(
                    "energy_storage",
                    builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    /** Table to drop */
    public static final Supplier<DataComponentType<ResourceKey<LootTable>>> GIFT_TABLE = DATA_COMPONENTS
            .registerComponentType("gift_table", builder -> builder.persistent(ResourceKey.codec(Registries.LOOT_TABLE))
                    .networkSynchronized(ResourceKey.streamCodec(Registries.LOOT_TABLE)));
    /** Difficulty of the item */
    public static final Supplier<DataComponentType<Integer>> DIFFICULTY = DATA_COMPONENTS.registerComponentType(
            "difficulty", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    // Grist detector
    public static final Supplier<DataComponentType<GristLayer>> GRIST_LAYER = DATA_COMPONENTS
            .registerComponentType("grist_layer", builder -> builder
                    .persistent(StringRepresentable.fromEnum(GristLayer::values))
                    .networkSynchronized(NeoForgeStreamCodecs.enumCodec(GristLayer.class)));
    public static final Supplier<DataComponentType<GristType>> GRIST_FOUND = DATA_COMPONENTS
            .registerComponentType("grist_type",
                    builder -> builder.persistent(GristType.CODEC).networkSynchronized(GristType.STREAM_CODEC));

    public static enum GristLayer implements StringRepresentable {
        COMMON,
        UNCOMMON,
        ANY;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public static GristLayer fromName(String string) {
            for (GristLayer layer : GristLayer.values()) {
                if (layer.name().toLowerCase().equals(string))
                    return layer;
            }
            throw new IllegalArgumentException("Invalid grist layer " + string);
        }

        public GristLayer next() {
            List<GristLayer> values = List.of(GristLayer.values());
            int i = (values.indexOf(this) + 1) % values.size();
            return values.get(i);
        }
    };

    // Shields
    /** Flame shield burn duration in ticks */
    public static final Supplier<DataComponentType<Integer>> BURN_DURATION = DATA_COMPONENTS
            .registerComponentType(
                    "burn_duration", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    /** Flux shield RF cost per damage */
    public static final Supplier<DataComponentType<Integer>> FLUX_MULTIPLIER = DATA_COMPONENTS
            .registerComponentType(
                    "flux_multiplier", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    /** Thorn shield damage */
    public static final Supplier<DataComponentType<Float>> SHIELD_DAMAGE = DATA_COMPONENTS
            .registerComponentType(
                    "shield_damage",
                    builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));

    // Weapons
    public static final Supplier<DataComponentType<Float>> AMMO_DAMAGE = DATA_COMPONENTS.registerComponentType(
            "ammo_damage", builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));
    /** Shots left */
    public static final Supplier<DataComponentType<Integer>> URANIUM_CHARGE = DATA_COMPONENTS.registerComponentType(
            "uranium_charge", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final Supplier<DataComponentType<String>> ENTITY_TYPE = DATA_COMPONENTS.registerComponentType(
            "entity_type",
            builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));

    // Moduses
    /** Mastermind card code */
    public static final Supplier<DataComponentType<Integer>> MASTERMIND_CODE = DATA_COMPONENTS.registerComponentType(
            "mastermind_code", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final Supplier<DataComponentType<List<Integer>>> ATTEMPTS = DATA_COMPONENTS
            .registerComponentType("attempts", builder -> builder.persistent(Codec.INT.listOf())
                    .networkSynchronized(ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list())));

    // Food
    public static final Supplier<DataComponentType<MoonCakeSliceColor>> MOON_CAKE_SLICE_COLOR = DATA_COMPONENTS
            .registerComponentType("moon_cake_slice_color", builder -> builder
                    .persistent(StringRepresentable.fromEnum(MoonCakeSliceColor::values))
                    .networkSynchronized(NeoForgeStreamCodecs.enumCodec(MoonCakeSliceColor.class)));

    public static enum MoonCakeSliceColor implements StringRepresentable {
        DUAL,
        DERSE,
        PROSPIT;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public static MoonCakeSliceColor fromName(String string) throws IllegalArgumentException {
            for (MoonCakeSliceColor layer : MoonCakeSliceColor.values()) {
                if (layer.name().toLowerCase().equals(string))
                    return layer;
            }
            throw new IllegalArgumentException("Invalid moon cake color " + string);
        }
    }

    public static final Supplier<DataComponentType<List<GristFilterEntry>>> GRIST_FILTER_DATA = DATA_COMPONENTS
            .registerComponentType("grist_filter", builder -> builder.persistent(GristFilterEntry.LIST_CODEC)
                    .networkSynchronized(GristFilterEntry.LIST_STREAM_CODEC));
}
