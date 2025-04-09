package com.medsal15.items.shields;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.ESDamageTypes;
import com.medsal15.data.ESLangProvider;
import com.medsal15.items.ESDataComponents;
import com.medsal15.items.IESEnergyStorage;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.player.PlayerBoondollars;
import com.mraof.minestuck.player.PlayerData;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.registries.DeferredItem;

public class ESShield extends ShieldItem implements IESEnergyStorage {
    @Nullable
    private Collection<IBlock> onBlock;
    @Nullable
    private DeferredItem<Item> swapWith;

    public ESShield(Properties properties) {
        super(properties);
    }

    public ESShield(Properties properties, DeferredItem<Item> swapItem) {
        super(properties);

        swapWith = swapItem;
    }

    public ESShield(Properties properties, IBlock... blocks) {
        super(properties);

        onBlock = List.of(blocks);
    }

    public ESShield(Properties properties, DeferredItem<Item> swapItem, IBlock... blocks) {
        super(properties);

        swapWith = swapItem;
        onBlock = List.of(blocks);
    }

    public boolean hasOnBlock(IBlock block) {
        return onBlock != null && onBlock.contains(block);
    }

    public int getMaxEnergyStored(ItemStack stack) {
        return stack.get(ESDataComponents.ENERGY_STORAGE);
    }

    public int getEnergyStored(ItemStack stack) {
        return stack.get(ESDataComponents.ENERGY);
    }

    public boolean canExtract(ItemStack stack) {
        var stored = stack.get(ESDataComponents.ENERGY);
        return stored > 0;
    }

    public boolean canReceive(ItemStack stack) {
        var stored = stack.get(ESDataComponents.ENERGY);
        var storage = stack.get(ESDataComponents.ENERGY_STORAGE);
        var diff = storage - stored;
        return diff > 0;
    }

    public int extractEnergy(ItemStack stack, int toExtract, boolean simulate) {
        var stored = stack.get(ESDataComponents.ENERGY);
        if (toExtract > stored)
            toExtract = stored;

        if (!simulate) {
            stack.set(ESDataComponents.ENERGY, stored - toExtract);
        }

        return toExtract;
    }

    public int receiveEnergy(ItemStack stack, int toReceive, boolean simulate) {
        var stored = stack.get(ESDataComponents.ENERGY);
        var storage = stack.get(ESDataComponents.ENERGY_STORAGE);
        if (toReceive + stored > storage)
            toReceive = storage - stored;

        if (!simulate) {
            stack.set(ESDataComponents.ENERGY, stored + toReceive);
        }

        return toReceive;
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return false;
    }

    public void onShieldBlock(LivingShieldBlockEvent event) {
        var ob = onBlock;
        if (ob == null || ob.size() == 0)
            return;

        for (var func : ob) {
            if (func.onBlock(event))
                return;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        var next = swapWith;
        if (next != null && player.isShiftKeyDown()) {
            ItemStack swap = new ItemStack(next.getDelegate(), stack.getCount(), stack.getComponentsPatch());

            return InteractionResultHolder.success(swap);
        }
        return super.use(level, player, hand);
    }

    public static interface IBlock {
        /**
         * Deals with the shield blocking
         *
         * @return True if this should end the event
         */
        public boolean onBlock(LivingShieldBlockEvent event);
    }

    /** Holds the different onblock functions */
    public static final class BlockFuncs {
        // #region DAMAGE
        // Must be a value so it can be equal to itself
        public static ESShield.IBlock DAMAGE = event -> {
            var useItem = event.getEntity().getUseItem();
            if (!useItem.has(ESDataComponents.SHIELD_DAMAGE))
                return false;

            var damage = useItem.get(ESDataComponents.SHIELD_DAMAGE);
            if (damage <= 0)
                return false;

            // Ensure the damage is melee and does not bypass shields
            var damageSource = event.getDamageSource();
            if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
                return false;

            // Ensure the attacker exists and can be damaged
            var attacker = damageSource.getDirectEntity();
            if (attacker == null || !(attacker instanceof LivingEntity livingEntity))
                return false;

            // Hurt them
            // This will crash at some point due to a null or whatever, no clue when or why
            var level = event.getEntity().level();
            var type = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(ESDamageTypes.THORN_SHIELD);
            var retSource = new DamageSource(type, event.getEntity());
            livingEntity.hurt(retSource, damage);
            return false;
        };
        // #endregion DAMAGE

        // #region USE_POWER
        public static ESShield.IBlock USE_POWER = event -> {
            var useItem = event.getEntity().getUseItem();
            if (!useItem.has(ESDataComponents.ENERGY) || !useItem.has(ESDataComponents.FLUX_MULTIPLIER))
                return false;

            var item = useItem.getItem();
            if (!(item instanceof ESShield shield))
                return false;

            var mult = useItem.get(ESDataComponents.FLUX_MULTIPLIER);
            if (mult < 0)
                mult = 0;

            // Ensure the damage does not bypass shields
            var damageSource = event.getDamageSource();
            if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD))
                return false;

            // Drain energy
            var drain = (int) (event.getBlockedDamage() * mult);
            var extracted = shield.extractEnergy(useItem, drain, false);
            if (extracted > 0) {
                event.setShieldDamage(0);
            }

            return false;
        };
        // #endregion USE_POWER

