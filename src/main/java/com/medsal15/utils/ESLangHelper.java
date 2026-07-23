package com.medsal15.utils;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public final class ESLangHelper {
    public static String getEnchantmentKey(ResourceKey<Enchantment> enchantment) {
        ResourceLocation location = enchantment.location();
        return "enchantment." + location.getNamespace() + "." + location.getPath();
    }
}
