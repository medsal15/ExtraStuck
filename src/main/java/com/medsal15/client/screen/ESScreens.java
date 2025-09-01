package com.medsal15.client.screen;

import com.medsal15.ExtraStuck;
import com.medsal15.client.screen.modus.FortuneModusScreen;
import com.medsal15.client.screen.modus.PileModusScreen;
import com.medsal15.modus.ESModus;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static com.mraof.minestuck.client.gui.MSScreenFactories.registerSylladexFactory;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = ExtraStuck.MODID)
public final class ESScreens {
    @SubscribeEvent
    public static void registerScreenFactories(RegisterMenuScreensEvent event) {
        registerSylladexFactory(ESModus.PILE_MODUS, PileModusScreen::new);
        registerSylladexFactory(ESModus.FORTUNE_MODUS, FortuneModusScreen::new);
    }
}
