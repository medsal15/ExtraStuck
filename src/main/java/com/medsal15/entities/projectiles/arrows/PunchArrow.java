package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PunchArrow extends AbstractArrow {
    public PunchArrow(EntityType<? extends PunchArrow> type, Level level) {
        super(type, level);
    }

    public PunchArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.PUNCH_ARROW.get(), shooter, level, pickup, weapon);
    }

    public PunchArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.PUNCH_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ESItems.PUNCH_ARROW.toStack();
    }

    @Override
    protected void doKnockback(@Nonnull LivingEntity entity, @Nonnull DamageSource damageSource) {
        double base_knockback = getWeaponItem() != null && level() instanceof ServerLevel serverLevel
                ? EnchantmentHelper.modifyKnockback(serverLevel, getWeaponItem(), entity, damageSource, 0)
                : 0;
        base_knockback += 2;
        double knockback_resistance = Math.max(0d, 1d - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        Vec3 knockback = getDeltaMovement().multiply(1, 0, 1).normalize()
                .scale(base_knockback * .6 * knockback_resistance);
        if (knockback.lengthSqr() > 0) {
            entity.push(knockback.x, .1, knockback.z);
        }
    }
}
