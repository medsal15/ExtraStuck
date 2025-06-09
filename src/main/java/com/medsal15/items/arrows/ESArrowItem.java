package com.medsal15.items.arrows;

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

public class ESArrowItem extends ArrowItem {
    private CreaArro create;
    private AsProj projectile;

    public ESArrowItem(Properties properties, CreaArro create, AsProj projectile) {
        super(properties);
        this.create = create;
        this.projectile = projectile;
        DispenserBlock.registerProjectileBehavior(this);
    }

    public ESArrowItem(Properties properties, ToEntity toEntity) {
        super(properties);
        this.create = toEntity;
        this.projectile = toEntity;
        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public AbstractArrow createArrow(@Nonnull Level level, @Nonnull ItemStack ammo, @Nonnull LivingEntity shooter,
            @Nullable ItemStack weapon) {
        return create.create(level, ammo.copyWithCount(1), shooter, weapon);
    }

    @Override
    public Projectile asProjectile(@Nonnull Level level, @Nonnull Position pos, @Nonnull ItemStack stack,
            @Nonnull Direction direction) {
        var proj = projectile.asProj(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        proj.pickup = AbstractArrow.Pickup.ALLOWED;
        return proj;
    }

    public static interface CreaArro {
        AbstractArrow create(@Nonnull Level level, @Nonnull ItemStack ammo, @Nonnull LivingEntity shooter,
                @Nullable ItemStack weapon);
    }

    public static interface AsProj {
        AbstractArrow asProj(@Nonnull Level level, double x, double y, double z, @Nonnull ItemStack stack,
                @Nullable ItemStack weapon);
    }

    public static interface ToEntity extends CreaArro, AsProj {
    }
}
