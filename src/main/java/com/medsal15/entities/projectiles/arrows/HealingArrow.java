package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class HealingArrow extends AbstractArrow {
    public HealingArrow(EntityType<? extends HealingArrow> entityType, Level level) {
        super(entityType, level);
        setBaseDamage(0);
    }

    public HealingArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.HEALING_ARROW.get(), shooter, level, pickup, weapon);
        setBaseDamage(0);
    }

    public HealingArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.HEALING_ARROW.get(), x, y, z, level, pickup, weapon);
        setBaseDamage(0);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.CRUSADER_CROSSBOLT.get());
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        super.onHitEntity(result);

        Entity ent = result.getEntity();
        if (!(ent instanceof LivingEntity entity))
            return;

        Entity owner = getOwner();
        /**
         * Allow healing if, in order,
         * there is no owner (like dispensers),
         * the owner is not part of any team (any small group of players),
         * the target is tamed by the owner (taming does not add to a team and vice
         * versa),
         * or the target is in the same team (dont want players to heal enemies)
         */
        boolean canHeal = owner == null ||
                owner.getTeam() == null ||
                (owner instanceof LivingEntity lowner && ent instanceof TamableAnimal tame
                        && tame.isOwnedBy(lowner))
                ||
                // todo? might make this a config option
                entity.isAlliedTo(owner);

        if (canHeal) {
            entity.heal(5);
        }
    }
}
