package com.medsal15.client.screen;

import static com.mraof.minestuck.client.gui.MSScreenFactories.registerSylladexFactory;

import com.medsal15.ExtraStuck;
import com.medsal15.client.screen.machine.BlasterScreen;
import com.medsal15.client.screen.machine.ChargerScreen;
import com.medsal15.client.screen.machine.PrinterScreen;
import com.medsal15.client.screen.machine.ReactorScreen;
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
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = ExtraStuck.MODID)
public final class ESScreens {
    @SubscribeEvent
    public static void registerScreenFactories(RegisterMenuScreensEvent event) {
        event.register(ESMenuTypes.PRINTER.get(), PrinterScreen::new);
        event.register(ESMenuTypes.CHARGER.get(), ChargerScreen::new);
        event.register(ESMenuTypes.REACTOR.get(), ReactorScreen::new);
        event.register(ESMenuTypes.URANIUM_BLASTER.get(), BlasterScreen::new);

        event.register(ESMenuTypes.MASTERMIND_CARD.get(), MastermindCardScreen::new);
        if (ModList.get().isLoaded("create")) {
            // TODO register screen
        } else {
            // TODO register screen
        }

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
