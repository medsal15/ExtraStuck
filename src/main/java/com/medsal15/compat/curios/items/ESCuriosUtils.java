package com.medsal15.compat.curios.items;

import java.util.List;
import java.util.function.Predicate;

import com.mraof.minestuck.api.uranium.IUraniumHandler;
import com.mraof.minestuck.api.uranium.UraniumCapabilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class ESCuriosUtils {
    public static int countWornItems(LivingEntity livingEntity, Predicate<ItemStack> filter) {
        ICuriosItemHandler inventory = CuriosApi.getCuriosInventory(livingEntity).orElse(null);
        if (inventory != null) {
            return inventory.findCurios(filter).size();
        }
        return 0;
    }

    public static boolean wearsItem(LivingEntity livingEntity, Predicate<ItemStack> filter) {
        ICuriosItemHandler inventory = CuriosApi.getCuriosInventory(livingEntity).orElse(null);
        if (inventory != null) {
            return inventory.isEquipped(filter);
        }
        return false;
    }

    public static void hurtAndBreakFirst(ServerPlayer serverPlayer, Predicate<ItemStack> filter, int damage) {
        ICuriosItemHandler inventory = CuriosApi.getCuriosInventory(serverPlayer).orElse(null);
        if (inventory != null) {
            SlotResult slot = inventory.findFirstCurio(filter).orElse(null);
            if (slot != null) {
                slot.stack().hurtAndBreak(damage, serverPlayer.serverLevel(), serverPlayer,
                        s -> CuriosApi.broadcastCurioBreakEvent(slot.slotContext()));
            }
        }
    }

    public static int chargeFEInventory(ServerPlayer serverPlayer, int max) {
        ICuriosItemHandler inventory = CuriosApi.getCuriosInventory(serverPlayer).orElse(null);
        if (inventory == null || max <= 0)
            return 0;

        int transferred = 0;
        List<SlotResult> slots = inventory.findCurios(s -> {
            @SuppressWarnings("null")
            IEnergyStorage energyHandler = Capabilities.EnergyStorage.ITEM.getCapability(s, null);
            return energyHandler != null && energyHandler.canReceive();
        });
        for (SlotResult slot : slots) {
            @SuppressWarnings("null")
            IEnergyStorage energyHandler = Capabilities.EnergyStorage.ITEM.getCapability(slot.stack(), null);
            if (energyHandler == null || !energyHandler.canReceive())
                continue;

            int sent = energyHandler.receiveEnergy(max, false);
            transferred += sent;
            max -= sent;
            if (max <= 0)
                break;
        }

        return transferred;
    }

    public static int chargeUrInventory(ServerPlayer serverPlayer, int max) {
        ICuriosItemHandler inventory = CuriosApi.getCuriosInventory(serverPlayer).orElse(null);
        if (inventory == null || max <= 0)
            return 0;

        int transferred = 0;
        List<SlotResult> slots = inventory.findCurios(s -> {
            @SuppressWarnings("null")
            IUraniumHandler energyHandler = UraniumCapabilities.ITEM.getCapability(s, null);
            return energyHandler != null && energyHandler.canReceiveUranium();
        });
        for (SlotResult slot : slots) {
            @SuppressWarnings("null")
            IUraniumHandler energyHandler = UraniumCapabilities.ITEM.getCapability(slot.stack(), null);
            if (energyHandler == null || !energyHandler.canReceiveUranium())
                continue;

            int sent = energyHandler.receiveUranium(max, false);
            transferred += sent;
            max -= sent;
            if (max <= 0)
                break;
        }

        return transferred;
    }
}
