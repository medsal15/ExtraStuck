package com.medsal15.items;

import java.util.Optional;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.Title;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class ESHitEffects {
    /**
     * Attacks will apply unluck to targets and luck to attackers when done by a
     * thief/light player
     */
    public static void stealLuck(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof ServerPlayer player) {
            Optional<Title> title = Title.getTitle(player);

            if (player.isCreative() || (!title.isEmpty()
                    && (title.get().heroAspect() == EnumAspect.LIGHT || title.get().heroClass() == EnumClass.THIEF))) {
                target.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 50));
                attacker.addEffect(new MobEffectInstance(MobEffects.LUCK, 50));
            }
        }
    }
}
