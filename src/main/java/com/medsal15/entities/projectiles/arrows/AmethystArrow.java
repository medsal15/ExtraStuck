package com.medsal15.entities.projectiles.arrows;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AmethystArrow extends AbstractArrow {
    public AmethystArrow(EntityType<? extends AmethystArrow> entityType, Level level) {
        super(entityType, level);
        setBaseDamage(3D);
    }

    public AmethystArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.AMETHYST_ARROW.get(), shooter, level, pickup, weapon);
        setBaseDamage(3D);
    }

    public AmethystArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.AMETHYST_ARROW.get(), x, y, z, level, pickup, weapon);
        setBaseDamage(3D);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ESItems.AMETHYST_ARROW.toStack();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.AMETHYST_BLOCK_STEP;
    }

    @Override
    public byte getPierceLevel() {
        return (byte) (super.getPierceLevel() + 2);
    }
}
