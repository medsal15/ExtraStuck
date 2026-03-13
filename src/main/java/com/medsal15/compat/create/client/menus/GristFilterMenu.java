package com.medsal15.compat.create.client.menus;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.medsal15.ExtraStuck;
import com.medsal15.compat.create.items.ESCreateComponents;
import com.medsal15.compat.create.items.GristFilterItem.GristFilterEntry;
import com.medsal15.compat.create.items.GristFilterItem.GristFilterEntry.ComparingMode;
import com.medsal15.compat.create.network.ESCreatePackets;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.inventory.ContainerHelper;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class GristFilterMenu extends AbstractContainerMenu {
    public static final String GRIST_FILTER_LIST = ExtraStuck.MODID + ".grist_filter.list";
    private static final int INVENTORY_X = 65, INVENTORY_Y = 107;

    public List<GristFilterEntry> entries;

    private final ItemStack filter;
    public final ItemStackHandler ghostInventory;

    public GristFilterMenu(int id, Inventory inventory) {
        this(ESCreateMenuTypes.GRIST_FILTER.get(), id, inventory, ItemStack.EMPTY);
    }

    public GristFilterMenu(int id, Inventory inventory, RegistryFriendlyByteBuf buffer) {
        this(ESCreateMenuTypes.GRIST_FILTER.get(), id, inventory, ItemStack.STREAM_CODEC.decode(buffer));
    }

    public GristFilterMenu(MenuType<?> type, int id, Inventory inventory, ItemStack stack) {
        super(type, id);

        filter = stack;
        entries = new ArrayList<>();
        entries.addAll(stack.getOrDefault(ESCreateComponents.GRIST_FILTER_DATA, List.of()));

        ghostInventory = new ItemStackHandler(1);

        addItemSlots(inventory);
        initGhostItem();
    }

    private void addItemSlots(Inventory playerInventory) {
        addSlot(new SlotItemHandler(ghostInventory, 0, 16, 62) {
            @Override
            public boolean mayPickup(@Nonnull Player playerIn) {
                return false;
            }

            @Override
            public boolean isFake() {
                return true;
            }
        });

        ContainerHelper.addPlayerInventorySlots(this::addSlot, INVENTORY_X, INVENTORY_Y, playerInventory);
    }

    private void initGhostItem() {
        ItemStack stack = MSItems.STONE_TABLET.toStack();
        stack.set(DataComponents.ITEM_NAME,
                Component.translatable(GRIST_FILTER_LIST).withStyle(ChatFormatting.BLUE));
        ghostInventory.setStackInSlot(0, stack);

        updateGhostStackLore();
    }

    public static GristFilterMenu create(int windowId, Inventory inventory, ItemStack filter) {
        return new GristFilterMenu(ESCreateMenuTypes.GRIST_FILTER.get(), windowId, inventory, filter);
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return player.getMainHandItem() == filter;
    }

    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        return ItemStack.EMPTY;
    }

    public void appendEntry(GristType grist, int amount, ComparingMode mode) {
        entries.add(new GristFilterEntry(grist, amount, mode));

        updateGhostStackLore();
    }

    public void clearContents() {
        entries.clear();

        updateGhostStackLore();
    }

    protected void updateGhostStackLore() {
        ItemStack stack = ghostInventory.getStackInSlot(0);
        if (stack.isEmpty())
            return;

        if (entries.size() > 0)
            stack.set(DataComponents.LORE, new ItemLore(
                    Lists.transform(entries, entry -> Component.literal("- ").append(entry.text())
                            .withStyle(style -> style.withItalic(false).withColor(ChatFormatting.WHITE)))));
        else
            stack.remove(DataComponents.LORE);
    }

    public void sendClearPacket() {
        PacketDistributor.sendToServer(ESCreatePackets.ClearGristFilter.INSTANCE);
    }

    @Override
    public void clicked(int slotId, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull Player player) {
        if (slotId == 0)
            return;
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public boolean canDragTo(@Nonnull Slot slotIn) {
        if (slotIn.index == 0)
            return false;
        return super.canDragTo(slotIn);
    }

    @Override
    public boolean canTakeItemForPickAll(@Nonnull ItemStack stack, @Nonnull Slot slotIn) {
        if (slotIn.index == 0)
            return false;
        return super.canTakeItemForPickAll(stack, slotIn);
    }

    @Override
    public void removed(@Nonnull Player player) {
        super.removed(player);

        if (entries.size() == 0 && filter.getOrDefault(ESCreateComponents.GRIST_FILTER_DATA, List.of()).size() > 0) {
            filter.remove(ESCreateComponents.GRIST_FILTER_DATA);
        } else if (entries.size() > 0) {
            filter.set(ESCreateComponents.GRIST_FILTER_DATA, entries);
        }
    }
}
