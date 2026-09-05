package com.medsal15.entities.projectiles.magic.circles;

import javax.annotation.Nullable;

import com.medsal15.ExtraStuck;
import com.medsal15.utils.ESEntityUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class LifeCircle extends CircleEntity {
    public LifeCircle(EntityType<? extends LifeCircle> type, Level level) {
        super(type, level);
    }

    public int getColor() {
        return 0xA49787;
    }

    public boolean tintSymbol() {
        return false;
    }

    @Nullable
    public ResourceLocation getSymbolLocation() {
        return ExtraStuck.modid("textures/entity/circle/life.png");
    }

    @Override
    public void tick() {
        super.tick();
        if (isRemoved())
            return;

        if (life % 10 == 0) {
            level().getEntities(this, getBoundingBox()).forEach(entity -> {
                if (entity instanceof LivingEntity livingEntity
                        && ESEntityUtils.areFriendly(getOwner(), livingEntity)) {
                    livingEntity.heal(1f);
                }
            });
        }
    }
}
