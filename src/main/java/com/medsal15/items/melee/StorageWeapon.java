package com.medsal15.items.melee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.medsal15.client.tooltips.ContainerTooltip;
import com.medsal15.config.ConfigServer;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class StorageWeapon extends WeaponItem {
    public StorageWeapon(WeaponItem.Builder builder, Properties properties) {
        super(builder, properties);
    }

    @Override
    public boolean overrideStackedOnOther(@Nonnull ItemStack stack, @Nonnull Slot slot, @Nonnull ClickAction action,
            @Nonnull Player player) {
        if (stack.getCount() != 1 || !slot.allowModification(player) || action != ClickAction.SECONDARY)
            return false;

        ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
        if (contents == null)
            return false;

        ItemStack other = slot.getItem();
        if (other.is(ESTags.Items.TOOLBOX_FORBIDDEN))
            return false;
        for (TypedDataComponent<?> component : other.getComponents()) {
            if (ConfigServer.TOOLBOX_FORBIDDEN_COMPONENTS.get().contains(component.type().toString()))
                return false;
        }

        @SuppressWarnings("null")
        IItemHandler handler = Capabilities.ItemHandler.ITEM.getCapability(stack, null);
        if (handler == null)
            return false;

        boolean handled = false;
        for (int i = 0; i < handler.getSlots(); i++) {
            if (other.isEmpty()) {
                ItemStack stored = handler.extractItem(i, ABSOLUTE_MAX_STACK_SIZE, false);
                if (!stored.isEmpty()) {
                    handler.insertItem(i, slot.safeInsert(stored), false);
                    return true;
                }
            } else if (handler.isItemValid(i, other)) {
                ItemStack remaining = handler.insertItem(i, other, false);
                if (remaining.getCount() != other.getCount()) {
                    slot.setByPlayer(remaining);
                    other.setCount(remaining.getCount());
                    handled = true;
                }
                if (remaining.isEmpty()) {
                    return true;
                }
            }
        }

        return handled;
    }

    @Override
    public boolean overrideOtherStackedOnMe(@Nonnull ItemStack stack, @Nonnull ItemStack other, @Nonnull Slot slot,
            @Nonnull ClickAction action, @Nonnull Player player, @Nonnull SlotAccess access) {
        if (stack.getCount() != 1 || !slot.allowModification(player) || action != ClickAction.SECONDARY)
            return false;

        ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
        if (contents == null)
            return false;

        if (other.is(ESTags.Items.TOOLBOX_FORBIDDEN))
            return false;
        for (TypedDataComponent<?> component : other.getComponents()) {
            if (ConfigServer.TOOLBOX_FORBIDDEN_COMPONENTS.get().contains(component.type().toString()))
                return false;
        }

        @SuppressWarnings("null")
        IItemHandler handler = Capabilities.ItemHandler.ITEM.getCapability(stack, null);
        if (handler == null)
            return false;

        boolean handled = false;
        for (int i = 0; i < handler.getSlots(); i++) {
            if (other.isEmpty()) {
                ItemStack stored = handler.extractItem(i, ABSOLUTE_MAX_STACK_SIZE, false);
                if (!stored.isEmpty()) {
                    access.set(stored);
                    return true;
                }
            } else if (handler.isItemValid(i, other)) {
                ItemStack remaining = handler.insertItem(i, other, false);
                if (remaining.getCount() != other.getCount()) {
                    access.set(remaining);
                    other.setCount(remaining.getCount());
                    handled = true;
                }
                if (remaining.isEmpty()) {
                    return true;
                }
            }
        }

        return handled;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        @SuppressWarnings("null")
        IItemHandler handler = Capabilities.ItemHandler.ITEM.getCapability(stack, null);
        if (handler != null) {
            ItemStack first = ItemStack.EMPTY;
            for (int i = 0; i < handler.getSlots(); i++) {
                first = handler.extractItem(i, ABSOLUTE_MAX_STACK_SIZE, false);
                if (!first.isEmpty())
                    break;
            }

            if (!first.isEmpty()) {
                if (hand == InteractionHand.OFF_HAND && player.getMainHandItem().isEmpty()) {
                    player.setItemInHand(InteractionHand.MAIN_HAND, first);
                } else if (!player.getInventory().add(first))
                    player.drop(first, true);
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public void onDestroyed(@Nonnull ItemEntity itemEntity, @Nonnull DamageSource damageSource) {
        @SuppressWarnings("null")
        IItemHandler handler = Capabilities.ItemHandler.ITEM.getCapability(itemEntity.getItem(), null);
        if (handler != null) {
            List<ItemStack> contents = new ArrayList<>();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.extractItem(i, ABSOLUTE_MAX_STACK_SIZE, false);
                if (!stack.isEmpty())
                    contents.add(stack);
            }
            ItemUtils.onContainerDestroyed(itemEntity, contents);
        }
        super.onDestroyed(itemEntity, damageSource);
    }

    @Override
    public <T extends LivingEntity> int damageItem(@Nonnull ItemStack stack, int amount,
            @SuppressWarnings("null") @Nullable T entity, @Nonnull Consumer<Item> onBroken) {
        amount = super.damageItem(stack, amount, entity, onBroken);

        if (entity != null) {
            Integer damage = stack.get(DataComponents.DAMAGE);
            Integer maxDamage = stack.get(DataComponents.MAX_DAMAGE);
            if (damage != null && maxDamage != null && amount + damage >= maxDamage) {
                @SuppressWarnings("null")
                IItemHandler handler = Capabilities.ItemHandler.ITEM.getCapability(stack, null);
                if (handler != null) {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stored = handler.extractItem(i, ABSOLUTE_MAX_STACK_SIZE, false);
                        if (!stored.isEmpty()) {
                            ItemEntity itemEntity = new ItemEntity(entity.level(), entity.getX(), entity.getY(),
                                    entity.getZ(), stored);
                            itemEntity.setDeltaMovement(entity.level().getRandom().nextDouble() - 0.5,
                                    entity.getDeltaMovement().y, entity.level().getRandom().nextDouble() - 0.5);
                            itemEntity.setDefaultPickUpDelay();
                            entity.level().addFreshEntity(itemEntity);
                        }
                    }
                }
            }
        }

        return amount;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(@Nonnull ItemStack stack) {
        return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
                ? Optional.ofNullable(stack.get(DataComponents.CONTAINER)).map(ContainerTooltip::new)
                : Optional.empty();
    }

    public static class Container extends ItemStackHandler {
        private final ItemStack weapon;

        public Container(@Nonnull ItemStack weapon) {
            super();
            this.weapon = weapon;
            fillStacks(weapon);
        }

        public Container(@Nonnull ItemStack weapon, int size) {
            super(size);
            this.weapon = weapon;
            fillStacks(weapon);
        }

        private void fillStacks(@Nonnull ItemStack weapon) {
            ItemContainerContents contents = weapon.get(DataComponents.CONTAINER);
            if (contents != null) {
                for (int i = 0; i < contents.getSlots(); i++) {
                    ItemStack stored = contents.getStackInSlot(i);
                    if (!stored.isEmpty())
                        this.setStackInSlot(i, stored);
                }
            }
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);

            weapon.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(stacks));
        }
    }
}
