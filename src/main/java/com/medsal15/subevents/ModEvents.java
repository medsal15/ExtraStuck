package com.medsal15.subevents;

import com.medsal15.ExtraStuck;
import com.medsal15.datamaps.ReactorFuel;
import com.medsal15.network.ESPackets.ToggleMode;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = ExtraStuck.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEvents {
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ExtraStuck.MODID);

        registrar.playToServer(ToggleMode.ID, ToggleMode.STREAM_CODEC,
                (t, context) -> t.execute(context, (ServerPlayer) context.player()));
    }

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(ReactorFuel.REACTOR_MAP);
    }
}
