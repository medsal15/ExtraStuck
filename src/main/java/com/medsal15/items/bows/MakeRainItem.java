package com.medsal15.items.bows;

import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.items.ESItems;
import com.medsal15.utils.ESTags;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

public class MakeRainItem extends BowItem {
    public MakeRainItem(Properties properties) {
        super(properties);
    }

    @Override
    protected Projectile createProjectile(@Nonnull Level level, @Nonnull LivingEntity shooter,
            @Nonnull ItemStack weapon, @Nonnull ItemStack ammo, boolean isCrit) {
        if (!ammo.isEmpty() && !ammo.is(ESTags.Items.MAKE_IT_RAIN_FORBIDDEN)) {
            ItemStack rainArrow = ESItems.RAIN_ARROW.toStack();
            rainArrow.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(ammo)));
            ammo = rainArrow;
        }

        return super.createProjectile(level, shooter, weapon, ammo, isCrit);
    }
}
