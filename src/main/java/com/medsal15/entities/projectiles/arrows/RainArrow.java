package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RainArrow extends AbstractArrow {
    public RainArrow(EntityType<? extends RainArrow> entityType, Level level) {
        super(entityType, level);
    }

    public RainArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.RAIN_ARROW.get(), shooter, level, pickup, weapon);
    }

    public RainArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.RAIN_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        // This shouldn't be picked up anyways
        return ESItems.RAIN_ARROW.toStack();
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        ProjectileItem projectileItem = getRealArrow();

        if (projectileItem != null) {
            ItemStack ammo = getPickupItem().getOrDefault(DataComponents.CONTAINER,
                    ItemContainerContents.EMPTY).getStackInSlot(0);
            // Shouldn't happen, but just in case
            if (!ammo.isEmpty()) {
                Vec3 location = result.getLocation();
                for (int i = 0; i < 25; i++) {
                    double x = location.x() + getRandom().nextDouble() * (double) i / 5d - (double) i / 10d;
                    double y = location.y() + getRandom().nextDouble() * 5d + 15d;
                    double z = location.z() + getRandom().nextDouble() * (double) i / 5d - (double) i / 10d;
                    Projectile projectile = projectileItem.asProjectile(level(), new Vec3(x, y, z), ammo,
                            Direction.DOWN);
                    projectile.setOwner(getOwner());
                    if (projectile instanceof AbstractArrow arrow)
                        arrow.pickup = Pickup.CREATIVE_ONLY;
                    level().addFreshEntity(projectile);
                }
            }
        }

        discard();
    }

    @Nullable
    protected ProjectileItem getRealArrow() {
        ItemContainerContents container = getPickupItem().getOrDefault(DataComponents.CONTAINER,
                ItemContainerContents.EMPTY);
        if (container.getSlots() < 1)
            return null;
        ItemStack ammo = container.getStackInSlot(0);
        if (ammo.getItem() instanceof ProjectileItem projectileItem)
            return projectileItem;
        return null;
    }
}
