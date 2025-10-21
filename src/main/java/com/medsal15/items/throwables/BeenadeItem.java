package com.medsal15.items.throwables;

import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.data.ESLangProvider;
import com.medsal15.entities.projectiles.ThrownBeenade;
import com.medsal15.items.ESDataComponents;
import com.medsal15.utils.ESTags;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class BeenadeItem extends Item {
    public BeenadeItem(Properties properties) {
        super(properties);
        // TODO custom dispenser behavior
    }

    @Override
    public InteractionResult interactLivingEntity(@Nonnull ItemStack stack, @Nonnull Player player,
            @Nonnull LivingEntity interactionTarget, @Nonnull InteractionHand hand) {
        if (stack.has(ESDataComponents.ENTITY_TYPE)) {
            super.interactLivingEntity(stack, player, interactionTarget, hand);
        }

        // Capture bees (but only if they're not owned by someone)
        if (interactionTarget.getType().is(ESTags.EntityTypes.BEENADE_ACCEPTS)) {
            if (interactionTarget instanceof OwnableEntity ownable && ownable.getOwner() != null)
                return InteractionResult.PASS;

            ItemStack copy = stack.copyWithCount(1);
            stack.shrink(1);
            CompoundTag entityData = new CompoundTag();
            interactionTarget.save(entityData);
            copy.set(ESDataComponents.ENTITY_TYPE, EntityType.getKey(interactionTarget.getType()).toString());
            copy.set(DataComponents.ENTITY_DATA, CustomData.of(entityData));
            if (!player.getInventory().add(copy)) {
                player.drop(copy, false);
            }
            interactionTarget.remove(RemovalReason.DISCARDED);

            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(stack, player, interactionTarget, hand);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.has(ESDataComponents.ENTITY_TYPE)) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW,
                    SoundSource.PLAYERS, .5F, .4F / (level.getRandom().nextFloat() * .4F + .8F));

            if (!level.isClientSide) {
                ThrownBeenade beenade = new ThrownBeenade(level, player);
                beenade.setItem(stack.copyWithCount(1));
                beenade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 1F);
                level.addFreshEntity(beenade);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            stack.shrink(1);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        // No bee stored, no beenade to throw
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if (stack.has(ESDataComponents.ENTITY_TYPE)) {
            tooltipComponents.add(Component.translatable(ESLangProvider.BEENADE_LOADED).withStyle(ChatFormatting.GRAY));
        }
    }

    public static boolean hasEntity(ItemStack stack) {
        return stack.has(ESDataComponents.ENTITY_TYPE);
    }
}
