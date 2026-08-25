package com.medsal15.compat.alchemyexpanded.items;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.ExtraStuck;
import com.medsal15.entities.ESEntities;
import com.medsal15.entities.projectiles.bullets.ESBullet;
import com.medsal15.items.projectiles.ESBulletItem;
import com.mraof.minestuck.item.MSItemTypes;
import com.mraof.minestuck.item.weapon.ItemRightClickEffect;
import com.mraof.minestuck.item.weapon.WeaponItem;
import com.rosebushes.alchemyexpanded.item.AEItems;
import com.rosebushes.alchemyexpanded.item.weapon.gun.GunWeaponItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AEESItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    public static final DeferredItem<Item> OFFICE_KEY = ITEMS.register("office_key",
            () -> new AltAEGunWeapon(
                    new WeaponItem.Builder(Tiers.IRON, 0, -1F).efficiency(1F)
                            .set(MSItemTypes.KEY_TOOL)
                            .set(ItemRightClickEffect.switchTo(AEESItems.HANDGUN)),
                    new Item.Properties()));

    public static final DeferredItem<Item> HANDGUN = ITEMS.register("handgun",
            () -> new GunWeaponItem(new Item.Properties(), 2, 2, 1, .5f, 3, 6, 1, 60, false, true, AEItems.AMMO.get(),
                    OFFICE_KEY.get()));

    // Unused, kept in case the mod is removed (why would you do that)
    public static final DeferredItem<Item> HANDGUN_BULLET = ITEMS.registerItem("handgun_bullet",
            (p) -> new ESBulletItem(p.stacksTo(99),
                    ESBullet.createArrow(ESEntities.HANDGUN_BULLET.get()),
                    ESBullet.asProjectile(ESEntities.HANDGUN_BULLET.get())));
    public static final DeferredItem<Item> HEAVY_HANDGUN_BULLET = ITEMS.registerItem("heavy_handgun_bullet",
            (p) -> new ESBulletItem(p.stacksTo(99),
                    ESBullet.createArrow(ESEntities.HEAVY_HANDGUN_BULLET.get()),
                    ESBullet.asProjectile(ESEntities.HEAVY_HANDGUN_BULLET.get())));

    public static Collection<DeferredItem<Item>> getRangedWeapons() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(HANDGUN);
        return list;
    }

    public static Collection<DeferredItem<Item>> getGuns() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(HANDGUN);
        return list;
    }

    public static Collection<DeferredItem<Item>> getKeys() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(OFFICE_KEY);
        return list;
    }
}
