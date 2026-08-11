package com.medsal15.items.food;

import java.text.NumberFormat;
import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.config.ConfigClient;
import com.medsal15.data.ESLangProvider;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class BurningFood extends Item {
    private final float seconds;

    public BurningFood(Properties properties, float seconds) {
        super(properties);
        this.seconds = seconds;
    }

    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity) {
        entity.igniteForSeconds(seconds);
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if (ConfigClient.displayFoodEffects) {
            tooltipComponents.add(Component.translatable(ESLangProvider.BURN_DURATION_SECONDS,
                    NumberFormat.getInstance().format(seconds)).withStyle(ChatFormatting.BLUE));
        }
    }
}
