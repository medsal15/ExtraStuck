package com.medsal15.items;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;

public interface IESEnergyStorage {
    public int getMaxEnergyStored(ItemStack stack);

    public int getEnergyStored(ItemStack stack);

    public boolean canExtract(ItemStack stack);

    public boolean canReceive(ItemStack stack);

    public int extractEnergy(ItemStack stack, int toExtract, boolean simulate);

    public int receiveEnergy(ItemStack stack, int toReceive, boolean simulate);

    public static class StackEnergyStorage
            implements IEnergyStorage, ICapabilityProvider<ItemStack, Void, IEnergyStorage> {
        private final ItemStack stack;

        public StackEnergyStorage(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public int getMaxEnergyStored() {
            if (stack.getItem() instanceof IESEnergyStorage storage)
                return storage.getMaxEnergyStored(stack);
            return 0;
        }

        @Override
        public int getEnergyStored() {
            if (stack.getItem() instanceof IESEnergyStorage storage)
                return storage.getEnergyStored(stack);
            return 0;
        }

        @Override
        public boolean canExtract() {
            if (stack.getItem() instanceof IESEnergyStorage storage)
                return storage.canExtract(stack);
            return false;
        }

        @Override
        public boolean canReceive() {
            if (stack.getItem() instanceof IESEnergyStorage storage)
                return storage.canReceive(stack);
            return false;
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            if (stack.getItem() instanceof IESEnergyStorage storage)
                return storage.extractEnergy(stack, toExtract, simulate);
            return 0;
        }

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            if (stack.getItem() instanceof IESEnergyStorage storage)
                return storage.receiveEnergy(stack, toReceive, simulate);
            return 0;
        }

        @Override
        public @Nonnull IEnergyStorage getCapability(@Nonnull ItemStack stack, @Nonnull Void unused) {
            return this;
        }
    }
}
