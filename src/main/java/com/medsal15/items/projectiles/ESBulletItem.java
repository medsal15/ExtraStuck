package com.medsal15.items.projectiles;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class ESBulletItem extends Item implements ProjectileItem {
    private ICreateArrow create;
    private IAsProjectile asProj;

    public ESBulletItem(Properties properties, ICreateArrow create, IAsProjectile asProj) {
        super(properties);
        this.create = create;
        this.asProj = asProj;
        DispenserBlock.registerProjectileBehavior(this);
    }

    public AbstractArrow createBullet(@Nonnull Level level, @Nonnull ItemStack ammo, @Nonnull LivingEntity shooter,
            @Nullable ItemStack weapon) {
        return create.createArrow(level, ammo.copyWithCount(1), shooter, weapon);
    }

    @Override
    public Projectile asProjectile(@Nonnull Level level, @Nonnull Position pos, @Nonnull ItemStack stack,
            @Nonnull Direction direction) {
        AbstractArrow proj = asProj.asProjectile(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        proj.pickup = AbstractArrow.Pickup.ALLOWED;
        return proj;
    }
}
