package com.medsal15.items.shields;

import com.medsal15.data.ESLangProvider;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

/**
 * Special class for the slied
 */
public class SbahjShield extends ESShield implements IShieldBlock {
    public SbahjShield(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onShieldBlock(LivingShieldBlockEvent event) {
        var user = event.getEntity();
        // Copied from minestuck
        if (user.getCommandSenderWorld().isClientSide || user.getRandom().nextFloat() >= .25)
            return false;

        var stack = user.getUseItem();
        ItemEntity shield = new ItemEntity(user.level(), user.getX(), user.getY(), user.getZ(), stack.copy());
        shield.getItem().setCount(1);
        shield.setPickUpDelay(40);
        user.level().addFreshEntity(shield);
        stack.shrink(1);
        user.sendSystemMessage(Component.translatable(ESLangProvider.SLIED_DROP_KEY));

        return true;
    }
}
