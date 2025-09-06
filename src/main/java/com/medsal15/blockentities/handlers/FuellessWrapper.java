package com.medsal15.blockentities.handlers;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

/**
 * RangedWrapper that prevents fuel extraction
 */
public class FuellessWrapper extends RangedWrapper {
    private int min;
    private int fuelSlot;

    public FuellessWrapper(IItemHandlerModifiable handler, int minSlot, int maxSlotExclusive, int fuelSlot) {
        super(handler, minSlot, maxSlotExclusive);

        this.min = minSlot;
        this.fuelSlot = fuelSlot;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot + min == fuelSlot)
            return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }
}
