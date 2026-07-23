package com.medsal15.items.bows;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

public class FastBowItem extends BowItem {
    private final float velocity;

    public FastBowItem(Properties properties) {
        super(properties);

        velocity = 2;
    }

    public FastBowItem(Properties properties, float velocity) {
        super(properties);

        this.velocity = velocity;
    }

    @Override
    protected void shoot(@Nonnull ServerLevel level, @Nonnull LivingEntity shooter, @Nonnull InteractionHand hand,
            @Nonnull ItemStack weapon, @Nonnull List<ItemStack> projectileItems, float velocity, float inaccuracy,
            boolean isCrit,
            @Nullable LivingEntity target) {
        super.shoot(level, shooter, hand, weapon, projectileItems, velocity * this.velocity, inaccuracy, isCrit,
                target);
    }
}
