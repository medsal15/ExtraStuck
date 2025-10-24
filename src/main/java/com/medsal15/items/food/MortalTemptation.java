package com.medsal15.items.food;

import javax.annotation.Nonnull;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.ConsortReputation;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MortalTemptation extends Item {
    public MortalTemptation(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(@Nonnull ItemStack stack, @Nonnull Player player,
            @Nonnull LivingEntity interactionTarget, @Nonnull InteractionHand usedHand) {
        if (interactionTarget instanceof ConsortEntity consort) {
            consort.eat(player.level(), stack);
            stack.consume(1, player);
            if (player instanceof ServerPlayer serverPlayer) {
                ConsortReputation reputation = ConsortReputation.get(serverPlayer);
                reputation.addConsortReputation(25, consort.getHomeDimension());
            }

            return InteractionResult.CONSUME;
        }

        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }
}
