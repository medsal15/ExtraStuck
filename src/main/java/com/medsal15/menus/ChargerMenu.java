package com.medsal15.menus;

import javax.annotation.Nonnull;

import com.medsal15.blockentities.ChargerBlockEntity;
import com.medsal15.blocks.ESBlocks;
import com.mraof.minestuck.inventory.ContainerHelper;
import com.mraof.minestuck.inventory.MachineContainerMenu;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

public class ChargerMenu extends MachineContainerMenu {
    private static final int INPUT_X = 80;
    private static final int INPUT_Y = 23;
    private static final int FUEL_X = 80;
    private static final int FUEL_Y = 49;

    private final DataSlot fuelHolder;
    private final DataSlot chargeHolder;
    private final DataSlot modeHolder;

    public ChargerMenu(int window, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(ESMenuTypes.CHARGER.get(), window, playerInventory, new ItemStackHandler(2),
                DataSlot.standalone(), DataSlot.standalone(), DataSlot.standalone(), ContainerLevelAccess.NULL,
                buffer.readBlockPos());
    }

    public ChargerMenu(int window, Inventory playerInventory, IItemHandlerModifiable inventory,
            DataSlot fuelHolder, DataSlot chargeHolder, DataSlot modeHolder, ContainerLevelAccess access,
            BlockPos pos) {
        this(ESMenuTypes.CHARGER.get(), window, playerInventory, inventory, fuelHolder, chargeHolder, modeHolder,
                access, pos);
    }

    public ChargerMenu(MenuType<? extends ChargerMenu> type, int window, Inventory playerInventory,
            IItemHandlerModifiable inventory, DataSlot fuelHolder, DataSlot chargeHolder, DataSlot modeHolder,
            ContainerLevelAccess access, BlockPos pos) {
        // Required, despite not being used here
        super(type, window, new SimpleContainerData(3), access, pos);

        assertItemHandlerSize(inventory, 2);
        this.fuelHolder = fuelHolder;
        this.chargeHolder = chargeHolder;
        this.modeHolder = modeHolder;

        addSlot(new SlotItemHandler(
                new RangedWrapper(inventory, ChargerBlockEntity.SLOT_IN, ChargerBlockEntity.SLOT_IN + 1),
                ChargerBlockEntity.SLOT_IN, INPUT_X, INPUT_Y));
        addSlot(new InputSlot(inventory, ChargerBlockEntity.SLOT_FUEL, FUEL_X, FUEL_Y, MSItems.RAW_URANIUM.get()));
        addDataSlot(fuelHolder);
        addDataSlot(chargeHolder);
        addDataSlot(modeHolder);

        ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
    }

    public int getFuel() {
        return fuelHolder.get();
    }

    public int getCharge() {
        return chargeHolder.get();
    }

    public boolean getMode() {
        return modeHolder.get() == 1;
    }

    public void setMode(int mode) {
        modeHolder.set(mode);
    }

    // MachineContainerMenu
    @Override
    protected Block getValidBlock() {
        return ESBlocks.CHARGER.get();
    }

    // AbstractContainerMenu
    @SuppressWarnings("null")
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        int all = slots.size();

        if (slot.hasItem()) {
            ItemStack original = slot.getItem().copy();
            stack = original.copy();
            boolean result = false;

            if (index == ChargerBlockEntity.SLOT_IN
                    || index == ChargerBlockEntity.SLOT_FUEL) {
                result = moveItemStackTo(original, 2, all, false);
            } else {
                if (Capabilities.EnergyStorage.ITEM.getCapability(stack, null) != null) {
                    result = moveItemStackTo(original, 0, 1, false);
                } else if (original.getItem() == MSItems.RAW_URANIUM.asItem()) {
                    // send to fuel
                    result = moveItemStackTo(original, 1, 2, false);
                }
            }

            if (!result)
                return ItemStack.EMPTY;

            if (!ItemStack.matches(original, slot.getItem()))
                slot.set(original);
        }

        return stack;
    }
}
