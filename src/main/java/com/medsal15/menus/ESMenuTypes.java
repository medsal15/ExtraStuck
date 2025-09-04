package com.medsal15.menus;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU,
            ExtraStuck.MODID);

    public static final Supplier<MenuType<PrinterMenu>> PRINTER = MENU_TYPES.register("printer",
            () -> new MenuType<>((IContainerFactory<PrinterMenu>) PrinterMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
