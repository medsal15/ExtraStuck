package com.medsal15.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Enemy;

public final class ESEntityUtils {
    public static boolean areFriendly(Entity source, LivingEntity target) {
        if (source == null)
            return true;
        if (target == null)
            return false;

        if (target.getType().is(ESTags.EntityTypes.ALWAYS_FRIENDLY))
            return true;
        if (target.getType().is(ESTags.EntityTypes.ALWAYS_HOSTILE))
            return false;

        if (target.isAlliedTo(source))
            return true;
        if (target instanceof TamableAnimal tamable && source instanceof LivingEntity livingSource) {
            if (tamable.isOwnedBy(livingSource))
                return true;
            LivingEntity owner = tamable.getOwner();
            if (owner != null && owner.isAlliedTo(source))
                return true;
        }

        return !(target instanceof Enemy);
    }
}
