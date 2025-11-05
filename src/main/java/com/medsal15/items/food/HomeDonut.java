package com.medsal15.items.food;

import javax.annotation.Nonnull;

import com.medsal15.data.ESLangProvider;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;

public class HomeDonut extends Item {
    public HomeDonut(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level,
            @Nonnull LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer player) {
            DimensionTransition transition = player.findRespawnPositionAndUseSpawnBlock(true,
                    DimensionTransition.PLAY_PORTAL_SOUND);
            if (transition.missingRespawnBlock()) {
                player.sendSystemMessage(Component.translatable(ESLangProvider.HOME_DONUT_NO_TP));
            } else {
                player.changeDimension(transition);
            }
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
