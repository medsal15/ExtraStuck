package com.medsal15.items.components;

import java.util.List;
import java.util.Locale;

import com.medsal15.ExtraStuck;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.api.alchemy.GristType;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record GristFilterEntry(GristType grist, Integer amount, ComparingMode mode) {
    public static final Codec<GristFilterEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            GristType.CODEC.fieldOf("grist").forGetter(GristFilterEntry::grist),
            Codec.INT.optionalFieldOf("amount", 0).forGetter(GristFilterEntry::amount),
            StringRepresentable.fromEnum(ComparingMode::values).fieldOf("mode").forGetter(GristFilterEntry::mode))
            .apply(inst, GristFilterEntry::new));
    public static final Codec<List<GristFilterEntry>> LIST_CODEC = CODEC.listOf();
    public static final StreamCodec<RegistryFriendlyByteBuf, GristFilterEntry> STREAM_CODEC = StreamCodec.composite(
            GristType.STREAM_CODEC, GristFilterEntry::grist,
            ByteBufCodecs.INT, GristFilterEntry::amount,
            NeoForgeStreamCodecs.enumCodec(ComparingMode.class), GristFilterEntry::mode,
            GristFilterEntry::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, List<GristFilterEntry>> LIST_STREAM_CODEC = STREAM_CODEC
            .apply(ByteBufCodecs.list());

    public boolean test(int amount) {
        return mode.test(amount, this.amount);
    }

    public static enum ComparingMode implements StringRepresentable {
        GREATER_THAN_EQUAL,
        GREATER_THAN,
        EQUAL,
        NOT_EQUAL,
        LESS_THAN_EQUAL,
        LESS_THAN;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public static ComparingMode fromName(String string) throws IllegalArgumentException {
            for (ComparingMode mode : ComparingMode.values()) {
                if (mode.name().toLowerCase().equals(string))
                    return mode;
            }
            throw new IllegalArgumentException("Invalid comparing mode " + string);
        }

        public String key() {
            return ExtraStuck.MODID + ".grist_filter." + name().toLowerCase();
        }

        public String symbol() {
            return ExtraStuck.MODID + ".grist_filter." + name().toLowerCase() + ".symbol";
        }

        public boolean test(int left, int right) {
            return switch (this) {
                case GREATER_THAN_EQUAL -> left >= right;
                case GREATER_THAN -> left > right;
                case EQUAL -> left == right;
                case NOT_EQUAL -> left != right;
                case LESS_THAN_EQUAL -> left <= right;
                case LESS_THAN -> left < right;
            };
        }
    }

}
