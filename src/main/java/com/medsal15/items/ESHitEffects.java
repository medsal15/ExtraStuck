package com.medsal15.items;

import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.medsal15.ExtraStuck;
import com.medsal15.mobeffects.ESMobEffects;
import com.mraof.minestuck.item.weapon.OnHitEffect;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.Title;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

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

    public static void timeStop(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int duration = 5 * 20;
        if (attacker instanceof ServerPlayer player
                && (player.isCreative() || Title.isPlayerOfAspect(player, EnumAspect.TIME))) {
            duration = 15 * 20;
        }

        target.addEffect(new MobEffectInstance(ESMobEffects.TIME_STOP, duration));
    }

    /**
     * Deals between 1 and <code>maxDamage^2</code> extra damage to the enemy
     */
    public static OnHitEffect randomDamage(int maxDamage) {
        return (stack, target, attacker) -> {
            DamageSource source;
            if (attacker instanceof Player player) {
                source = attacker.damageSources().playerAttack(player);
            } else {
                source = attacker.damageSources().mobAttack(attacker);
            }

            // Only deals damage if the number is bigger than the default damage
            float rng = (float) (attacker.getRandom().nextInt(maxDamage) + 1);

            if (stack.has(DataComponents.ATTRIBUTE_MODIFIERS)) {
                ItemAttributeModifiers modifiers = stack.getAttributeModifiers();
                for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
                    if (entry.attribute() == Attributes.ATTACK_DAMAGE) {
                        rng += entry.modifier().amount();
                    }
                }
            }

            // Extra damage based on luck
            AttributeInstance luckatin = attacker.getAttribute(Attributes.LUCK);
            if (luckatin != null) {
                rng += (float) luckatin.getValue();
            }

            target.hurt(source, rng);
        };
    }

    /**
     * Adds a chance to drop an item on attack
     *
     * @param item    Item to drop
     * @param chance  Chance to drop
     * @param message Translation key of the message to send to the attacker
     */
    public static OnHitEffect chanceDrop(Supplier<ItemStack> item, float chance, @Nullable Supplier<String> message) {
        return (stack, target, attacker) -> {
            if (attacker.getRandom().nextFloat() < chance) {
                ItemStack itemStack = item.get().copy();
                ItemEntity entity = new ItemEntity(attacker.level(), target.getX(), target.getY(), target.getZ(),
                        itemStack);
                attacker.level().addFreshEntity(entity);
                if (message != null)
                    attacker.sendSystemMessage(Component.translatable(message.get()));
            }
        };
    }

    public static void rainbowEffect(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int rng = attacker.getRandom().nextInt(7);
        Holder<MobEffect> effect = null;
        switch (rng) {
            case 0:
                effect = MobEffects.DAMAGE_BOOST;
                break;
            case 1:
                effect = MobEffects.DAMAGE_RESISTANCE;
                break;
            case 2:
                effect = MobEffects.UNLUCK;
                break;
            case 3:
                effect = MobEffects.LUCK;
                break;
            case 4:
                effect = MobEffects.MOVEMENT_SPEED;
                break;
            case 5:
                effect = MobEffects.MOVEMENT_SLOWDOWN;
                break;
            case 6:
                effect = MobEffects.LEVITATION;
                break;
            default:
                ExtraStuck.LOGGER.error("rainbowEffect rolled a {} even though it shouldn't be above 6", rng);
                break;
        }

        if (effect != null) {
            target.addEffect(new MobEffectInstance(effect, 50));
            attacker.addEffect(new MobEffectInstance(effect, 50));
        }
    }
}
