package com.medsal15.items.components;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.medsal15.data.ESLangProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record SteamFuelComponent(int fuel, boolean burning) implements TooltipProvider {
    public static final Codec<SteamFuelComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.optionalFieldOf("fuel", 0).forGetter(SteamFuelComponent::fuel),
            Codec.BOOL.optionalFieldOf("burning", false).forGetter(SteamFuelComponent::burning))
            .apply(inst, SteamFuelComponent::new));
    public static final StreamCodec<ByteBuf, SteamFuelComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SteamFuelComponent::fuel,
            ByteBufCodecs.BOOL, SteamFuelComponent::burning,
            SteamFuelComponent::new);

    public SteamFuelComponent drain(int fuel) {
        return new SteamFuelComponent(this.fuel - fuel, burning);
    }

    /** Sets whether the item is consuming fuel */
    public SteamFuelComponent toggle(boolean burning) {
        return new SteamFuelComponent(fuel, burning);
    }

    public SteamFuelComponent toggle() {
        return toggle(!burning);
    }

    public SteamFuelComponent extinguish() {
        return toggle(false);
    }

    public SteamFuelComponent refuel(int fuel) {
        return new SteamFuelComponent(this.fuel + fuel, burning);
    }

    @Override
    public void addToTooltip(@Nonnull TooltipContext context, @Nonnull Consumer<Component> tooltipAdder,
            @Nonnull TooltipFlag tooltipFlag) {
        float burnTime = (float) fuel / 20;

        MutableComponent state;
        if (burning) {
            state = Component.translatable(ESLangProvider.STEAM_WEAPON_LIT).withColor(0xFF7700);
        } else {
            state = Component.translatable(ESLangProvider.STEAM_WEAPON_UNLIT).withStyle(ChatFormatting.DARK_GRAY);
        }
        tooltipAdder.accept(Component.translatable(ESLangProvider.STEAM_WEAPON_FUEL, burnTime, state));
    }
}
