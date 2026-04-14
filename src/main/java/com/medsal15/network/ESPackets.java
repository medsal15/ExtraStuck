package com.medsal15.network;

import java.util.ArrayList;
import java.util.List;

import com.medsal15.ESAttachements;
import com.medsal15.ESAttachements.ESGristLayerInfo;
import com.medsal15.ExtraStuck;
import com.medsal15.config.ConfigServer;
import com.medsal15.items.ESItems;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.menus.ChargerMenu;
import com.medsal15.menus.CraftingModusRecipeMenu;
import com.medsal15.storage.ESBoondollarValues;
import com.medsal15.storage.ESBoondollarValues.BoondollarValue;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.network.MSPacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
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
            ItemStack card = player.getMainHandItem();
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
            ItemStack card = player.getMainHandItem();
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
                ByteBufCodecs.INT, MastermindReset::code,
                ByteBufCodecs.INT, MastermindReset::difficulty,
                MastermindReset::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context, ServerPlayer player) {
            ItemStack card = player.getMainHandItem();
            if (card.getItem() != ESItems.MASTERMIND_CARD.get())
                return;
            if (card.has(ESDataComponents.ATTEMPTS))
                card.remove(ESDataComponents.ATTEMPTS);
            card.set(ESDataComponents.MASTERMIND_CODE, code);
            card.set(ESDataComponents.DIFFICULTY, difficulty);
        }
    }

    public record MastermindDifficulty(int difficulty) implements MSPacket.PlayToServer {
        public static final Type<MastermindDifficulty> ID = new Type<>(ExtraStuck.modid("mastermind/difficulty"));
        public static final StreamCodec<ByteBuf, MastermindDifficulty> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, MastermindDifficulty::difficulty,
                MastermindDifficulty::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context, ServerPlayer player) {
            ItemStack modus = player.getMainHandItem();
            if (modus.getItem() == ESItems.MASTERMIND_MODUS_CARD.get()
                    && ConfigServer.MASTERMIND_CHANGE_PC.getAsBoolean()) {
                modus.set(ESDataComponents.DIFFICULTY, difficulty);
            }
        }
    }

    public enum CraftingModusRecipeMenuOpen implements MSPacket.PlayToServer {
        INSTANCE;

        public static final Type<CraftingModusRecipeMenuOpen> ID = new Type<>(
                ExtraStuck.modid("crafting_modus/open_recipe"));
        public static final StreamCodec<RegistryFriendlyByteBuf, CraftingModusRecipeMenuOpen> STREAM_CODEC = StreamCodec
                .unit(INSTANCE);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context, ServerPlayer player) {
            player.openMenu(new SimpleMenuProvider(
                    (windowId, playInventory, play) -> new CraftingModusRecipeMenu(windowId, playInventory),
                    Component.literal("")));
        }
    }

    /** Switches to the next available recipe in the craftin menu */
    public enum CraftingModusRecipeMenuNext implements MSPacket.PlayToServer {
        INSTANCE;

        public static final Type<CraftingModusRecipeMenuNext> ID = new Type<>(
                ExtraStuck.modid("crafting_modus/next_recipe"));
        public static final StreamCodec<RegistryFriendlyByteBuf, CraftingModusRecipeMenuNext> STREAM_CODEC = StreamCodec
                .unit(INSTANCE);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context, ServerPlayer player) {
            if (player.containerMenu instanceof CraftingModusRecipeMenu menu) {
                menu.nextRecipe();
            }
        }
    }

    /** Tells the menu to save the crafting modus data */
    public enum CraftingModusRecipeMenuSave implements MSPacket.PlayToServer {
        INSTANCE;

        public static final Type<CraftingModusRecipeMenuSave> ID = new Type<>(
                ExtraStuck.modid("crafting_modus/save_recipe"));
        public static final StreamCodec<RegistryFriendlyByteBuf, CraftingModusRecipeMenuSave> STREAM_CODEC = StreamCodec
                .unit(INSTANCE);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context, ServerPlayer player) {
            if (player.containerMenu instanceof CraftingModusRecipeMenu menu) {
                menu.saveRecipe(player);
            }
        }
    }

    /** Syncs data from the server crafting menu to the player's copy */
    public record CraftingModusRecipeMenuSync(int index, int size, ItemStack output) implements MSPacket.PlayToClient {
        public static final Type<CraftingModusRecipeMenuSync> ID = new Type<>(
                ExtraStuck.modid("crafting_modus/sync_menu"));
        public static final StreamCodec<RegistryFriendlyByteBuf, CraftingModusRecipeMenuSync> STREAM_CODEC = StreamCodec
                .composite(
                        ByteBufCodecs.INT, CraftingModusRecipeMenuSync::index,
                        ByteBufCodecs.INT, CraftingModusRecipeMenuSync::size,
                        ItemStack.OPTIONAL_STREAM_CODEC, CraftingModusRecipeMenuSync::output,
                        CraftingModusRecipeMenuSync::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @SuppressWarnings("null")
        @Override
        public void execute(IPayloadContext context) {
            if (Minecraft.getInstance().player.containerMenu instanceof CraftingModusRecipeMenu menu) {
                menu.setRecipeCount(size);
                menu.setRecipeIndex(index);
                menu.setOutput(output);
            }
        }
    }

    /** Exits the crafting menu and returns to the sylladex screen */
    public enum CraftingModusRecipeMenuQuit implements MSPacket.PlayToClient {
        INSTANCE;

        public static final Type<CraftingModusRecipeMenuQuit> ID = new Type<>(
                ExtraStuck.modid("crafting_modus/quit"));
        public static final StreamCodec<RegistryFriendlyByteBuf, CraftingModusRecipeMenuQuit> STREAM_CODEC = StreamCodec
                .unit(INSTANCE);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context) {
            PlayerStatsScreen.openGui(true);
        }
    }

    public record SyncBoondollarValues(List<BoondollarValue> values) implements MSPacket.PlayToClient {
        public static final Type<SyncBoondollarValues> ID = new Type<>(ExtraStuck.modid("sync_boondollar_values"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncBoondollarValues> STREAM_CODEC = StreamCodec
                .composite(
                        BoondollarValue.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncBoondollarValues::values,
                        SyncBoondollarValues::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context) {
            ESBoondollarValues.fromList(values);
        }
    }

    public enum GristLayerInfoDelete implements MSPacket.PlayToClient {
        INSTANCE;

        public static final Type<GristLayerInfoDelete> ID = new Type<>(ExtraStuck.modid("gristlayerinfo/delete"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GristLayerInfoDelete> STREAM_CODEC = StreamCodec
                .unit(INSTANCE);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context) {
            context.player().removeData(ESAttachements.GRIST_LAYER);
        }
    }

    public record GristLayerInfoData(GristType any, GristType common, GristType uncommon)
            implements MSPacket.PlayToClient {
        public static final Type<GristLayerInfoData> ID = new Type<>(ExtraStuck.modid("gristlayerinfo/data"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GristLayerInfoData> STREAM_CODEC = StreamCodec
                .composite(
                        GristType.STREAM_CODEC, GristLayerInfoData::any,
                        GristType.STREAM_CODEC, GristLayerInfoData::common,
                        GristType.STREAM_CODEC, GristLayerInfoData::uncommon,
                        GristLayerInfoData::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context) {
            context.player().setData(ESAttachements.GRIST_LAYER, new ESGristLayerInfo(any, common, uncommon));
        }
    }
}
