package com.medsal15.compat.create.client.screens;

import com.medsal15.compat.create.client.menus.ESCreateMenuTypes;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public final class ESCreateScreens {
    public static void registerScreenFactories(final RegisterMenuScreensEvent event) {
        event.register(ESCreateMenuTypes.GRIST_FILTER.get(), GristFilterScreen::new);
    }
}
