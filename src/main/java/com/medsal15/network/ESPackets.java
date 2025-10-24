package com.medsal15.network;

import java.util.ArrayList;
import java.util.List;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESDataComponents;
import com.medsal15.items.ESItems;
import com.medsal15.menus.ChargerMenu;
import com.mraof.minestuck.network.MSPacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ESPackets {
    public record ToggleMode(boolean charging) implements MSPacket.PlayToServer {
        public static final Type<ToggleMode> ID = new Type<>(ExtraStuck.modid("machines/toggle_mode"));
        public static final StreamCodec<ByteBuf, ToggleMode> STREAM_CODEC = ByteBufCodecs.BOOL.map(ToggleMode::new,
                ToggleMode::charging);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context, ServerPlayer player) {
            if (player.containerMenu instanceof ChargerMenu charger) {
                charger.setMode(charging ? 1 : 0);
            }
        }
    }

    /** Adds an attempt to a mastermind card */
    public record MastermindAddAttempt(int code) implements MSPacket.PlayToServer {
        public static final Type<MastermindAddAttempt> ID = new Type<>(ExtraStuck.modid("mastermind/add_attempt"));
        public static final StreamCodec<ByteBuf, MastermindAddAttempt> STREAM_CODEC = ByteBufCodecs.INT
                .map(MastermindAddAttempt::new, MastermindAddAttempt::code);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context, ServerPlayer player) {
            ItemStack card = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (card.getItem() != ESItems.MASTERMIND_CARD.get())
                return;
            List<Integer> attempts = new ArrayList<>();
            if (card.has(ESDataComponents.ATTEMPTS)) {
                attempts = new ArrayList<>(card.get(ESDataComponents.ATTEMPTS));
            }
            attempts.add(code);
            card.set(ESDataComponents.ATTEMPTS, attempts);
        }
    }

    /**
     * Destroys a mastermind card
     *
     * If drop is true, the card's contents will be given to the player.
     */
    public record MastermindDestroy(boolean drop) implements MSPacket.PlayToServer {
        public static final Type<MastermindDestroy> ID = new Type<>(ExtraStuck.modid("mastermind/destroy"));
        public static final StreamCodec<ByteBuf, MastermindDestroy> STREAM_CODEC = ByteBufCodecs.BOOL
                .map(MastermindDestroy::new, MastermindDestroy::drop);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context, ServerPlayer player) {
            ItemStack card = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (card.getItem() != ESItems.MASTERMIND_CARD.get())
                return;
            if (drop && card.has(DataComponents.CONTAINER)) {
                ItemContainerContents contents = card.get(DataComponents.CONTAINER);
                if (contents != null) {
                    int mult = card.getCount();
                    for (ItemStack item : contents.nonEmptyItemsCopy()) {
                        int count = item.getCount() * mult;
                        while (count > 0) {
                            int give = Math.min(count, item.getMaxStackSize());
                            count -= give;
                            ItemStack stack = item.copyWithCount(give);
                            if (!player.getInventory().add(stack)) {
                                player.drop(stack, false);
                            }
                        }
                    }
                }
            }
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
    }

    /** Resets a card's code, difficulty, and attempts */
    public record MastermindReset(int code, int difficulty) implements MSPacket.PlayToServer {
        public static final Type<MastermindReset> ID = new Type<>(ExtraStuck.modid("mastermind/replace"));
        public static final StreamCodec<ByteBuf, MastermindReset> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, MastermindReset::code, ByteBufCodecs.INT, MastermindReset::difficulty,
                MastermindReset::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context, ServerPlayer player) {
            ItemStack card = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (card.getItem() != ESItems.MASTERMIND_CARD.get())
                return;
            if (card.has(ESDataComponents.ATTEMPTS))
                card.remove(ESDataComponents.ATTEMPTS);
            card.set(ESDataComponents.MASTERMIND_CODE, code);
            card.set(ESDataComponents.DIFFICULTY, difficulty);
        }
    }
}
