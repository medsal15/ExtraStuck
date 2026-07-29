package com.medsal15.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.ESDamageTypes;
import com.medsal15.config.ConfigClient;
import com.medsal15.data.ESLangProvider;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.player.PlayerBoondollars;
import com.mraof.minestuck.player.PlayerData;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.registries.DeferredItem;

public class ESShield extends ShieldItem {
    private final Collection<IBlock> onBlock;
    @Nullable
    private final DeferredItem<Item> swapWith;
    private final Predicate<ItemStack> isRepairMaterial;
    private final Collection<Supplier<Component>> addedTooltip;
    private final BiFunction<ItemStack, Integer, Integer> damager;

    public ESShield(Properties properties) {
        super(properties);
        onBlock = new ArrayList<>();
        swapWith = null;
        isRepairMaterial = s -> false;
        addedTooltip = new ArrayList<>();
        damager = (s, i) -> i;
    }

    public ESShield(Builder builder, Properties properties) {
        super(properties);
        onBlock = builder.onBlock;
        swapWith = builder.swapWith;
        isRepairMaterial = builder.isRepairMaterial;
        addedTooltip = builder.addedTooltip;
        damager = builder.damager;
    }

    public boolean hasOnBlock(IBlock block) {
        return onBlock.contains(block);
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return isRepairMaterial.test(repair);
    }

    public void onShieldBlock(LivingShieldBlockEvent event) {
        ItemStack shield = event.getEntity().getUseItem();

        for (IBlock func : onBlock) {
            if (func.onBlock(event))
                return;
        }

        EquipmentSlot slot;
        if (event.getEntity().getItemBySlot(EquipmentSlot.MAINHAND).equals(shield)) {
            slot = EquipmentSlot.MAINHAND;
        } else {
            slot = EquipmentSlot.OFFHAND;
        }
        shield.hurtAndBreak((int) event.shieldDamage(), event.getEntity(), slot);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        DeferredItem<Item> next = swapWith;
        if (next != null && player.isShiftKeyDown()) {
            ItemStack swap = new ItemStack(next.getDelegate(), stack.getCount(), stack.getComponentsPatch());

            return InteractionResultHolder.success(swap);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        for (Supplier<Component> toAdd : addedTooltip) {
            Component line = toAdd.get();
            if (line != null)
                tooltipComponents.add(line);
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(@Nonnull ItemStack stack, int amount,
            @SuppressWarnings("null") @org.jetbrains.annotations.Nullable T entity, @Nonnull Consumer<Item> onBroken) {
        int baseDamage = super.damageItem(stack, amount, entity, onBroken);

        return damager.apply(stack, baseDamage);
    }

    public static class Builder {
        private final Collection<IBlock> onBlock = new ArrayList<>();
        @Nullable
        private DeferredItem<Item> swapWith;
        private Predicate<ItemStack> isRepairMaterial = s -> false;
        private final Collection<Supplier<Component>> addedTooltip = new ArrayList<>();
        private BiFunction<ItemStack, Integer, Integer> damager = (s, i) -> i;

        public Builder addBlock(IBlock onBlock) {
            this.onBlock.add(onBlock);
            return this;
        }

        public Builder setOther(DeferredItem<Item> other) {
            this.swapWith = other;
            return this;
        }

        public Builder setRepairMaterial(Predicate<ItemStack> repair) {
            this.isRepairMaterial = repair;
            return this;
        }

        public Builder addTooltipLine(Supplier<Component> line) {
            addedTooltip.add(line);
            return this;
        }

        /** Alters the shield's durability loss */
        public Builder setDamageMethod(BiFunction<ItemStack, Integer, Integer> damager) {
            this.damager = damager;
            return this;
        }
    }

    public static interface IBlock {
        /**
         * Deals with the shield blocking
         *
         * @return True if this should end the event
         */
        public boolean onBlock(LivingShieldBlockEvent event);

        public static IBlock damageFor(Supplier<Double> damage) {
            return event -> {
                float d = (float) ((double) damage.get());
                if (d <= 0)
                    return false;

                // Ensure the damage is melee and does not bypass shields
                DamageSource damageSource = event.getDamageSource();
                if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
                    return false;

                // Ensure the attacker exists and can be damaged
                Entity attacker = damageSource.getDirectEntity();
                if (attacker == null || !(attacker instanceof LivingEntity livingEntity))
                    return false;

                // Hurt them
                // This will crash at some point due to a null or whatever, no clue when or why
                Level level = event.getEntity().level();
                Reference<DamageType> type = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(ESDamageTypes.THORN_SHIELD);
                DamageSource retSource = new DamageSource(type, event.getEntity());
                livingEntity.hurt(retSource, d);
                return false;
            };
        }

        public static IBlock consumeBoondollars(Supplier<Long> costPerDamage) {
            return event -> {
                LivingEntity user = event.getEntity();
                // Only players get boondollars
                if (!(user instanceof ServerPlayer player))
                    return false;

                // Ensure the damage does not bypass shields
                DamageSource damageSource = event.getDamageSource();
                if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD))
                    return false;

                PlayerData playerData = PlayerData.get(player).orElse(null);
                if (playerData == null)
                    return false;

                long cost = (long) event.getBlockedDamage() * costPerDamage.get();
                if (!PlayerBoondollars.tryTakeBoondollars(playerData, cost, true))
                    return false;

                event.setShieldDamage(0);
                return false;
            };
        }

        public static IBlock bounceProjectiles(ProjectileDeflection deflection) {
            return e -> {
                // Ensure the damage does not bypass shields
                DamageSource damageSource = e.getDamageSource();
                if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD))
                    return false;

                Entity attacker = damageSource.getDirectEntity();
                if (attacker == null || !(attacker instanceof Projectile projectile))
                    return false;

                projectile.deflect(deflection, e.getEntity(), damageSource.getEntity(),
                        e.getEntity() instanceof Player);
                return true;
            };
        }

