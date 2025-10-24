package com.medsal15.client.screen;

import com.medsal15.ExtraStuck;
import com.medsal15.client.screen.modus.ArcheologyModusScreen;
import com.medsal15.client.screen.modus.EnderModusScreen;
import com.medsal15.client.screen.modus.FortuneModusScreen;
import com.medsal15.client.screen.modus.MastermindModusScreen;
import com.medsal15.client.screen.modus.OreModusScreen;
import com.medsal15.client.screen.modus.PileModusScreen;
import com.medsal15.client.screen.modus.VoidModusScreen;
import com.medsal15.menus.ESMenuTypes;
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
        event.register(ESMenuTypes.PRINTER.get(), PrinterScreen::new);
        event.register(ESMenuTypes.CHARGER.get(), ChargerScreen::new);
        event.register(ESMenuTypes.REACTOR.get(), ReactorScreen::new);
        event.register(ESMenuTypes.MASTERMIND_CARD.get(), MastermindCardScreen::new);

        // Moduses
        registerSylladexFactory(ESModus.PILE_MODUS, PileModusScreen::new);
        registerSylladexFactory(ESModus.FORTUNE_MODUS, FortuneModusScreen::new);
        registerSylladexFactory(ESModus.ORE_MODUS, OreModusScreen::new);
        registerSylladexFactory(ESModus.ARCHEOLOGY_MODUS, ArcheologyModusScreen::new);
        registerSylladexFactory(ESModus.VOID_MODUS, VoidModusScreen::new);
        registerSylladexFactory(ESModus.ENDER_MODUS, EnderModusScreen::new);
        registerSylladexFactory(ESModus.MASTERMIND_MODUS, MastermindModusScreen::new);
    }
}
