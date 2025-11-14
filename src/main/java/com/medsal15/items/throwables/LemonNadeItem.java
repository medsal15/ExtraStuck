package com.medsal15.items.throwables;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.entities.projectiles.LemonNade;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class LemonNadeItem extends Item implements ProjectileItem {
    public LemonNadeItem(Properties properties) {
        super(properties);
        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public Projectile asProjectile(@Nonnull Level level, @Nonnull Position pos, @Nonnull ItemStack stack,
            @Nonnull Direction direction) {
        LemonNade lemonNade = new LemonNade(ESEntities.LEMONNADE.get(), pos.x(), pos.y(), pos.z(), level);
        lemonNade.setItem(stack);
        return lemonNade;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack result = stack.copy();
        result.consume(1, player);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.NEUTRAL,
                1.0F, 1.0F);

        if (!level.isClientSide) {
            LemonNade lemonNade = new LemonNade(ESEntities.LEMONNADE.get(), player, level);
            lemonNade.setItem(stack);
            lemonNade.shootFromRotation(player, player.getXRot(), player.getYRot(), -20F, .7F, 1F);
            level.addFreshEntity(lemonNade);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.success(result);
    }
}