        public static boolean dropCandy(LivingShieldBlockEvent event) {
            // Ensure the damage is melee and does not bypass shields
            DamageSource damageSource = event.getDamageSource();
            if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
                return false;

            // Ensure the attacker exists and is an underling
            Entity attacker = damageSource.getEntity();
            if (attacker == null)
                return false;

            if (attacker instanceof UnderlingEntity underling)
                underling.dropCandy = true;

            return false;
        }

        public static IBlock replace(DeferredItem<Item> next, TagKey<DamageType> changer) {
            return event -> {
                DamageSource damageSource = event.getDamageSource();
                if (!damageSource.is(changer))
                    return false;

                LivingEntity entity = event.getEntity();
                entity.setItemInHand(entity.getUsedItemHand(), next.toStack());
                return true;
            };
        }

        public static IBlock burnFor(Supplier<Integer> ticks) {
            return event -> {
                int duration = ticks.get();
                if (duration <= 0)
                    return false;

                // Ensure the damage is melee and does not bypass shields
                DamageSource damageSource = event.getDamageSource();
                if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
                    return false;

                // Ensure the attacker exists and can be damaged
                Entity attacker = damageSource.getDirectEntity();
                if (attacker == null || !(attacker instanceof LivingEntity target))
                    return false;

                if (target.getRemainingFireTicks() < duration)
                    target.setRemainingFireTicks(duration);
                return false;
            };
        }

        public static boolean strongerKnockback(LivingShieldBlockEvent event) {
            // Ensure the damage is melee and does not bypass shields
            DamageSource damageSource = event.getDamageSource();
            if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
                return false;

            // Ensure the attacker exists and can be damaged
            Entity attacker = damageSource.getDirectEntity();
            if (attacker == null || !(attacker instanceof LivingEntity target))
                return false;

            float rot = event.getEntity().getYRot();
            target.knockback(1, Math.sin(rot / 180 * Math.PI), -Math.cos(rot / 180 * Math.PI));

            return false;
        }

