package com.medsal15.items;

import java.util.function.Predicate;
import java.util.function.Supplier;

import com.mraof.minestuck.item.weapon.InventoryTickEffect;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class ESInventoryTickEffects {
    public static void blind(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (selected && entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
        }
    }

    public static InventoryTickEffect effectIfHeldOffHand(Predicate<ItemStack> valid,
            Supplier<MobEffectInstance> effect) {
        return (ItemStack stack, Level level, Entity entity, int slot, boolean selected) -> {
            if (!selected || !(entity instanceof Player player))
                return;
            if (valid.test(player.getItemInHand(InteractionHand.OFF_HAND))) {
                player.addEffect(effect.get());
            }
        };
    }
}
