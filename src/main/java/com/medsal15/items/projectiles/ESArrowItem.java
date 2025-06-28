package com.medsal15.items.projectiles;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class ESArrowItem extends ArrowItem implements ICreateArrow {
    private ICreateArrow create;
    private IAsProjectile projectile;

    public ESArrowItem(Properties properties, ICreateArrow create, IAsProjectile projectile) {
        super(properties);
        this.create = create;
        this.projectile = projectile;
        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public AbstractArrow createArrow(@Nonnull Level level, @Nonnull ItemStack ammo, @Nonnull LivingEntity shooter,
            @Nullable ItemStack weapon) {
        return create.createArrow(level, ammo.copyWithCount(1), shooter, weapon);
    }

    @Override
    public Projectile asProjectile(@Nonnull Level level, @Nonnull Position pos, @Nonnull ItemStack stack,
            @Nonnull Direction direction) {
        AbstractArrow proj = projectile.asProjectile(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        proj.pickup = AbstractArrow.Pickup.ALLOWED;
        return proj;
    }
}
