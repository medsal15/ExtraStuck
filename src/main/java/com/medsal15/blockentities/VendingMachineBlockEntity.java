package com.medsal15.blockentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.handlers.BEStackHandler;
import com.medsal15.config.ConfigServer;
import com.medsal15.menus.VendingMachineMenu;
import com.mraof.minestuck.blockentity.machine.IOwnable;
import com.mraof.minestuck.blockentity.machine.MachineProcessBlockEntity;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerBoondollars;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class VendingMachineBlockEntity extends MachineProcessBlockEntity implements IOwnable, MenuProvider {
    public static final String TITLE = "container.extrastuck.vending_machine";
    public static final int SLOT_STORAGE_IN = 0;
    public static final int SLOT_STORAGE_OUT = 1;
    public static final int SLOT_BOONDOLLARS = 2;

    @Nullable
    private PlayerIdentifier owner;
    private int cost;
    /** Amount of items stored */
    private int amount;

    private final DataSlot costHolder = new DataSlot() {
        public int get() {
            return cost;
        };

        public void set(int value) {
            cost = value;
        };
    };
    private final DataSlot amountHolder = new DataSlot() {
        public int get() {
            return amount;
        };

        public void set(int value) {
            amount = value;
        };
    };

    public final MenuProvider storageMenuProvider;

    public VendingMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ESBlockEntities.SMALL_VENDING_MACHINE.get(), pos, state);

        storageMenuProvider = new StorageMenuProvider();
    }

    public class StorageMenuProvider implements MenuProvider {
        @Override
        public Component getDisplayName() {
            return VendingMachineBlockEntity.this.getDisplayName();
        }

        @Override
        @Nullable
        public AbstractContainerMenu createMenu(int containerId, @Nonnull Inventory playerInventory,
                @Nonnull Player player) {
            return VendingMachineBlockEntity.this.createStorageMenu(containerId, playerInventory, player);
        }
    }

    public int sell(ServerPlayer player, int amount) {
        ItemStack stack = itemHandler.getStackInSlot(SLOT_STORAGE_OUT);
        if (stack.isEmpty())
            return 0;

        amount = Math.min(amount, stack.getCount());
        @Nullable
        PlayerData playerData = PlayerData.get(player).orElse(null);
        if (playerData == null)
            return 0;
        amount = (int) Math.min(amount, PlayerBoondollars.getBoondollars(playerData) / cost);
        if (amount <= 0)
            return 0;
        int real_cost = cost * amount;
        boolean sold = PlayerBoondollars.tryTakeBoondollars(playerData, real_cost, true);
        if (sold) {
            stack = itemHandler.extractItem(SLOT_STORAGE_OUT, amount, false);
            if (!player.getInventory().add(stack))
                player.drop(stack, false);

            ItemStack boondollars = itemHandler.getStackInSlot(SLOT_BOONDOLLARS);
            if (boondollars.isEmpty()) {
                itemHandler.setStackInSlot(SLOT_BOONDOLLARS,
                        BoondollarsItem.setCount(MSItems.BOONDOLLARS.toStack(), real_cost));
            } else {
                itemHandler.setStackInSlot(SLOT_BOONDOLLARS,
                        BoondollarsItem.setCount(boondollars, real_cost + BoondollarsItem.getCount(boondollars)));
            }
        }
        return amount;
    }

    public static int getMaxStacks() {
        return ConfigServer.VENDING_MACHINE_STACKS.get();
    }

    public void growAmount(int amount) {
        this.amount = Math.max(this.amount + amount, 0);
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side == null)
            return itemHandler;

        return null;
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, Provider provider) {
        super.loadAdditional(nbt, provider);

        owner = IdentifierHandler.load(nbt, "owner").result().orElse(null);
        cost = nbt.getInt("cost");
        amount = nbt.getInt("amount");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, Provider provider) {
        super.saveAdditional(nbt, provider);

        nbt.putInt("cost", cost);
        nbt.putInt("amount", amount);
        if (owner != null)
            owner.saveToNBT(nbt, "owner");
    }

    @Override
    protected void tick() {
    }

    @Override
    protected ItemStackHandler createItemHandler() {
        return new VendingMachineItemHandler();
    }

    private class VendingMachineItemHandler extends BEStackHandler {
        public VendingMachineItemHandler() {
            super(3, (slot, stack) -> true, VendingMachineBlockEntity.this);
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            ItemStack out = stacks.get(SLOT_STORAGE_OUT);
            return slot == SLOT_STORAGE_IN && (out.isEmpty() || ItemStack.isSameItemSameComponents(stack, out));
        }

        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (stack.isEmpty())
                return ItemStack.EMPTY;

            if (!isItemValid(slot, stack))
                return stack;

            validateSlotIndex(slot);

            ItemStack existing = stacks.get(SLOT_STORAGE_OUT);
            if (!existing.isEmpty() && !ItemStack.isSameItemSameComponents(stack, existing))
                return ItemStack.EMPTY;

            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            validateSlotIndex(slot);
            stacks.set(slot, stack);
            transferItems();
            onContentsChanged(slot);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot != SLOT_STORAGE_OUT)
                return super.extractItem(slot, amount, simulate);
            if (amount == 0)
                return ItemStack.EMPTY;

            validateSlotIndex(slot);
            ItemStack existing = stacks.get(slot);

            if (existing.isEmpty())
                return ItemStack.EMPTY;

            final VendingMachineBlockEntity self = VendingMachineBlockEntity.this;
            int toExtract = Math.min(amount, existing.getMaxStackSize());
            int count = existing.getCount() + self.getAmount();
            if (count <= toExtract) {
                if (!simulate) {
                    self.setAmount(0);
                    stacks.set(slot, ItemStack.EMPTY);
                    onContentsChanged(slot);
                }
                return existing.copyWithCount(count);
            } else {
                if (!simulate) {
                    int extracting = toExtract;
                    if (extracting >= self.getAmount()) {
                        extracting -= self.getAmount();
                        self.setAmount(0);
                    } else {
                        self.growAmount(-extracting);
                        extracting = 0;
                    }
                    if (extracting >= 0) {
                        stacks.set(slot, existing.copyWithCount(existing.getCount() - extracting));
                    }
                    onContentsChanged(slot);
                }
                return existing.copyWithCount(toExtract);
            }
        }

        /**
         * Transfers items from input slot to storage/output
         *
         * @returns True if the contents have changed
         */
        protected boolean transferItems() {
            ItemStack out = stacks.get(SLOT_STORAGE_OUT);
            ItemStack in = stacks.get(SLOT_STORAGE_IN);

            if (out.isEmpty()) {
                stacks.set(SLOT_STORAGE_OUT, in);
                stacks.set(SLOT_STORAGE_IN, ItemStack.EMPTY);
                return true;
            } else {
                final VendingMachineBlockEntity self = VendingMachineBlockEntity.this;
                int limit = getMaxStacks() * out.getMaxStackSize();
                boolean changed = false;

                // Transfer from storage to out
                if (out.getMaxStackSize() > out.getCount() && self.getAmount() > 0) {
                    int transfer = Math.min(out.getMaxStackSize() - out.getCount(), self.getAmount());
                    out.grow(transfer);
                    self.growAmount(-transfer);
                    changed = true;
                }

                // Transfer from input to storage
                if (!in.isEmpty() && self.getAmount() < limit) {
                    int transfer = Math.min(limit - self.getAmount(), in.getCount());
                    in.shrink(transfer);
                    self.growAmount(transfer);
                    changed = true;
                }

                return changed;
            }
        }
    }

    // These 4 below are required to update the client's item
    // Credit to @commoble on discord
    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(@Nonnull Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public void onDataPacket(@Nonnull Connection net, @Nonnull ClientboundBlockEntityDataPacket pkt,
            @Nonnull Provider lookupProvider) {
        loadWithComponents(pkt.getTag(), lookupProvider);
    }

    @Override
    public void handleUpdateTag(@Nonnull CompoundTag tag, @Nonnull Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);

        if (level != null)
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
    }

    // IOwnable
    @Override
    public PlayerIdentifier getOwner() {
        return owner;
    }

    @Override
    public void setOwner(PlayerIdentifier player) {
        owner = player;
    }

    // MenuProvider
    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int containerId, @Nonnull Inventory playerInventory,
            @Nonnull Player player) {
        Level l = level;
        if (l == null)
            return null;
        return new VendingMachineMenu.Sell(containerId, playerInventory, itemHandler, costHolder,
                ContainerLevelAccess.create(l, worldPosition), worldPosition);
    }

    @Nullable
    public AbstractContainerMenu createStorageMenu(int containerId, @Nonnull Inventory playerInventory,
            @Nonnull Player player) {
        Level l = level;
        if (l == null)
            return null;
        return new VendingMachineMenu.Storage(containerId, playerInventory, itemHandler, costHolder, amountHolder,
                ContainerLevelAccess.create(l, worldPosition), worldPosition);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(TITLE);
    }
}