        public static boolean consumeBoondollars(LivingShieldBlockEvent event) {
            var user = event.getEntity();
            // Only players get boondollars
            if (!(user instanceof ServerPlayer player))
                return false;

            // Ensure the damage does not bypass shields
            var damageSource = event.getDamageSource();
            if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD))
                return false;

            var oPlayerData = PlayerData.get(player);
            if (!oPlayerData.isPresent())
                return false;
            var playerData = oPlayerData.get();
            if (!PlayerBoondollars.tryTakeBoondollars(playerData, (long) event.getBlockedDamage()))
                return false;

            event.setShieldDamage(0);
            return false;
        }

        public static ESShield.IBlock bounceProjectiles(ProjectileDeflection deflection) {
            return e -> {
                // Ensure the damage does not bypass shields
                var damageSource = e.getDamageSource();
                if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD))
                    return false;

                var attacker = damageSource.getDirectEntity();
                if (attacker == null || !(attacker instanceof Projectile projectile))
                    return false;

                projectile.deflect(deflection, e.getEntity(), damageSource.getEntity(),
                        e.getEntity() instanceof Player);
                return true;
            };
        }

        public static boolean dropCandy(LivingShieldBlockEvent event) {
            // Ensure the damage is melee and does not bypass shields
            var damageSource = event.getDamageSource();
            if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
                return false;

            // Ensure the attacker exists and is an underling
            var attacker = damageSource.getEntity();
            if (attacker == null)
                return false;

            if (attacker instanceof UnderlingEntity underling)
                underling.dropCandy = true;

            return false;
        }

        public static ESShield.IBlock replace(DeferredItem<Item> next, TagKey<DamageType> changer) {
            return event -> {
                var damageSource = event.getDamageSource();
                if (!damageSource.is(changer))
                    return false;

                var entity = event.getEntity();
                entity.setItemInHand(entity.getUsedItemHand(), next.toStack());
                return true;
            };
        }

        public static boolean burn(LivingShieldBlockEvent event) {
            var useItem = event.getEntity().getUseItem();
            if (!useItem.has(ESDataComponents.BURN_DURATION.get()))
                return false;
            var duration = useItem.get(ESDataComponents.BURN_DURATION);
            if (duration <= 0)
                return false;

            // Ensure the damage is melee and does not bypass shields
            var damageSource = event.getDamageSource();
            if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
                return false;

            // Ensure the attacker exists and can be damaged
            var attacker = damageSource.getDirectEntity();
            if (attacker == null || !(attacker instanceof LivingEntity target))
                return false;

            if (target.getRemainingFireTicks() < duration)
                target.setRemainingFireTicks(duration);
            return false;
        }

        public static boolean strongerKnockback(LivingShieldBlockEvent event) {
            // Ensure the damage is melee and does not bypass shields
            var damageSource = event.getDamageSource();
            if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
                return false;

            // Ensure the attacker exists and can be damaged
            var attacker = damageSource.getDirectEntity();
            if (attacker == null || !(attacker instanceof LivingEntity target))
                return false;

            var rot = event.getEntity().getYRot();
            target.knockback(1, Math.sin(rot / 180 * Math.PI), -Math.cos(rot / 180 * Math.PI));

            return false;
        }

        public static ESShield.IBlock turn(float rot) {
            return event -> {
                // Ensure the damage is melee and does not bypass shields
                var damageSource = event.getDamageSource();
                if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
                    return false;

                // Ensure the attacker exists and can be damaged
                var attacker = damageSource.getDirectEntity();
                if (attacker == null)
                    return false;

                attacker.setYRot((attacker.getYRot() + rot) % 360);

                return false;
            };
        }

        /** Gives an effect to the user on blocking */
        public static ESShield.IBlock gainEffect(Holder<MobEffect> effect, int duration) {
            return event -> {
                // Ensure the damage does not bypass shields
                var damageSource = event.getDamageSource();
                if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD))
                    return false;

                var entity = event.getEntity();
                if (!entity.hasEffect(effect)) {
                    entity.addEffect(new MobEffectInstance(effect, duration));
                }
                return false;
            };
        }

        public static ESShield.IBlock dropChance(float chance) {
            return event -> {
                var user = event.getEntity();
                // Copied from minestuck
                if (user.getCommandSenderWorld().isClientSide || user.getRandom().nextFloat() >= chance)
                    return false;

                var stack = user.getUseItem();
                ItemEntity shield = new ItemEntity(user.level(), user.getX(), user.getY(), user.getZ(), stack.copy());
                shield.getItem().setCount(1);
                shield.setPickUpDelay(40);
                user.level().addFreshEntity(shield);
                stack.shrink(1);
                user.sendSystemMessage(Component.translatable(ESLangProvider.SLIED_DROP_KEY));

                return true;
            };
        }
    }
}
