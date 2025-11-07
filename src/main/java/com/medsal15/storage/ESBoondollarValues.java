package com.medsal15.storage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.medsal15.ExtraStuck;
import com.medsal15.network.ESPackets.SyncBoondollarValues;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.entity.consort.BoondollarPriceRecipe;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public final class ESBoondollarValues {
    private static ESBoondollarValues INSTANCE;

    private final List<BoondollarValue> values;

    private ESBoondollarValues(List<BoondollarValue> values) {
        this.values = values;
    }

    public Optional<BoondollarValue> getValue(ItemStack stack) {
        return values.stream().filter(value -> value.appliesTo(stack)).findAny();
    }

    public static ESBoondollarValues getInstance() {
        return Objects.requireNonNull(INSTANCE);
    }

    public static ESBoondollarValues fromList(List<BoondollarValue> values) {
        ExtraStuck.LOGGER.info("synced {} boondollar values", values.size());
        INSTANCE = new ESBoondollarValues(values);
        return INSTANCE;
    }

    @SubscribeEvent
    private static void onResourceReload(final AddReloadListenerEvent event) {
        event.addListener(new Loader());
    }

    @SubscribeEvent
    private static void onServerStopped(final ServerStoppedEvent event) {
        INSTANCE = null;
    }

    @SubscribeEvent
    private static void onDatapackSync(final OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            PacketDistributor.sendToPlayer(event.getPlayer(), new SyncBoondollarValues(INSTANCE.values),
                    new CustomPacketPayload[0]);
        } else {
            PacketDistributor.sendToAllPlayers(new SyncBoondollarValues(INSTANCE.values), new CustomPacketPayload[0]);
        }
    }

    public record BoondollarValue(Ingredient ingredient, int min, int max) {
        public static final StreamCodec<RegistryFriendlyByteBuf, BoondollarValue> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, BoondollarValue::ingredient,
                ByteBufCodecs.INT, BoondollarValue::min,
                ByteBufCodecs.INT, BoondollarValue::max,
                BoondollarValue::new);

        public static BoondollarValue fromPrice(BoondollarPriceRecipe price) {
            return new BoondollarValue(price.ingredient(), price.priceRange().getMinValue(),
                    price.priceRange().getMaxValue());
        }

        public boolean appliesTo(ItemStack stack) {
            return ingredient.test(stack);
        }
    }

    private static final class Loader extends SimpleJsonResourceReloadListener {
        Loader() {
            super(new GsonBuilder().create(), "minestuck/boondollar_prices");
        }

        @Override
        protected void apply(@Nonnull Map<ResourceLocation, JsonElement> jsonEntries,
                @Nonnull ResourceManager resourceManager, @Nonnull ProfilerFiller profiler) {
            ImmutableList.Builder<BoondollarValue> values = ImmutableList.builder();
            for (Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet()) {
                BoondollarPriceRecipe.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
                        .resultOrPartial()
                        .ifPresent(price -> {
                            values.add(BoondollarValue.fromPrice(price));
                        });
            }

            INSTANCE = new ESBoondollarValues(values.build());
            ExtraStuck.LOGGER.info("found {} boondollar values", INSTANCE.values.size());
        }
    }
}
