package com.medsal15.items.tools;

import javax.annotation.Nonnull;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BrokenWatchItem extends Item {
    public BrokenWatchItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slotId,
            boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (!isSelected || !(entity instanceof Player player) || player.level().isClientSide
                || player.getCooldowns().isOnCooldown(this))
            return;

        double luck = player.getAttributeValue(Attributes.LUCK);
        double chanceBonus = 1;
        double chanceMalus = 1;

        if (luck > 0)
            chanceBonus += luck;
        else
            chanceMalus += luck;
        if (Title.isPlayerOfAspect((ServerPlayer) player, EnumAspect.TIME))
            chanceBonus *= 2;

        if (player.getRandom().nextDouble() > chanceBonus / (chanceBonus + chanceMalus)) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 600));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600));
        } else {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600));
        }

        player.getCooldowns().addCooldown(this, 2400);
    }
}
