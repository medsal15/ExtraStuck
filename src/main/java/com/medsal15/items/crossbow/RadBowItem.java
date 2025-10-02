package com.medsal15.items.crossbow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.config.ConfigCommon;
import com.medsal15.data.ESItemTags;
import com.medsal15.data.ESLangProvider;
import com.medsal15.entities.projectiles.UraniumRod;
import com.medsal15.items.ESDataComponents;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class RadBowItem extends CrossbowItem {
    public RadBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.is(ESItemTags.URANIUM_RODS) || stack.is(MSItems.URANIUM_POWERED_STICK);
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return stack -> stack.is(ESItemTags.URANIUM_RODS) || stack.is(MSItems.URANIUM_POWERED_STICK);
    }

    @Override
    protected Projectile createProjectile(@Nonnull Level level, @Nonnull LivingEntity shooter,
            @Nonnull ItemStack weapon, @Nonnull ItemStack ammo, boolean isCrit) {
        Projectile projectile = new UraniumRod(level, ammo, shooter, weapon);
        return projectile;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        int charges = stack.getOrDefault(ESDataComponents.URANIUM_CHARGE, 0);
        if (charges > 0) {
            tooltipComponents.add(Component.translatable(ESLangProvider.RADBOW_CHARGE, charges));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int charges = stack.getOrDefault(ESDataComponents.URANIUM_CHARGE, 0);
        if (charges > 0) {
            performShooting(level, player, hand, stack, 3F, 1F, null);
            return InteractionResultHolder.consume(stack);
        } else if (!player.getProjectile(stack).isEmpty()) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity,
            int timeLeft) {
        int time = getUseDuration(stack, entity) - timeLeft;
        float power = getPowerForTime(time, stack, entity);
        if (power >= 1 && !isCharged(stack)) {
            stack.set(ESDataComponents.URANIUM_CHARGE, ConfigCommon.RADBOW_CHARGES.get());
        }
    }

    public static boolean isCharged(ItemStack radbow) {
        return radbow.getOrDefault(ESDataComponents.URANIUM_CHARGE, 0) > 0;
    }

    private static float getPowerForTime(int timeLeft, ItemStack stack, LivingEntity shooter) {
        float f = (float) timeLeft / (float) getChargeDuration(stack, shooter);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public void performShooting(@Nonnull Level level, @Nonnull LivingEntity shooter, @Nonnull InteractionHand hand,
            @Nonnull ItemStack weapon, float velocity, float inaccuracy, @Nullable LivingEntity target) {
        if (level instanceof ServerLevel serverLevel) {
            if (shooter instanceof Player player && net.neoforged.neoforge.event.EventHooks.onArrowLoose(weapon,
                    shooter.level(), player, 1, true) < 0)
                return;
            int charges = weapon.getOrDefault(ESDataComponents.URANIUM_CHARGE, 0);
            if (charges > 0) {
                // Support for projectile increasing enchants
                int shots = EnchantmentHelper.processProjectileCount(serverLevel, weapon, shooter, 1);
                List<ItemStack> ammo = new ArrayList<>(shots);
                for (int i = 0; i < shots; i++) {
                    ammo.add(MSItems.URANIUM_POWERED_STICK.toStack());
                }
                // Support for lower consumption enchants
                int used = shooter.hasInfiniteMaterials() ? 0
                        : EnchantmentHelper.processAmmoUse(serverLevel, weapon, MSItems.URANIUM_POWERED_STICK.toStack(),
                                1);
                weapon.set(ESDataComponents.URANIUM_CHARGE, charges - used);

                shoot(serverLevel, shooter, hand, weapon, ammo, velocity,
                        inaccuracy, shooter instanceof Player, target);
                if (shooter instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.SHOT_CROSSBOW.trigger(serverPlayer, weapon);
                    serverPlayer.awardStat(Stats.ITEM_USED.get(weapon.getItem()));
                }
            }
        }
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack stack, @Nonnull ItemStack repairCandidate) {
        return stack.is(ESItemTags.URANIUM_RODS) || stack.is(MSItems.URANIUM_POWERED_STICK);
    }

    @Override
    public void onUseTick(@Nonnull Level level, @Nonnull LivingEntity livingEntity, @Nonnull ItemStack stack,
            int count) {
    }

    @Override
    public int getEnchantmentValue() {
        return Tiers.IRON.getEnchantmentValue();
    }
}
