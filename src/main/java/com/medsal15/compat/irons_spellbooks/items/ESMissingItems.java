package com.medsal15.compat.irons_spellbooks.items;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.MissingModItem;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESMissingItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    // These are not referenced anywhere
    static {
        ITEMS.register("grimoire",
                () -> new MissingModItem(new Item.Properties().stacksTo(1), "Iron's Spells & Spellbooks",
                        "irons_spellbooks"));
        ITEMS.register("gemini_spellbook_red",
                () -> new MissingModItem(new Item.Properties().stacksTo(1), "Iron's Spells & Spellbooks",
                        "irons_spellbooks"));
        ITEMS.register("gemini_spellbook_blue",
                () -> new MissingModItem(new Item.Properties().stacksTo(1), "Iron's Spells & Spellbooks",
                        "irons_spellbooks"));
    }
}
