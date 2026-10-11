package com.medsal15.compat.alchemyexpanded.items;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;
import com.rosebushes.alchemyexpanded.item.AEItems;
import com.rosebushes.alchemyexpanded.item.weapon.gun.GunWeaponItem;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AEESItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    public static final DeferredItem<Item> HANDGUN = ITEMS.register("handgun",
            () -> new GunWeaponItem(new Item.Properties(), 2, 2, 1, .5f, 3, 6, 1, 60, false, true, AEItems.AMMO.get(),
                    ESItems.OFFICE_KEY.get()));

    public static Collection<DeferredItem<Item>> getGuns() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(HANDGUN);
        return list;
    }
}
