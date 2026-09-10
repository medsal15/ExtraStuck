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
            () -> new MenuType<>((IContainerFactory<PrinterMenu>) PrinterMenu::new,
                    FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<ChargerMenu>> CHARGER = MENU_TYPES.register("charger",
            () -> new MenuType<>((IContainerFactory<ChargerMenu>) ChargerMenu::new,
                    FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<WirelessChargerMenu>> WIRELESS_CHARGER = MENU_TYPES.register(
            "wireless_charger", () -> new MenuType<>((IContainerFactory<WirelessChargerMenu>) WirelessChargerMenu::new,
                    FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<ReactorMenu>> REACTOR = MENU_TYPES.register("reactor",
            () -> new MenuType<>((IContainerFactory<ReactorMenu>) ReactorMenu::new,
                    FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<BlasterMenu>> URANIUM_BLASTER = MENU_TYPES.register("uranium_blaster",
            () -> new MenuType<>((IContainerFactory<BlasterMenu>) BlasterMenu::new,
                    FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<StorageBlockMenu.Dowel>> DOWEL_STORAGE = MENU_TYPES.register("dowel_storage",
            () -> new MenuType<>((IContainerFactory<StorageBlockMenu.Dowel>) StorageBlockMenu.Dowel::new,
                    FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<StorageBlockMenu.Card>> CARD_STORAGE = MENU_TYPES.register("card_storage",
            () -> new MenuType<>((IContainerFactory<StorageBlockMenu.Card>) StorageBlockMenu.Card::new,
                    FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<VendingMachineMenu.Sell>> VENDING_MACHINE_SELL = MENU_TYPES.register(
            "vending_machine_sell",
            () -> new MenuType<>((IContainerFactory<VendingMachineMenu.Sell>) VendingMachineMenu.Sell::new,
                    FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<VendingMachineMenu.Storage>> VENDING_MACHINE_STORAGE = MENU_TYPES.register(
            "vending_machine_storage",
            () -> new MenuType<>((IContainerFactory<VendingMachineMenu.Storage>) VendingMachineMenu.Storage::new,
                    FeatureFlags.DEFAULT_FLAGS));

    public static final Supplier<MenuType<MastermindCardMenu>> MASTERMIND_CARD = MENU_TYPES.register(
            "mastermind_card",
            () -> new MenuType<MastermindCardMenu>(MastermindCardMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<CraftingModusRecipeMenu>> CRAFTING_MODUS_RECIPE = MENU_TYPES.register(
            "crafting_modus_recipe",
            () -> new MenuType<CraftingModusRecipeMenu>(CraftingModusRecipeMenu::new,
                    FeatureFlags.DEFAULT_FLAGS));
}
