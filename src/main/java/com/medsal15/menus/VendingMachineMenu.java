package com.medsal15.menus;

import javax.annotation.Nonnull;

import com.medsal15.blockentities.VendingMachineBlockEntity;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.menus.slot.SellSlot;
import com.mraof.minestuck.inventory.ContainerHelper;
import com.mraof.minestuck.inventory.MachineContainerMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public final class VendingMachineMenu {
    public static class Sell extends MachineContainerMenu {
        private static final int SELL_X = 80;
        private static final int SELL_Y = 35;

        private final DataSlot costHolder;

        public Sell(int window, Inventory playerInventory, FriendlyByteBuf buffer) {
            this(ESMenuTypes.VENDING_MACHINE_SELL.get(), window, playerInventory, new ItemStackHandler(3),
                    DataSlot.standalone(), ContainerLevelAccess.NULL, buffer.readBlockPos());
        }

        public Sell(int window, Inventory playerInventory, IItemHandlerModifiable inventory, DataSlot costHolder,
                ContainerLevelAccess access, BlockPos pos) {
            this(ESMenuTypes.VENDING_MACHINE_SELL.get(), window, playerInventory, inventory, costHolder, access, pos);
        }

        public Sell(MenuType<? extends Sell> type, int window, Inventory playerInventory,
                IItemHandlerModifiable inventory, DataSlot costHolder, ContainerLevelAccess access, BlockPos pos) {
            super(type, window, new SimpleContainerData(3), access, pos);

            assertItemHandlerSize(inventory, 3);
            this.costHolder = costHolder;
            addSlot(new SellSlot(inventory, VendingMachineBlockEntity.SLOT_STORAGE_OUT, SELL_X, SELL_Y));
            addDataSlot(costHolder);

            ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
        }

        public int getCost() {
            return costHolder.get();
        }

        public void purchase(ServerPlayer player, int amount) {
            access.execute((level, pos) -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null && blockEntity instanceof VendingMachineBlockEntity vending) {
                    vending.sell(player, amount);
                }
            });
        }

        @Override
        protected Block getValidBlock() {
            return ESBlocks.SMALL_VENDING_MACHINE.get();
        }

        @Override
        public ItemStack quickMoveStack(@Nonnull Player player, int index) {
            if (index == 0 && player instanceof ServerPlayer serverPlayer) {
                purchase(serverPlayer, 64);
            }
            return ItemStack.EMPTY;
        }
    }

    public static class Storage extends MachineContainerMenu {
        private static final int STORAGE_IN_X = 62;
        private static final int STORAGE_IN_Y = 19;
        private static final int STORAGE_OUT_X = 62;
        private static final int STORAGE_OUT_Y = 53;
        private static final int BOONDOLLAR_X = 134;
        private static final int BOONDOLLAR_Y = 20;

        private final DataSlot costHolder;
        private final DataSlot amountHolder;
        private final Slot output;

        public Storage(int window, Inventory playerInventory, FriendlyByteBuf buffer) {
            this(ESMenuTypes.VENDING_MACHINE_STORAGE.get(), window, playerInventory, new ItemStackHandler(3),
                    DataSlot.standalone(), DataSlot.standalone(), ContainerLevelAccess.NULL, buffer.readBlockPos());
        }

        public Storage(int window, Inventory playerInventory, IItemHandlerModifiable inventory, DataSlot costHolder,
                DataSlot amountHolder, ContainerLevelAccess access, BlockPos pos) {
            this(ESMenuTypes.VENDING_MACHINE_STORAGE.get(), window, playerInventory, inventory, costHolder,
                    amountHolder, access, pos);
        }

        public Storage(MenuType<? extends Storage> type, int window, Inventory playerInventory,
                IItemHandlerModifiable inventory, DataSlot costHolder, DataSlot amountHolder,
                ContainerLevelAccess access, BlockPos pos) {
            super(type, window, new SimpleContainerData(3), access, pos);

            assertItemHandlerSize(inventory, 3);
            this.costHolder = costHolder;
            this.amountHolder = amountHolder;
            addSlot(new SlotItemHandler(inventory, VendingMachineBlockEntity.SLOT_STORAGE_IN, STORAGE_IN_X,
                    STORAGE_IN_Y));
            addSlot(output = new SlotItemHandler(inventory, VendingMachineBlockEntity.SLOT_STORAGE_OUT, STORAGE_OUT_X,
                    STORAGE_OUT_Y) {
                @Override
                public boolean mayPlace(@Nonnull ItemStack stack) {
                    return false;
                }
            });
            addSlot(new SlotItemHandler(inventory, VendingMachineBlockEntity.SLOT_BOONDOLLARS, BOONDOLLAR_X,
                    BOONDOLLAR_Y) {
                @Override
                public boolean mayPlace(@Nonnull ItemStack stack) {
                    return false;
                }
            });
            addDataSlot(costHolder);
            addDataSlot(amountHolder);

            ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
        }

        public int getCost() {
            return costHolder.get();
        }

        public void setCost(int cost) {
            costHolder.set(cost);
        }

        public int getAmount() {
            return amountHolder.get();
        }

        public int getMax() {
            ItemStack outStack = output.getItem();
            if (outStack.isEmpty())
                return 0;
            return VendingMachineBlockEntity.getMaxStacks() * outStack.getMaxStackSize();
        }

        @Override
        protected Block getValidBlock() {
            return ESBlocks.SMALL_VENDING_MACHINE.get();
        }

        @Override
        public ItemStack quickMoveStack(@Nonnull Player player, int index) {
            ItemStack stack = ItemStack.EMPTY;
            Slot slot = slots.get(index);
            int all = slots.size();

            if (slot != null && slot.hasItem()) {
                ItemStack original = slot.getItem();
                stack = original.copy();
                boolean result = false;

                if (index == VendingMachineBlockEntity.SLOT_STORAGE_OUT) {
                    result = moveItemStackTo(original, VendingMachineBlockEntity.SLOT_BOONDOLLARS, all, false);
                    if (result) {
                        slot.set(stack.copy());
                        slot.remove(stack.getCount() - original.getCount());
                    }
                } else if (index > VendingMachineBlockEntity.SLOT_BOONDOLLARS) {
                    result = moveItemStackTo(original, 0, VendingMachineBlockEntity.SLOT_BOONDOLLARS, false);
                }

                if (!result)
                    return ItemStack.EMPTY;

                if (!ItemStack.matches(original, slot.getItem()) && index != VendingMachineBlockEntity.SLOT_STORAGE_OUT)
                    slot.set(original);
            }

            return stack;
        }
    }
}
