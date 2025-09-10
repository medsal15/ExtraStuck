package com.medsal15.menus;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.medsal15.blockentities.ReactorBlockEntity;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.datamaps.ReactorFuel;
import com.medsal15.menus.slot.FilteredInputSlot;
import com.mraof.minestuck.inventory.ContainerHelper;
import com.mraof.minestuck.inventory.MachineContainerMenu;
import com.mraof.minestuck.inventory.slot.OutputSlot;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ReactorMenu extends MachineContainerMenu {
    private static int INPUT_X = 148;
    private static int INPUT_Y = 15;
    private static int OUTPUT_X = 148;
    private static int OUTPUT_Y = 45;

    private final DataSlot fuelHolder;
    private final DataSlot maxFuelHolder;
    private final DataSlot uraniumHolder;
    private final DataSlot chargeHolder;
    private final DataSlot fluidAmountHolder;

    public ReactorMenu(int window, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(ESMenuTypes.REACTOR.get(), window, playerInventory, new ItemStackHandler(2), DataSlot.standalone(),
                DataSlot.standalone(), DataSlot.standalone(), DataSlot.standalone(), DataSlot.standalone(),
                ContainerLevelAccess.NULL,
                buffer.readBlockPos());
    }

    public ReactorMenu(int window, Inventory playerInventory, IItemHandler inventory, DataSlot fuelHolder,
            DataSlot maxFuelHolder, DataSlot uraniumHolder, DataSlot chargeHolder, DataSlot fluidAmountHolder,
            ContainerLevelAccess access, BlockPos pos) {
        this(ESMenuTypes.REACTOR.get(), window, playerInventory, inventory, fuelHolder, maxFuelHolder, uraniumHolder,
                chargeHolder, fluidAmountHolder, access, pos);
    }

    public ReactorMenu(MenuType<? extends ReactorMenu> type, int window, Inventory playerInventory,
            IItemHandler inventory, DataSlot fuelHolder, DataSlot maxFuelHolder, DataSlot uraniumHolder,
            DataSlot chargeHolder,
            DataSlot fluidAmountHolder, ContainerLevelAccess access, BlockPos pos) {
        super(type, window, new SimpleContainerData(3), access, pos);

        assertItemHandlerSize(inventory, 2);
        this.fuelHolder = fuelHolder;
        this.maxFuelHolder = maxFuelHolder;
        this.uraniumHolder = uraniumHolder;
        this.chargeHolder = chargeHolder;
        this.fluidAmountHolder = fluidAmountHolder;

        addSlot(new FilteredInputSlot(inventory, ReactorBlockEntity.SLOT_FUEL_IN, INPUT_X, INPUT_Y, stack -> {
            Holder<Item> holder = stack.getItemHolder();
            @Nullable
            ReactorFuel data = holder.getData(ReactorFuel.REACTOR_MAP);
            return data != null;
        }));
        addSlot(new OutputSlot(inventory, ReactorBlockEntity.SLOT_FUEL_OUT, OUTPUT_X, OUTPUT_Y));
        addDataSlot(fuelHolder);
        addDataSlot(maxFuelHolder);
        addDataSlot(uraniumHolder);
        addDataSlot(chargeHolder);
        addDataSlot(fluidAmountHolder);

        ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
    }

    public int getFuel() {
        return fuelHolder.get();
    }

    public int getMaxFuel() {
        return maxFuelHolder.get();
    }

    public int getUranium() {
        return uraniumHolder.get();
    }

    public int getCharge() {
        return chargeHolder.get();
    }

    public int getFluidAmount() {
        return fluidAmountHolder.get();
    }

    @Override
    protected Block getValidBlock() {
        return ESBlocks.REACTOR.get();
    }

    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        int all = slots.size();

        if (slot.hasItem()) {
            ItemStack original = slot.getItem();
            stack = original.copy();
            boolean result = false;

            if (index <= 2) {
                result = moveItemStackTo(original, 2, all, false);
            } else if (original.getItem() == MSItems.ENERGY_CORE.get()) {
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
