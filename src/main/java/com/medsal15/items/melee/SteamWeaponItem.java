package com.medsal15.items.melee;

import javax.annotation.Nonnull;

import com.medsal15.config.ConfigCommon;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.components.SteamFuelComponent;
import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public class SteamWeaponItem extends WeaponItem {
    public SteamWeaponItem(WeaponItem.Builder builder, Properties properties) {
        super(builder, properties);
    }

    @Override
    public boolean overrideOtherStackedOnMe(@Nonnull ItemStack stack, @Nonnull ItemStack other, @Nonnull Slot slot,
            @Nonnull ClickAction action, @Nonnull Player player, @Nonnull SlotAccess access) {
        if (stack.getCount() != 1)
            return false;

        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            Holder<Item> item = other.getItemHolder();
            FurnaceFuel furnaceFuel = item.getData(NeoForgeDataMaps.FURNACE_FUELS);
            if (furnaceFuel == null)
                return false;

            SteamFuelComponent steamFuel = stack.getOrDefault(ESDataComponents.STEAM_FUEL,
                    new SteamFuelComponent(0, false));
            if (steamFuel.fuel() > ConfigCommon.STEAM_FUEL_THRESHOLD.get())
                return false;

            stack.set(ESDataComponents.STEAM_FUEL, steamFuel.refuel(furnaceFuel.burnTime()));
            other.shrink(1);
            return true;
        }

        return false;
    }
}
