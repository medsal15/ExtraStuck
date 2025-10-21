package com.medsal15.items.throwables;

import javax.annotation.Nonnull;

import com.medsal15.entities.projectiles.ThrownBeeLarva;

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

public class BeeLarvaItem extends Item implements ProjectileItem {
    public BeeLarvaItem(Properties properties) {
        super(properties);
        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS,
                .5F, .4F / (level.getRandom().nextFloat() * .4F + .8F));

        if (!level.isClientSide) {
            ThrownBeeLarva thrownBeegg = new ThrownBeeLarva(level, player);
            thrownBeegg.setItem(stack);
            thrownBeegg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 1F);
            level.addFreshEntity(thrownBeegg);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        stack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public Projectile asProjectile(@Nonnull Level level, @Nonnull Position pos, @Nonnull ItemStack stack,
            @Nonnull Direction direction) {
        ThrownBeeLarva thrownBeegg = new ThrownBeeLarva(level, pos.x(), pos.y(), pos.z());
        thrownBeegg.setItem(stack);
        return thrownBeegg;
    }
}
