package com.medsal15.compat.alchemyexpanded.items;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.alchemyexpanded.items.guns.ESGun;
import com.medsal15.entities.ESEntities;
import com.medsal15.entities.projectiles.bullets.ESBullet;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.projectiles.ESBulletItem;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.item.MSItemProperties;
import com.mraof.minestuck.item.MSItemTypes;
import com.mraof.minestuck.item.weapon.ItemRightClickEffect;
import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AEESMissingItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    public static final DeferredItem<Item> OFFICE_KEY = ITEMS.register("office_key",
            () -> new AltGunWeapon(
                    new WeaponItem.Builder(Tiers.IRON, 0, -1F).efficiency(1F).set(MSItemTypes.KEY_TOOL)
                            .set(ItemRightClickEffect.switchTo(AEESMissingItems.HANDGUN)),
                    new Item.Properties()));

    public static final DeferredItem<Item> HANDGUN = ITEMS.register("handgun",
            () -> new ESGun(
                    new ESGun.Builder().ammo(ESTags.Items.AMMO_HANDGUN).maxBullets(6).zoom(.8F)
                            .switchTo(AEESMissingItems.OFFICE_KEY),
                    new MSItemProperties().durability(250)));

    public static final DeferredItem<Item> HANDGUN_BULLET = ITEMS.registerItem("handgun_bullet",
            (p) -> new ESBulletItem(p.stacksTo(99).component(ESDataComponents.AMMO_DAMAGE, 2f),
                    ESBullet.createArrow(ESEntities.HANDGUN_BULLET.get()),
                    ESBullet.asProjectile(ESEntities.HANDGUN_BULLET.get())));
    public static final DeferredItem<Item> HEAVY_HANDGUN_BULLET = ITEMS.registerItem("heavy_handgun_bullet",
            (p) -> new ESBulletItem(p.stacksTo(99).component(ESDataComponents.AMMO_DAMAGE, 4f),
                    ESBullet.createArrow(ESEntities.HEAVY_HANDGUN_BULLET.get()),
                    ESBullet.asProjectile(ESEntities.HEAVY_HANDGUN_BULLET.get())));

    public static Collection<DeferredItem<Item>> getRangedWeapons() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(HANDGUN);
        return list;
    }

    public static Collection<DeferredItem<Item>> getKeys() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(OFFICE_KEY);
        return list;
    }

    public static Collection<DeferredItem<Item>> getAmmo() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(HANDGUN_BULLET);
        list.add(HEAVY_HANDGUN_BULLET);
        return list;
    }
}
