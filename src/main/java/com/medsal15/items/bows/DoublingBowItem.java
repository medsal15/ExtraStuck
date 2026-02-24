package com.medsal15.items.bows;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class DoublingBowItem extends BowItem {
    private boolean fakeArrow;
    private final int copies;

    public DoublingBowItem(Properties properties) {
        super(properties);
        copies = 1;
    }

    public DoublingBowItem(Properties properties, int copies) {
        super(properties);
        this.copies = copies;
    }

    @Override
    protected void shoot(@Nonnull ServerLevel level, @Nonnull LivingEntity shooter, @Nonnull InteractionHand hand,
            @Nonnull ItemStack weapon, @Nonnull List<ItemStack> projectileItems, float velocity, float inaccuracy,
            boolean isCrit, @Nullable LivingEntity target) {
        fakeArrow = false;
        if (projectileItems.size() > 1 || copies <= 0) {
            super.shoot(level, shooter, hand, weapon, projectileItems, velocity, inaccuracy, isCrit, target);
            return;
        }
        float spread = EnchantmentHelper.processProjectileSpread(level, weapon, shooter, 0.0F);
        // TODO test if this changes anything
        float f1 = copies == 1 ? 0.0F : 2.0F * spread / (float) copies;
        float f2 = (float) (copies % 2) * f1 / 2.0F;
        float f3 = 1.0F;

        for (int i = 0; i < copies + 1; i++) {
            ItemStack stack = projectileItems.get(0);
            if (!stack.isEmpty()) {
                float f4 = f2 + f3 * (float) ((i + 1) / 2) * f1;
                f3 = -f3;
                Projectile projectile = this.createProjectile(level, shooter, weapon, stack, isCrit);
                this.shootProjectile(shooter, projectile, i, velocity, inaccuracy, f4, target);
                level.addFreshEntity(projectile);
                weapon.hurtAndBreak(this.getDurabilityUse(stack), shooter, LivingEntity.getSlotForHand(hand));
                if (weapon.isEmpty()) {
                    break;
                }
                fakeArrow = true;
            }
        }
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
