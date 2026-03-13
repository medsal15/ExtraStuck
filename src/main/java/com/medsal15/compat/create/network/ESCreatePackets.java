package com.medsal15.compat.create.network;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.create.client.menus.GristFilterMenu;
import com.medsal15.compat.create.items.GristFilterItem.GristFilterEntry.ComparingMode;
import com.medsal15.subevents.CommonEvents;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.network.MSPacket;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ESCreatePackets {
    public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ExtraStuck.MODID);

        registrar.playToServer(ClearGristFilter.ID, ClearGristFilter.STREAM_CODEC, CommonEvents::exec);
        registrar.playToServer(AddGristFilterEntry.ID, AddGristFilterEntry.STREAM_CODEC, CommonEvents::exec);
    }

    public enum ClearGristFilter implements MSPacket.PlayToServer {
        INSTANCE;

        public static final Type<ClearGristFilter> ID = new Type<>(ExtraStuck.modid("grist_filter/clear"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ClearGristFilter> STREAM_CODEC = StreamCodec
                .unit(INSTANCE);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context, ServerPlayer player) {
            if (player == null)
                return;
            if (player.containerMenu instanceof GristFilterMenu menu)
                menu.clearContents();
        }
    }

    public record AddGristFilterEntry(GristType grist, int amount, ComparingMode mode)
            implements MSPacket.PlayToServer {
        public static final Type<AddGristFilterEntry> ID = new Type<>(ExtraStuck.modid("grist_filter/add"));
        public static final StreamCodec<RegistryFriendlyByteBuf, AddGristFilterEntry> STREAM_CODEC = StreamCodec
                .composite(
                        GristType.STREAM_CODEC, AddGristFilterEntry::grist,
                        ByteBufCodecs.INT, AddGristFilterEntry::amount,
                        NeoForgeStreamCodecs.enumCodec(ComparingMode.class), AddGristFilterEntry::mode,
                        AddGristFilterEntry::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }

        @Override
        public void execute(IPayloadContext context, ServerPlayer player) {
            if (player == null)
                return;
            if (player.containerMenu instanceof GristFilterMenu menu)
                menu.appendEntry(grist, amount, mode);
        }
    }
}
