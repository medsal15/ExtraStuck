package com.medsal15.blockentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.handlers.BEStackHandler;
import com.medsal15.menus.StorageBlockMenu;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class StorageBlockEntity extends BlockEntity {
    protected final ItemStackHandler itemHandler;

    public StorageBlockEntity(BlockEntityType<? extends StorageBlockEntity> type, BlockPos pos,
            BlockState state) {
        super(type, pos, state);

        itemHandler = new BEStackHandler(54, this::isValidItem, this);
    }

    public abstract boolean isValidItem(int slot, ItemStack stack);

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag, @Nonnull Provider registries) {
        super.saveAdditional(tag, registries);

        tag.put("inventory", itemHandler.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag tag, @Nonnull Provider registries) {
        super.loadAdditional(tag, registries);

        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    public IItemHandler getItemHandler(@Nullable Direction direction) {
        return itemHandler;
    }

    public int getAnalogOutputSignal() {
        float signal = 0;

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                signal += (float) stack.getCount() / (float) itemHandler.getSlotLimit(i);
            }
        }

        signal /= itemHandler.getSlots();
        return Mth.lerpDiscrete(signal, 0, 15);
    }

    public static class Dowel extends StorageBlockEntity implements MenuProvider {
        public static final String TITLE = "container.extrastuck.dowel_storage";

        public Dowel(BlockPos pos, BlockState state) {
            super(ESBlockEntities.DOWEL_STORAGE.get(), pos, state);
        }

        @Override
        public boolean isValidItem(int slot, ItemStack stack) {
            return stack.is(MSItems.CRUXITE_DOWEL);
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable(TITLE);
        }

        @Override
        @Nullable
        public AbstractContainerMenu createMenu(int containerId, @Nonnull Inventory playerInventory,
                @Nonnull Player player) {
            if (level != null)
                return new StorageBlockMenu.Dowel(containerId, ContainerLevelAccess.create(level, worldPosition),
                        playerInventory, itemHandler, stack -> isValidItem(0, stack));
            return null;
        }
    }

    public static class Card extends StorageBlockEntity implements MenuProvider {
        public static final String TITLE = "container.extrastuck.card_storage";

        public Card(BlockPos pos, BlockState state) {
            super(ESBlockEntities.CARD_STORAGE.get(), pos, state);
        }

        @Override
        public boolean isValidItem(int slot, ItemStack stack) {
            return stack.is(MSItems.CAPTCHA_CARD);
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable(TITLE);
        }

        @Override
        @Nullable
        public AbstractContainerMenu createMenu(int containerId, @Nonnull Inventory playerInventory,
                @Nonnull Player player) {
            if (level != null) {
                return new StorageBlockMenu.Card(containerId, ContainerLevelAccess.create(level, worldPosition),
                        playerInventory, itemHandler, stack -> isValidItem(0, stack));
            }
            return null;
        }
    }
}
