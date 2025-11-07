package com.medsal15.items.weaponeffects;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.medsal15.ExtraStuck;
import com.medsal15.config.ConfigCommon;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.components.SteamFuelComponent;
import com.medsal15.mobeffects.ESMobEffects;
import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.entity.item.VitalityGelEntity;
import com.mraof.minestuck.item.weapon.OnHitEffect;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.Title;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

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

            float rng = (float) (attacker.getRandom().nextInt(maxDamage) + 1);

            // Only deals damage if the number is bigger than the default damage
            if (stack.has(DataComponents.ATTRIBUTE_MODIFIERS)) {
                ItemAttributeModifiers modifiers = stack.getAttributeModifiers();
                for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
                    if (entry.attribute() == Attributes.ATTACK_DAMAGE) {
                        rng += entry.modifier().amount();
                    }
                }
            }

            // Extra damage based on luck
            rng += attacker.getAttributeValue(Attributes.LUCK);

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
            target.addEffect(new MobEffectInstance(effect, 77));
            attacker.addEffect(new MobEffectInstance(effect, 77));
        }
    }

    /**
     * Does a given effect during day/night time (based on skylight)
     */
    public static OnHitEffect dayNightEffect(OnHitEffect day, OnHitEffect night) {
        return (stack, target, attacker) -> {
            Level level = target.level();
            long time = level.getDayTime();
            if (time >= 6000 && time < 18000) {
                day.onHit(stack, target, attacker);
            } else {
                day.onHit(stack, target, attacker);
            }
        };
    }

    public static void randomMaxDamage(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        DamageSource source;
        if (attacker instanceof Player player) {
            source = attacker.damageSources().playerAttack(player);
        } else {
            source = attacker.damageSources().mobAttack(attacker);
        }

        float rng = 0;
        if (!Float.isInfinite(target.getMaxHealth())) {
            rng = attacker.getRandom().nextFloat() * target.getHealth();
        }

        target.hurt(source, rng);
    }

    /**
     * Attracts specific entities to the attacker in a range around the target
     */
    public static OnHitEffect attractItemsGrist(double range) {
        return (stack, target, attacker) -> {
            AABB box = new AABB(target.blockPosition()).inflate(range);
            List<Entity> entities = attacker.level().getEntities(target, box,
                    e -> e instanceof ItemEntity || e instanceof GristEntity || e instanceof VitalityGelEntity);
            // Try to give to player, otherwise teleport entity to attacker
            Consumer<Entity> apply = attacker instanceof Player player ? (e) -> {
                if (!giveToPlayer(player, e)) {
                    e.setPos(attacker.position());
                }
            } : (e) -> {
                e.setPos(attacker.position());
            };

            for (Entity entity : entities) {
                apply.accept(entity);
            }
        };
    }

    private static boolean giveToPlayer(Player player, Entity entity) {
        if (entity instanceof ItemEntity item) {
            boolean success = player.addItem(item.getItem());
            if (success)
                item.discard();
            return success;
        } else if (entity instanceof GristEntity grist) {
            grist.playerTouch(player);
            return true;
        } else if (entity instanceof VitalityGelEntity gel) {
            gel.playerTouch(player);
            return true;
        }
        return false;
    }

    /**
     * Gates an OnHitEffect behind an amount of FE stored in the stack
     *
     * The effect will not be called if there is no IEnergyStorage bound
     *
     * @param charge  amount of FE needed
     * @param effect  effect to call when there's enough FE
     * @param consume if true, FE will be consumed before the effect is called
     */
    public static OnHitEffect requireCharge(int charge, OnHitEffect effect, boolean consume) {
        return (stack, target, attacker) -> {
            @SuppressWarnings("null")
            IEnergyStorage energyStorage = Capabilities.EnergyStorage.ITEM.getCapability(stack, null);
            if (energyStorage != null && energyStorage.getEnergyStored() >= charge) {
                if (consume) {
                    energyStorage.extractEnergy(charge, false);
                }

                effect.onHit(stack, target, attacker);
            }
        };
    }

    /**
     * Gates an OnHitEffect behind consuming an amount of FE stored in the stack
     *
     * The effect will not be called if there is no IEnergyStorage bound
     *
     * @param charge amount of FE consumed
     * @param effect effect to call when there's enough FE
     */
    public static OnHitEffect requireCharge(int charge, OnHitEffect effect) {
        return requireCharge(charge, effect, true);
    }

    /**
     * Checks if the item has enough steam fuel and is active to apply a separate
     * hit effect
     */
    public static OnHitEffect steamPowered(boolean consume, OnHitEffect effect) {
        return (stack, target, attacker) -> {
            if (!stack.has(ESDataComponents.STEAM_FUEL))
                return;

            int fuel = ConfigCommon.STEAM_FUEL_CONSUME.get();
            SteamFuelComponent steamFuel = stack.get(ESDataComponents.STEAM_FUEL);
            if (steamFuel.burning() && steamFuel.fuel() >= fuel) {
                if (consume) {
                    steamFuel = steamFuel.drain(fuel);

                    // Not enough fuel to keep going
                    if (steamFuel.fuel() < fuel) {
                        steamFuel = steamFuel.extinguish();
                        attacker.playSound(SoundEvents.FIRE_EXTINGUISH);
                    }

                    stack.set(ESDataComponents.STEAM_FUEL, steamFuel);
                }
                effect.onHit(stack, target, target);
            }
        };
    }

    public static OnHitEffect biggerDamage(float damage) {
        return (stack, target, attacker) -> {
            AABB attackerBounds = attacker.getBoundingBox();
            double attackerVolume = attackerBounds.getXsize() * attackerBounds.getYsize() * attackerBounds.getZsize()
                    * 1.5;
            AABB targetBounds = target.getBoundingBox();
            double targetVolume = targetBounds.getXsize() * targetBounds.getYsize() * targetBounds.getZsize();

            if (attackerVolume < targetVolume) {
                float base = 0;
                for (var mod : stack.getAttributeModifiers().modifiers()) {
                    if (mod.attribute().is(Attributes.ATTACK_DAMAGE.getKey())
                            && mod.slot() == EquipmentSlotGroup.MAINHAND) {
                        base += mod.modifier().amount();
                    }
                }
                DamageSource source;
                if (attacker instanceof Player player) {
                    source = attacker.damageSources().playerAttack(player);
                } else {
                    source = attacker.damageSources().mobAttack(attacker);
                }

                target.hurt(source, damage + base);
            }
        };
    }
}
