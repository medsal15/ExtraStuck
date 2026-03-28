package com.medsal15.items.weaponeffects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.medsal15.compat.irons_spellbooks.items.ESISSMissingItems;
import com.medsal15.config.ConfigServer;
import com.medsal15.data.ESLootTableProvider.TableSubProvider;
import com.medsal15.items.ESItems;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.components.SteamFuelComponent;
import com.medsal15.loot.modifiers.ESLootModifier;
import com.mraof.minestuck.client.util.MagicEffect;
import com.mraof.minestuck.client.util.MagicEffect.AOEType;
import com.mraof.minestuck.client.util.MagicEffect.RangedType;
import com.mraof.minestuck.item.weapon.ItemRightClickEffect;
import com.mraof.minestuck.item.weapon.MagicAOERightClickEffect;
import com.mraof.minestuck.item.weapon.MagicRangedRightClickEffect;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.util.MSAttachments;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;

public final class ESRightClickEffects {
    /**
     * Allows toggling the steam weapon's fire by crouch using
     */
    public static InteractionResultHolder<ItemStack> steamWeapon(Level level, Player player,
            InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Toggle
        if (player.isShiftKeyDown()) {
            SteamFuelComponent fuel = stack.getOrDefault(ESDataComponents.STEAM_FUEL, SteamFuelComponent.empty());
            if (fuel.burning()) {
                stack.set(ESDataComponents.STEAM_FUEL, fuel.extinguish());
                player.playSound(SoundEvents.FIRE_EXTINGUISH);
                return InteractionResultHolder.success(stack);
            }
            int fuel_needed = ConfigServer.STEAM_FUEL_CONSUME.get();
            if (fuel.fuel() >= fuel_needed) {
                stack.set(ESDataComponents.STEAM_FUEL, fuel.toggle(true));
                player.playSound(SoundEvents.FLINTANDSTEEL_USE);
                return InteractionResultHolder.success(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    public static final MagicRangedRightClickEffect CASH_MAGIC = new CashMagicRangedEffect(64, 4, null, null, 1F,
            MagicEffect.RangedType.GREEN);

    public static final MagicAOERightClickEffect CURSED_STAFF_MAGIC = new MagicAOEEffect(4, 5, AOEType.ENCHANT,
            e -> e.addEffect(new MobEffectInstance(MobEffects.POISON, 120)));

    public static final MagicRangedRightClickEffect BLESSED_STAFF_MAGIC = new MagicRightClickEffect(16, 5, null,
            () -> SoundEvents.AMETHYST_BLOCK_CHIME, 1F, RangedType.ENCHANT);

    public static final MagicRangedRightClickEffect BRANCH_OF_YGGDRASIL_MAGIC = new MagicRightClickEffect(24, 8,
            () -> new MobEffectInstance(MobEffects.LEVITATION, 200), () -> SoundEvents.BLAZE_SHOOT, 1F,
            RangedType.ECHIDNA);

    public static final MagicRangedRightClickEffect STAFF_OF_YGGDRASIL_MAGIC = new YggdrasilMagicRangedEffect(24, 8,
            () -> new MobEffectInstance(MobEffects.LEVITATION, 200), () -> SoundEvents.BLAZE_SHOOT, 1F,
            RangedType.ECHIDNA);

    /**
     * Switches to Cast Gold Shield when holding shift, shoots a fireball otherwise
     */
    public static InteractionResultHolder<ItemStack> prospitianWand(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            return ItemRightClickEffect.switchTo(ESISSMissingItems.CAST_GOLD_SHIELD).onRightClick(level, player, hand);
        } else {
            ItemStack stack = player.getItemInHand(hand);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLAZE_SHOOT,
                    SoundSource.PLAYERS, 1.0F, 0.8F);

            if (!level.isClientSide) {
                Vec3 sight = player.getViewVector(1F);
                Vec3 aim = new Vec3(sight.x * 2, sight.y * 2, sight.z * 2);
                LargeFireball fireball = new LargeFireball(level, player, aim.normalize(), 1);
                fireball.setPos(player.getX() + sight.x, .5 + player.getY(.5), player.getZ() + sight.z);
                level.addFreshEntity(fireball);

                player.getCooldowns().addCooldown(stack.getItem(), 20);
                stack.hurtAndBreak(2, player, LivingEntity.getSlotForHand(hand));
            }

            return InteractionResultHolder.pass(stack);
        }
    }

    /**
     * Switches to Amethyst Backstabber when holding shift, ??? otherwise
     */
    public static InteractionResultHolder<ItemStack> dersiteWand(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            return ItemRightClickEffect.switchTo(ESISSMissingItems.AMETHYST_BACKSTABBER).onRightClick(level, player,
                    hand);
        } else {
            ItemStack stack = player.getItemInHand(hand);

            if (!player.hasEffect(MobEffects.INVISIBILITY) && player instanceof ServerPlayer serverPlayer) {
                int duration = 200;
                if (player.isCreative() || Title.isPlayerOfAspect(serverPlayer, EnumAspect.VOID))
                    duration = 400;
                player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, duration));
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }

            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
    }

