package com.medsal15.items.bows;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

public class BwoItem extends BowItem {
    public BwoItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void shoot(@Nonnull ServerLevel level, @Nonnull LivingEntity shooter, @Nonnull InteractionHand hand,
            @Nonnull ItemStack weapon, @Nonnull List<ItemStack> projectileItems, float velocity, float inaccuracy,
            boolean isCrit, @Nullable LivingEntity target) {
        super.shoot(level, shooter, hand, weapon, projectileItems, velocity / 4f, inaccuracy, false, target);
    }
}
