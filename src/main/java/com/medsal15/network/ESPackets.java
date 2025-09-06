package com.medsal15.network;

import com.medsal15.ExtraStuck;
import com.medsal15.menus.ChargerMenu;
import com.mraof.minestuck.network.MSPacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
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
}
