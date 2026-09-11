package com.medsal15.menus;

import javax.annotation.Nonnull;

import com.medsal15.blockentities.WirelessChargerBlockEntity;
import com.medsal15.blocks.ESBlocks;
import com.mraof.minestuck.api.uranium.UraniumPower;
import com.mraof.minestuck.inventory.ContainerHelper;
import com.mraof.minestuck.inventory.MachineContainerMenu;
import com.mraof.minestuck.inventory.slot.UraniumPowerSlot;

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
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

public class WirelessChargerMenu extends MachineContainerMenu {
    private static final int FUEL_X = 80;
    private static final int FUEL_Y = 35;

    private final DataSlot fuelHolder;
    private final DataSlot chargeHolder;

    public WirelessChargerMenu(int window, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(ESMenuTypes.WIRELESS_CHARGER.get(), window, playerInventory, new ItemStackHandler(1),
                DataSlot.standalone(), DataSlot.standalone(), ContainerLevelAccess.NULL, buffer.readBlockPos());
    }

    public WirelessChargerMenu(int window, Inventory playerInventory, IItemHandlerModifiable inventory,
            DataSlot fuelSlot, DataSlot chargeHolder, ContainerLevelAccess access, BlockPos pos) {
        this(ESMenuTypes.WIRELESS_CHARGER.get(), window, playerInventory, inventory, fuelSlot, chargeHolder, access,
                pos);
    }

    public WirelessChargerMenu(MenuType<? extends WirelessChargerMenu> type, int window, Inventory playerInventory,
            IItemHandlerModifiable inventory, DataSlot fuelHolder, DataSlot chargeHolder, ContainerLevelAccess access,
            BlockPos pos) {
        super(type, window, new SimpleContainerData(3), access, pos);

        assertItemHandlerSize(inventory, 1);
        this.fuelHolder = fuelHolder;
        this.chargeHolder = chargeHolder;

        addSlot(new UraniumPowerSlot(inventory, WirelessChargerBlockEntity.SLOT_FUEL, FUEL_X, FUEL_Y));
        addDataSlot(fuelHolder);
        addDataSlot(chargeHolder);
        ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
    }

    public int getFuel() {
        return fuelHolder.get();
    }

    public int getCharge() {
        return chargeHolder.get();
    }

    // MachineContainerMenu
    @Override
    protected Block getValidBlock() {
        return ESBlocks.WIRELESS_CHARGER.get();
    }

    // AbstractContainerMenu
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        int all = slots.size();

        if (slot.hasItem()) {
            ItemStack original = slot.getItem().copy();
            stack = original.copy();
            boolean result = false;

            if (index == WirelessChargerBlockEntity.SLOT_FUEL) {
                result = moveItemStackTo(original, 1, all, false);
            } else if (UraniumPower.hasUraniumPower(original)) {
                // send to fuel
                result = moveItemStackTo(original, 0, 1, false);
            }

            if (!result)
                return ItemStack.EMPTY;

            if (!ItemStack.matches(original, slot.getItem()))
                slot.set(original);
        }

        return stack;
    }
}