    public static ItemRightClickEffect healNearby(int radius) {
        return (level, player, hand) -> {
            ItemStack stack = player.getItemInHand(hand);

            AABB box = new AABB(player.blockPosition()).inflate(radius);
            List<Entity> targets = level.getEntities(player, box);
            targets.add(player);

            for (Entity entity : targets) {
                if (!(entity instanceof LivingEntity livingEntity))
                    continue;

                boolean canHeal = entity == player ||
                        (entity instanceof TamableAnimal tamableAnimal && tamableAnimal.isOwnedBy(player)) ||
                        player.getTeam() == null ||
                        entity.isAlliedTo(player);

                if (canHeal) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200));
                }
            }

            stack.hurtAndBreak(radius * 10, player, LivingEntity.getSlotForHand(hand));

            player.getCooldowns().addCooldown(stack.getItem(), 300);

            return InteractionResultHolder.pass(stack);
        };
    }

    /**
     * Swaps to a Heartstabber when holding shift, heals 2 hearts at the cost of 50
     * durability otherwise
     *
     * @param level
     * @param player
     * @param hand
     * @return
     */
    public static InteractionResultHolder<ItemStack> twoOfHearts(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            return ItemRightClickEffect.switchTo(ESItems.HEARTSTABBER).onRightClick(level, player, hand);
        } else {
            ItemStack stack = player.getItemInHand(hand);

            if (player instanceof ServerPlayer) {
                player.heal(4);
                stack.hurtAndBreak(25, player, LivingEntity.getSlotForHand(hand));
            }

            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
    }

    /**
     * Swaps to a Shinebreaker when holding shift, gives 2-2000 (by default)
     * boondollars at the cost of 50 durability otherwise
     *
     * @param level
     * @param player
     * @param hand
     * @return
     */
    public static InteractionResultHolder<ItemStack> twoOfDiamonds(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            return ItemRightClickEffect.switchTo(ESItems.SHINEBREAKER).onRightClick(level, player, hand);
        } else {
            ItemStack stack = player.getItemInHand(hand);

            if (level instanceof ServerLevel serverLevel) {
                List<ItemStack> loot = ESLootModifier.runTable(serverLevel,
                        TableSubProvider.TWO_OF_DIAMONDS.location());
                for (ItemStack lootStack : loot) {
                    if (!player.getInventory().add(lootStack))
                        player.drop(lootStack, false);
                }
                if (loot.size() > 0)
                    stack.hurtAndBreak(25, player, LivingEntity.getSlotForHand(hand));
            }

            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
    }

    /**
     * Swaps to a Shinebreaker when holding shift, gives 2-2000 (by default)
     * boondollars at the cost of 50 durability otherwise
     *
     * @param level
     * @param player
     * @param hand
     * @return
     */
    public static InteractionResultHolder<ItemStack> twoOfSpades(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            return ItemRightClickEffect.switchTo(ESItems.EXPLOSIVE_SCOOP).onRightClick(level, player, hand);
        } else {
            ItemStack stack = player.getItemInHand(hand);

            if (level instanceof ServerLevel serverLevel) {
                List<ItemStack> loot = ESLootModifier.runTable(serverLevel,
                        TableSubProvider.TWO_OF_SPADES.location());
                for (ItemStack lootStack : loot) {
                    if (!player.getInventory().add(lootStack))
                        player.drop(lootStack, false);
                }
                if (loot.size() > 0)
                    stack.hurtAndBreak(25, player, LivingEntity.getSlotForHand(hand));
            }

            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
    }

    /**
     * Increases spell damage by log10 player boondollars
     */
    private static class CashMagicRangedEffect extends MagicRightClickEffect {
        public CashMagicRangedEffect(int distance, int damage, Supplier<MobEffectInstance> effect,
                Supplier<SoundEvent> sound, float pitch, @Nullable MagicEffect.RangedType type) {
            super(distance, damage, effect, sound, pitch, type);
        }

        @Override
        protected float calculateDamage(ServerPlayer player, LivingEntity target, int damage) {
            if (!(player instanceof FakePlayer)) {
                Optional<PlayerData> odata = PlayerData.get(player);
                if (odata.isPresent()) {
                    PlayerData data = odata.get();
                    long boondollars = data.getData(MSAttachments.BOONDOLLARS);
                    if (boondollars > 0)
                        return (float) damage + (float) Math.log10(boondollars);
                }
            }
            return (float) damage;
        }
    }

    /**
     * Increases spell damage by sum of negative effect levels
     */
    private static class YggdrasilMagicRangedEffect extends MagicRightClickEffect {
        public YggdrasilMagicRangedEffect(int distance, int damage, Supplier<MobEffectInstance> effect,
                Supplier<SoundEvent> sound, float pitch, @Nullable MagicEffect.RangedType type) {
            super(distance, damage, effect, sound, pitch, type);
        }

        @Override
        protected float calculateDamage(ServerPlayer player, LivingEntity target, int damage) {
            List<MobEffectInstance> effects = new ArrayList<>(target.getActiveEffects());
            effects.removeIf(
                    effect -> effect.getEffect().value().getCategory() != MobEffectCategory.HARMFUL
                            || effect.getEffect().value().isInstantenous());
            damage += effects.stream().map(eff -> eff.getAmplifier() + 1).reduce((sum, n) -> sum + n).orElse(0);
            return super.calculateDamage(player, target, damage);
        }
    }

    private static class MagicAOEEffect extends MagicAOERightClickEffect {
        public MagicAOEEffect(float radius, int damage, @Nullable AOEType type) {
            super(radius, damage, type);
        }

        public MagicAOEEffect(float radius, int damage, @Nullable AOEType type, Consumer<LivingEntity> targetEffect) {
            super(radius, damage, type, targetEffect);
        }
    }

    private static class MagicRightClickEffect extends MagicRangedRightClickEffect {
        public MagicRightClickEffect(int distance, int damage, Supplier<MobEffectInstance> effect,
                Supplier<SoundEvent> sound, float pitch, @Nullable MagicEffect.RangedType type) {
            super(distance, damage, effect, sound, pitch, type);
        }
    }
}