        public static IBlock turn(float rot) {
            return event -> {
                // Ensure the damage is melee and does not bypass shields
                DamageSource damageSource = event.getDamageSource();
                if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
                    return false;

                // Ensure the attacker exists and can be damaged
                Entity attacker = damageSource.getDirectEntity();
                if (attacker == null)
                    return false;

                attacker.setYRot((attacker.getYRot() + rot) % 360);

                return false;
            };
        }

        /** Gives an effect to the user on blocking */
        public static IBlock gainEffect(Holder<MobEffect> effect, int duration) {
            return event -> {
                // Ensure the damage does not bypass shields
                DamageSource damageSource = event.getDamageSource();
                if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD))
                    return false;

                LivingEntity entity = event.getEntity();
                if (!entity.hasEffect(effect)) {
                    entity.addEffect(new MobEffectInstance(effect, duration));
                }
                return false;
            };
        }

        /**
         * Shoots a fireball forward
         */
        public static boolean shootFireball(LivingShieldBlockEvent event) {
            LivingEntity user = event.getEntity();
            ItemStack itemStack = user.getUseItem();
            Level level = user.level();
            level.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1f, .8f);

            if (!level.isClientSide && user instanceof Player player) {
                Vec3 sight = user.getViewVector(1F);
                Vec3 aim = new Vec3(sight.x * 2, sight.y * 2, sight.z * 2);
                LargeFireball fireball = new LargeFireball(level, user, aim.normalize(), 0);
                fireball.setPos(user.getX() + sight.x * .5, .5 + user.getY(.5), user.getZ() + sight.z * .5);
                level.addFreshEntity(fireball);

                player.getCooldowns().addCooldown(itemStack.getItem(), 20);
                itemStack.hurtAndBreak(2, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
            }

            return true;
        }

        public static IBlock selfDropChance(float chance, Supplier<String> message) {
            return event -> {
                LivingEntity user = event.getEntity();
                // Copied from minestuck
                if (user.getCommandSenderWorld().isClientSide || user.getRandom().nextFloat() >= chance)
                    return false;

                ItemStack stack = user.getUseItem();
                ItemEntity shield = new ItemEntity(user.level(), user.getX(), user.getY(), user.getZ(), stack.copy());
                shield.getItem().setCount(1);
                shield.setPickUpDelay(40);
                user.level().addFreshEntity(shield);
                stack.shrink(1);
                user.sendSystemMessage(Component.translatable(message.get()));

                return true;
            };
        }

        public static IBlock itemDropChance(Supplier<ItemStack> stack, float chance,
                Supplier<String> message) {
            return event -> {
                LivingEntity user = event.getEntity();
                if (user.getCommandSenderWorld().isClientSide || user.getRandom().nextFloat() >= chance)
                    return false;

                ItemStack itemStack = stack.get().copy();
                ItemEntity item = new ItemEntity(user.level(), user.getX(), user.getY(), user.getZ(), itemStack);
                user.level().addFreshEntity(item);
                user.sendSystemMessage(Component.translatable(message.get(), itemStack));

                return true;
            };
        }
    }

    public static Supplier<Component> damageLine(Supplier<Double> damage) {
        return () -> {
            if (!ConfigClient.displayShieldInfo)
                return null;
            return Component.translatable(ESLangProvider.SHIELD_DAMAGE_KEY, damage.get())
                    .withStyle(ChatFormatting.GRAY);
        };
    }

    public static BiFunction<ItemStack, Integer, Integer> consumeEnergy(Supplier<Integer> multiplier) {
        return (stack, damage) -> {
            @SuppressWarnings("null")
            IEnergyStorage energyStorage = Capabilities.EnergyStorage.ITEM.getCapability(stack, null);
            if (energyStorage != null && energyStorage.canExtract()) {
                int mult = multiplier.get();
                int blocked = Math.min(energyStorage.getEnergyStored() / mult, damage);
                int drain = blocked * mult;
                energyStorage.extractEnergy(drain, false);
                return Math.max(damage - blocked, 0);
            }
            return damage;
        };
    }
}
