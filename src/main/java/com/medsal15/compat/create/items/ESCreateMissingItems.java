package com.medsal15.compat.create.items;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.MissingModItem;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESCreateMissingItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    static {
        ITEMS.register("grist_filter",
                () -> new MissingModItem(new Item.Properties().stacksTo(1), "Create", "create"));
    }
}
