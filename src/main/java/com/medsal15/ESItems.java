package com.medsal15;

import com.medsal15.items.ESShield;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    public static final DeferredItem<Item> WOODEN_SHIELD = ITEMS.registerItem("wooden_shield", ESShield::new,
            new Item.Properties().durability(80));
}
