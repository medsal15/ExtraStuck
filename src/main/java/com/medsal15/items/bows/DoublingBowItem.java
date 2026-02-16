package com.medsal15.items.bows;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

public class DoublingBowItem extends BowItem {
    private boolean fakeArrow;

    public DoublingBowItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void shoot(@Nonnull ServerLevel level, @Nonnull LivingEntity shooter, @Nonnull InteractionHand hand,
            @Nonnull ItemStack weapon, @Nonnull List<ItemStack> projectileItems, float velocity, float inaccuracy,
            boolean isCrit, @Nullable LivingEntity target) {
        fakeArrow = false;
        super.shoot(level, shooter, hand, weapon, projectileItems, velocity, inaccuracy, isCrit, target);
        fakeArrow = true;
        super.shoot(level, shooter, hand, weapon, projectileItems, velocity, inaccuracy * 2f, isCrit, target);
    }

    @Override
    public AbstractArrow customArrow(@Nonnull AbstractArrow arrow, @Nonnull ItemStack projectileStack,
            @Nonnull ItemStack weaponStack) {
        if (fakeArrow) {
            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        return arrow;
    }
}
