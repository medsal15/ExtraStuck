package com.medsal15.subevents;

import com.medsal15.ExtraStuck;
import com.medsal15.datamaps.ReactorFuel;
import com.medsal15.network.ESPackets.MastermindAddAttempt;
import com.medsal15.network.ESPackets.MastermindDestroy;
import com.medsal15.network.ESPackets.MastermindReset;
import com.medsal15.network.ESPackets.ToggleMode;
import com.mraof.minestuck.network.MSPacket;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = ExtraStuck.MODID)
public final class ModEvents {
    @SubscribeEvent
    public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ExtraStuck.MODID);

        registrar.playToServer(ToggleMode.ID, ToggleMode.STREAM_CODEC, ModEvents::exec);
        registrar.playToServer(MastermindAddAttempt.ID, MastermindAddAttempt.STREAM_CODEC, ModEvents::exec);
        registrar.playToServer(MastermindDestroy.ID, MastermindDestroy.STREAM_CODEC, ModEvents::exec);
        registrar.playToServer(MastermindReset.ID, MastermindReset.STREAM_CODEC, ModEvents::exec);
    }

    private static void exec(MSPacket.PlayToServer packet, IPayloadContext context) {
        packet.execute(context, (ServerPlayer) context.player());
    }

    @SubscribeEvent
    public static void registerDataMapTypes(final RegisterDataMapTypesEvent event) {
        event.register(ReactorFuel.REACTOR_MAP);
    }
}
