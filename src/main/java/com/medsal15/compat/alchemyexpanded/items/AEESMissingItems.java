package com.medsal15.compat.alchemyexpanded.items;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.alchemyexpanded.items.guns.ESGun;
import com.medsal15.items.ESItems;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.item.MSItemProperties;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AEESMissingItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    public static final DeferredItem<Item> HANDGUN = ITEMS.register("handgun",
            () -> new ESGun(
                    new ESGun.Builder().ammo(ESTags.Items.AMMO_HANDGUN).maxBullets(6).zoom(.8F)
                            .switchTo(ESItems.OFFICE_KEY),
                    new MSItemProperties().durability(250)));

    public static Collection<DeferredItem<Item>> getGuns() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(HANDGUN);
        return list;
    }
}
