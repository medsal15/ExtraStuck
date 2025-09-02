package com.medsal15.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class ESInventoryTickEffects {
    public static void blind(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (selected && entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
        }
    }
}
