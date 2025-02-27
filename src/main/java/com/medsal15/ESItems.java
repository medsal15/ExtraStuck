package com.medsal15;

import com.medsal15.items.shields.ESShield;
import com.medsal15.items.shields.EffectShield;
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
}
