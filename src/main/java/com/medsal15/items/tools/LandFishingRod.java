package com.medsal15.items.tools;

import javax.annotation.Nonnull;

import com.medsal15.entities.LandFishingHook;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class LandFishingRod extends FishingRodItem {
    public LandFishingRod(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.fishing != null) {
            if (!level.isClientSide) {
                int damage = player.fishing.retrieve(stack);
                stack.hurtAndBreak(damage, player, LivingEntity.getSlotForHand(hand));
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE,
                    SoundSource.NEUTRAL, 1, .4F / (level.getRandom().nextFloat() * .4F) + .8F);
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW,
                    SoundSource.NEUTRAL, .5F, .4F / (level.getRandom().nextFloat() * .4F) + .8F);
            if (level instanceof ServerLevel serverLevel) {
                int lure = (int) (EnchantmentHelper.getFishingTimeReduction(serverLevel, stack, player) * 20F);
                int luck = EnchantmentHelper.getFishingLuckBonus(serverLevel, stack, player);
                level.addFreshEntity(new LandFishingHook(player, level, luck, lure));
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public int getEnchantmentValue() {
        return 5;
    }
}
