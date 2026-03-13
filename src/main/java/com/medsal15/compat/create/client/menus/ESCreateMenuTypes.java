package com.medsal15.compat.create.client.menus;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESCreateMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU,
            ExtraStuck.MODID);

    public static final Supplier<MenuType<GristFilterMenu>> GRIST_FILTER = MENU_TYPES.register("grist_filter",
            () -> new MenuType<>((IContainerFactory<GristFilterMenu>) GristFilterMenu::new,
                    FeatureFlags.DEFAULT_FLAGS));
    // public static final Supplier<MenuType<GristFilterMenu>> GRIST_FILTER =
    // MENU_TYPES.register("grist_filter",
    // () -> new MenuType<>((MenuType.MenuSupplier<GristFilterMenu>)
    // GristFilterMenu::new,
    // FeatureFlags.DEFAULT_FLAGS));
}
