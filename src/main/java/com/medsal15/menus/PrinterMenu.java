package com.medsal15.menus;

import javax.annotation.Nonnull;

import com.medsal15.blocks.ESBlocks;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.machine.MiniAlchemiterBlockEntity;
import com.mraof.minestuck.inventory.ContainerHelper;
import com.mraof.minestuck.inventory.MachineContainerMenu;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.inventory.slot.OutputSlot;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class PrinterMenu extends MachineContainerMenu {
    private static final int INPUT_X = 27;
    private static final int INPUT_Y = 20;
    private static final int OUTPUT_X = 135;
    private static final int OUTPUT_Y = 20;

    private final DataSlot wildcardHolder;

    public PrinterMenu(int window, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(ESMenuTypes.PRINTER.get(), window, playerInventory, new ItemStackHandler(2), new SimpleContainerData(3),
                DataSlot.standalone(), ContainerLevelAccess.NULL, buffer.readBlockPos());
    }

    public PrinterMenu(int window, Inventory playerInventory, IItemHandler inventory, ContainerData parameters,
            DataSlot wildcardHolder, ContainerLevelAccess access, BlockPos pos) {
        this(ESMenuTypes.PRINTER.get(), window, playerInventory, inventory, parameters, wildcardHolder, access, pos);
    }

    public PrinterMenu(MenuType<? extends PrinterMenu> type, int window, Inventory playerInventory,
            IItemHandler inventory, ContainerData parameters, DataSlot wildcardHolder, ContainerLevelAccess access,
            BlockPos pos) {
        super(type, window, parameters, access, pos);

        assertItemHandlerSize(inventory, 2);
        this.wildcardHolder = wildcardHolder;

        addSlot(new InputSlot(inventory, MiniAlchemiterBlockEntity.INPUT, INPUT_X, INPUT_Y,
                MSBlocks.CRUXITE_DOWEL.get().asItem()));
        addSlot(new OutputSlot(inventory, MiniAlchemiterBlockEntity.OUTPUT, OUTPUT_X, OUTPUT_Y));
        addDataSlot(wildcardHolder);

        ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
    }

    public GristType getWildcardType() {
        GristType grist = GristTypes.REGISTRY.byId(wildcardHolder.get());
        if (grist == null)
            grist = GristTypes.BUILD.get();
        return grist;
    }

    // MachineContainerMenu
    @Override
    protected Block getValidBlock() {
        return ESBlocks.PRINTER.get();
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

            if (index <= 1) {
                // Machine slot
                result = moveItemStackTo(original, 2, all, false);
            } else {
                // Inventory slot
                if (original.getItem() == MSBlocks.CRUXITE_DOWEL.asItem()) {
                    result = moveItemStackTo(original, 0, 1, false);
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
