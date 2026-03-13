package com.medsal15.compat.create.items;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.ExtraStuck;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESCreateItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    public static final DeferredItem<Item> GRIST_FILTER = ITEMS.registerItem("grist_filter", GristFilterItem::new);

    public static Collection<DeferredItem<Item>> getMiscTools() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(GRIST_FILTER);
        return list;
    }
}
