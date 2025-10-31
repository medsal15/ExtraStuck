package com.medsal15.compat.create.menus;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.data.ESLangProvider;
import com.medsal15.items.ESDataComponents;
import com.medsal15.items.components.GristFilterEntry;
import com.medsal15.items.components.GristFilterEntry.ComparingMode;
import com.medsal15.menus.ESMenuTypes;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.item.MSItems;
import com.simibubi.create.content.logistics.filter.AbstractFilterMenu;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class GristFilterMenu extends AbstractFilterMenu {
    public List<GristFilterEntry> entries;

    public GristFilterMenu(int id, Inventory inventory) {
        super(ESMenuTypes.GRIST_FILTER.get(), id, inventory, ItemStack.EMPTY);
    }

    public GristFilterMenu(MenuType<?> type, int id, Inventory inventory, RegistryFriendlyByteBuf buffer) {
        super(type, id, inventory, buffer);
    }

    public GristFilterMenu(MenuType<?> type, int id, Inventory inventory, ItemStack stack) {
        super(type, id, inventory, stack);
    }

    public static GristFilterMenu create(int id, Inventory inventory, ItemStack stack) {
        return new GristFilterMenu(ESMenuTypes.GRIST_FILTER.get(), id, inventory, stack);
    }

    @Override
    protected void init(Inventory inv, ItemStack contentHolderIn) {
        super.init(inv, contentHolderIn);
        ItemStack stack = MSItems.STONE_TABLET.toStack();
        stack.set(DataComponents.CUSTOM_NAME, Component.translatable(ESLangProvider.GRIST_FILTER_LIST)
                .withStyle(ChatFormatting.RESET, ChatFormatting.BLUE));
        ghostInventory.setStackInSlot(0, stack);
    }

    @Override
    protected int getPlayerInventoryXOffset() {
        return 51;
    }

    @Override
    protected int getPlayerInventoryYOffset() {
        return 107;
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return new ItemStackHandler(1);
    }

    @Override
    protected void addFilterSlots() {
        this.addSlot(new SlotItemHandler(ghostInventory, 0, 16, 62) {
            @Override
            public boolean mayPickup(@Nonnull Player playerIn) {
                return false;
            }
        });
    }

    public void appendEntry(GristType grist, int amount, ComparingMode mode) {
        entries.add(new GristFilterEntry(grist, amount, mode));
    }

    @Override
    public void clearContents() {
        entries.clear();
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId == 36)
            return;
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public boolean canDragTo(Slot slotIn) {
        if (slotIn.index == 36)
            return false;
        return super.canDragTo(slotIn);
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        if (slotIn.index == 36)
            return false;
        return super.canTakeItemForPickAll(stack, slotIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void initAndReadInventory(ItemStack filter) {
        super.initAndReadInventory(filter);

        entries = new ArrayList<>();
        List<GristFilterEntry> existing = filter.getOrDefault(ESDataComponents.GRIST_FILTER_DATA, List.of());
        entries.addAll(existing);
    }

    @Override
    protected void saveData(ItemStack filter) {
        if (entries.size() != 0) {
            filter.set(ESDataComponents.GRIST_FILTER_DATA, entries);
        } else if (filter.has(ESDataComponents.GRIST_FILTER_DATA)) {
            filter.remove(ESDataComponents.GRIST_FILTER_DATA);
        }
    }
}
