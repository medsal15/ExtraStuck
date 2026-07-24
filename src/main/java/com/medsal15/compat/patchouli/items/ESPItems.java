package com.medsal15.compat.patchouli.items;

import com.medsal15.ExtraStuck;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vazkii.patchouli.common.item.PatchouliDataComponents;

public final class ESPItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    public static final DeferredItem<Item> GUIDE = ITEMS.register("guide", GuideItem::new);

    public static ItemStack getGuideBook() {
        // If Patchouli isn't adding a way to do this, I'm doing it myself!
        ItemStack guide = GUIDE.toStack();
        guide.set(PatchouliDataComponents.BOOK, ExtraStuck.modid("extrastuck"));
        return guide;
    }
}
