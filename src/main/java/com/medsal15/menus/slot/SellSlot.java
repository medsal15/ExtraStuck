package com.medsal15.menus.slot;

import javax.annotation.Nonnull;

import com.medsal15.network.ESPackets.VendingMachinePurchase;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class SellSlot extends SlotItemHandler {
    private final int index;

    public SellSlot(IItemHandler inventory, int slot, int x, int y) {
        super(inventory, slot, x, y);

        this.index = slot;
    }

    @Override
    public ItemStack remove(int amount) {
        PacketDistributor.sendToServer(new VendingMachinePurchase(amount), new CustomPacketPayload[0]);
        return ItemStack.EMPTY;
    }

    @Override
    public boolean mayPickup(@Nonnull Player player) {
        return !(player instanceof ServerPlayer);
    }

    @Override
    public ItemStack getItem() {
        return getItemHandler().getStackInSlot(index).copyWithCount(1);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return false;
    }
}
