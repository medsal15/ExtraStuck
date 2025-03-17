package com.medsal15.items.shields;

import javax.annotation.Nonnull;

import com.medsal15.items.ESItems;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

public class FluxShield extends ESShield implements IShieldBlock {
    /** Multiplier from damage to flux */
    public int mult;
    public int storage;

    public FluxShield(Properties properties, int mult, int storage) {
        super(properties);
        this.mult = mult;
        this.storage = storage;
    }

    public int getMaxEnergyStored(ItemStack stack) {
        return storage;
    }

    public int getEnergyStored(ItemStack stack) {
        return stack.getOrDefault(ESItems.ENERGY, 0);
    }

    public boolean canExtract(ItemStack stack) {
        var stored = stack.getOrDefault(ESItems.ENERGY, 0);
        return stored > 0;
    }

    public boolean canReceive(ItemStack stack) {
        var stored = stack.getOrDefault(ESItems.ENERGY, 0);
        var diff = storage - stored;
        return diff > 0;
    }

    public int extractEnergy(ItemStack stack, int toExtract, boolean simulate) {
        var stored = stack.getOrDefault(ESItems.ENERGY, 0);
        if (toExtract > stored)
            toExtract = stored;

        if (!simulate) {
            stack.set(ESItems.ENERGY, stored - toExtract);
        }

        return toExtract;
    }

    public int receiveEnergy(ItemStack stack, int toReceive, boolean simulate) {
        var stored = stack.getOrDefault(ESItems.ENERGY, 0);
        if (toReceive + stored > storage)
            toReceive = storage - stored;

        if (!simulate) {
            stack.set(ESItems.ENERGY, stored + toReceive);
        }

        return toReceive;
    }

    @Override
    public boolean onShieldBlock(LivingShieldBlockEvent event) {
        var useItem = event.getEntity().getUseItem();
        if (!useItem.has(ESItems.ENERGY.get()) || mult <= 0)
            return false;

        // Ensure the damage does not bypass shields
        var damageSource = event.getDamageSource();
        if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD))
            return false;

        // Drain energy
        var drain = (int) (event.getBlockedDamage() * mult);
        if (!(useItem.getItem() instanceof FluxShield))
            return false;

        var extracted = extractEnergy(useItem, drain, false);
        if (extracted > 0) {
            event.setShieldDamage(0);
        }

        return true;
    }

    public static class StackEnergyStorage
            implements IEnergyStorage, ICapabilityProvider<ItemStack, Void, IEnergyStorage> {
        private final ItemStack stack;

        public StackEnergyStorage(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public int getMaxEnergyStored() {
            if (stack.getItem() instanceof FluxShield shield)
                return shield.getMaxEnergyStored(stack);
            return 0;
        }

        @Override
        public int getEnergyStored() {
            if (stack.getItem() instanceof FluxShield shield)
                return shield.getEnergyStored(stack);
            return 0;
        }

        @Override
        public boolean canExtract() {
            if (stack.getItem() instanceof FluxShield shield)
                return shield.canExtract(stack);
            return false;
        }

        @Override
        public boolean canReceive() {
            if (stack.getItem() instanceof FluxShield shield)
                return shield.canReceive(stack);
            return false;
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            if (stack.getItem() instanceof FluxShield shield)
                return shield.extractEnergy(stack, toExtract, simulate);
            return 0;
        }

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            if (stack.getItem() instanceof FluxShield shield)
                return shield.receiveEnergy(stack, toReceive, simulate);
            return 0;
        }

        @Override
        public @Nonnull IEnergyStorage getCapability(@Nonnull ItemStack stack, @Nonnull Void unused) {
            return this;
        }
    }
}
