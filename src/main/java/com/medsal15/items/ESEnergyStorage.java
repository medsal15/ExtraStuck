package com.medsal15.items;

import static com.medsal15.items.ESDataComponents.ENERGY;
import static com.medsal15.items.ESDataComponents.ENERGY_STORAGE;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ESEnergyStorage implements IEnergyStorage, ICapabilityProvider<ItemStack, Void, IEnergyStorage> {
    private final ItemStack stack;

    public ESEnergyStorage(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public int getMaxEnergyStored() {
        if (stack.has(ENERGY_STORAGE)) {
            return stack.get(ENERGY_STORAGE);
        }
        return 0;
    }

    @Override
    public int getEnergyStored() {
        if (stack.has(ENERGY)) {
            return stack.get(ENERGY);
        }
        return 0;
    }

    @Override
    public boolean canExtract() {
        return stack.has(ENERGY) || stack.has(ENERGY_STORAGE);
    }

    @Override
    public boolean canReceive() {
        return stack.has(ENERGY_STORAGE);
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        if (stack.has(ENERGY)) {
            int stored = stack.get(ENERGY);
            int energy = Math.min(toExtract, stack.get(ENERGY));

            if (!simulate) {
                int left = stored - energy;
                stack.set(ENERGY, left);
            }

            return energy;
        }
        return 0;
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        if (stack.has(ENERGY_STORAGE)) {
            int max = stack.get(ENERGY_STORAGE);
            int stored = stack.getOrDefault(ENERGY, 0);
            int charge = Math.min(toReceive, max - stored);

            if (!simulate) {
                stack.set(ENERGY, Math.min(max, stored + charge));
            }

            return charge;
        }
        return 0;
    }

    @Override
    public @Nullable IEnergyStorage getCapability(@Nonnull ItemStack object, @Nonnull Void context) {
        return this;
    }
}
