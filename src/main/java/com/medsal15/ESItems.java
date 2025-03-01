package com.medsal15;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.items.shields.ChangeShield;
import com.medsal15.items.shields.FlameShield;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESItems {
        public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

        public static final DeferredItem<Item> FLAME_SHIELD = ITEMS.registerItem("flame_shield",
                        (properties) -> new FlameShield(properties, 100),
                        new Item.Properties().durability(80));
        public static final DeferredItem<Item> WOODEN_SHIELD = ITEMS.registerItem("wooden_shield",
                        (properties) -> new ChangeShield(properties, FLAME_SHIELD, DamageTypeTags.IS_FIRE),
                        new Item.Properties().durability(80));

        public static Collection<DeferredItem<Item>> getItems() {
                ArrayList<DeferredItem<Item>> list = new ArrayList<>();
                list.addAll(getShields());
                return list;
        }

        public static Collection<DeferredItem<Item>> getShields() {
                ArrayList<DeferredItem<Item>> list = new ArrayList<>();
                list.add(WOODEN_SHIELD);
                list.add(FLAME_SHIELD);
                return list;
        }
}
