package com.medsal15.items.bows;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.medsal15.config.ConfigServer;
import com.medsal15.utils.ESTags;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

public class RainbowBowItem extends BowItem {
    private boolean converted;

    public RainbowBowItem(Properties properties) {
        super(properties);
    }

    @Override
    protected Projectile createProjectile(@Nonnull Level level, @Nonnull LivingEntity shooter,
            @Nonnull ItemStack weapon, @Nonnull ItemStack ammo, boolean isCrit) {
        converted = false;
        if (ammo.is(Items.ARROW)) {
            Optional<? extends Holder<MobEffect>> effect = null;
            var effectRegistry = shooter.registryAccess().registryOrThrow(Registries.MOB_EFFECT);
            if (ConfigServer.RAINBOW_WHITELIST.getAsBoolean()) {
                effect = effectRegistry.getRandomElementOf(ESTags.MobEffects.RAINBOW_BOW_EFFECTS, shooter.getRandom());
            } else {
                effect = effectRegistry.getRandom(shooter.getRandom());
            }

            if (effect.isPresent()) {
                converted = true;
                ItemStack tippedAmmo = new ItemStack(Items.TIPPED_ARROW);
                tippedAmmo.setCount(ammo.getCount());
                tippedAmmo.applyComponents(ammo.getComponentsPatch());
                ammo = tippedAmmo;

                ammo.set(DataComponents.POTION_CONTENTS,
                        new PotionContents(Optional.empty(), Optional.empty(), List.of(
                                new MobEffectInstance(effect.get(), 200))));
            }
        }

        return super.createProjectile(level, shooter, weapon, ammo, isCrit);
    }

    @Override
    public AbstractArrow customArrow(@Nonnull AbstractArrow abstractArrow, @Nonnull ItemStack projectileStack,
            @Nonnull ItemStack weaponStack) {
        if (converted) {
            abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        return super.customArrow(abstractArrow, projectileStack, weaponStack);
    }
}
