package com.medsal15.items.weaponeffects;

import com.medsal15.config.ConfigServer;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.components.SteamFuelComponent;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.item.weapon.RightClickBlockEffect;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public final class ESRightClickBlockEffects {
    public static InteractionResult brush(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        if (!level.isClientSide && player != null
                && level.getBlockEntity(pos) instanceof BrushableBlockEntity brushable) {
            boolean complete = brushable.brush(level.getGameTime(), player, context.getClickedFace());
            if (complete) {
                ItemStack stack = context.getItemInHand();
                EquipmentSlot slot = context.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND
                        : EquipmentSlot.OFFHAND;
                stack.hurtAndBreak(1, player, slot);
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    public static InteractionResult pry(UseOnContext context) {
        Player player = context.getPlayer();

        if (player != null && player.isCrouching()) {
            BlockPos pos = context.getClickedPos();
            Level level = context.getLevel();

            if (level.getBlockState(pos).is(ESTags.Blocks.PRYABLE) && player.canInteractWithBlock(pos, 0)) {
                ItemStack stack = context.getItemInHand();
                EquipmentSlot slot = context.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND
                        : EquipmentSlot.OFFHAND;

                level.destroyBlock(pos, true);
                stack.hurtAndBreak(1, player, slot);

                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    /**
     * Checks if the item has enough steam fuel and is active to apply a separate
     * right click block effect
     */
    public static RightClickBlockEffect steamPowered(boolean consume, RightClickBlockEffect effect) {
        return context -> {
            ItemStack stack = context.getItemInHand();
            if (!stack.has(ESDataComponents.STEAM_FUEL))
                return InteractionResult.PASS;

            int fuel = ConfigServer.STEAM_FUEL_CONSUME.get();
            SteamFuelComponent steamFuel = stack.get(ESDataComponents.STEAM_FUEL);
            if (steamFuel.burning() && steamFuel.fuel() >= fuel) {
                if (consume) {
                    steamFuel = steamFuel.drain(fuel);

                    // Not enough fuel to keep going
                    if (steamFuel.fuel() < fuel) {
                        steamFuel = steamFuel.extinguish();
                        Player player = context.getPlayer();
                        if (player != null)
                            player.playSound(SoundEvents.FIRE_EXTINGUISH);
                    }

                    stack.set(ESDataComponents.STEAM_FUEL, steamFuel);
                }
                return effect.onClick(context);
            }

            return InteractionResult.PASS;
        };
    }

    // Mostly a copy of flint and steel's code
    public static InteractionResult setOnFire(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState target = level.getBlockState(blockpos);
        BlockState burningState = target.getToolModifiedState(context,
                net.neoforged.neoforge.common.ItemAbilities.FIRESTARTER_LIGHT, false);
        if (burningState == null) {
            BlockPos firePos = blockpos.relative(context.getClickedFace());
            if (BaseFireBlock.canBePlacedAt(level, firePos, context.getHorizontalDirection())) {
                level.playSound(player, firePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F,
                        level.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState fireState = BaseFireBlock.getState(level, firePos);
                level.setBlock(firePos, fireState, 11);
                level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);

                ItemStack itemstack = context.getItemInHand();
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, firePos, itemstack);
                    itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            level.playSound(player, blockpos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1,
                    level.getRandom().nextFloat() * .4f + .8f);
            level.setBlock(blockpos, burningState, 11);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockpos);
            if (player != null) {
                context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
    }
}
