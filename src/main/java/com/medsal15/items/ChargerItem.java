package com.medsal15.items;

import javax.annotation.Nonnull;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ChargerItem extends Item {
    public ChargerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        @SuppressWarnings("null")
        IEnergyStorage self = Capabilities.EnergyStorage.ITEM.getCapability(held, null);
        if (self != null && self.canExtract()) {
            boolean used = false;
            Inventory inventory = player.getInventory();
            for (ItemStack offhand : inventory.offhand) {
                if (offhand.isEmpty() || offhand.getItem() instanceof ChargerItem)
                    continue;

                @SuppressWarnings("null")
                IEnergyStorage other = Capabilities.EnergyStorage.ITEM.getCapability(held, null);
                if (other != null && other.canReceive()) {
                    int sent = other.receiveEnergy(self.getEnergyStored(), false);
                    if (sent > 0) {
                        used = true;
                        self.extractEnergy(sent, false);
                    }
                }
            }
            for (ItemStack armor : inventory.armor) {
                if (armor.isEmpty() || armor.getItem() instanceof ChargerItem)
                    continue;

                @SuppressWarnings("null")
                IEnergyStorage other = Capabilities.EnergyStorage.ITEM.getCapability(held, null);
                if (other != null && other.canReceive()) {
                    int sent = other.receiveEnergy(self.getEnergyStored(), false);
                    if (sent > 0) {
                        used = true;
                        self.extractEnergy(sent, false);
                    }
                }
            }
            for (ItemStack inv : inventory.items) {
                if (inv.isEmpty() || inv.getItem() instanceof ChargerItem)
                    continue;

                @SuppressWarnings("null")
                IEnergyStorage other = Capabilities.EnergyStorage.ITEM.getCapability(inv, null);
                if (other != null && other.canReceive()) {
                    int sent = other.receiveEnergy(self.getEnergyStored(), false);
                    if (sent > 0) {
                        used = true;
                        self.extractEnergy(sent, false);
                    }
                }
            }

            if (used) {
                return InteractionResultHolder.consume(held);
            }
        }

        return super.use(level, player, hand);
    }
}
