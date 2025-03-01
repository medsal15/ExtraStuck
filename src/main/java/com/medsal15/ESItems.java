package com.medsal15;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.items.shields.ESShield;
import com.medsal15.items.shields.EffectShield;
import com.medsal15.items.shields.FlameShield;
import com.medsal15.items.shields.GlassShield;
import com.medsal15.items.shields.ThornShield;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESItems {
        public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

        public static final DeferredItem<Item> WOODEN_SHIELD = ITEMS.registerItem("wooden_shield", ESShield::new,
                        new Item.Properties().durability(80));
        public static final DeferredItem<Item> THORN_SHIELD = ITEMS.registerItem("thorn_shield",
                        (properties) -> new ThornShield(properties, 2),
                        new Item.Properties().durability(243));
        public static final DeferredItem<Item> WITHERED_SHIELD = ITEMS.registerItem("withered_shield",
                        (properties) -> new EffectShield(properties, MobEffects.WITHER, 200),
                        new Item.Properties().durability(792));
        public static final DeferredItem<Item> FLAME_SHIELD = ITEMS.registerItem("flame_shield",
                        (properties) -> new FlameShield(properties, 100),
                        new Item.Properties().durability(151));
        public static final DeferredItem<Item> GLASS_SHIELD = ITEMS.registerItem("glass_shield", GlassShield::new,
                        new Item.Properties().durability(1));
        public static final DeferredItem<Item> REINFORCED_GLASS_SHIELD = ITEMS.registerItem("reinforced_glass_shield",
                        GlassShield::new, new Item.Properties().durability(336));

        public static Collection<DeferredItem<Item>> getItems() {
                ArrayList<DeferredItem<Item>> list = new ArrayList<>();
                list.addAll(getShields());
                return list;
        }

        public static Collection<DeferredItem<Item>> getShields() {
                ArrayList<DeferredItem<Item>> list = new ArrayList<>();
                list.add(WOODEN_SHIELD);
                list.add(THORN_SHIELD);
                list.add(WITHERED_SHIELD);
                list.add(FLAME_SHIELD);
                list.add(GLASS_SHIELD);
                list.add(REINFORCED_GLASS_SHIELD);
                return list;
        }
}
