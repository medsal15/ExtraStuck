package com.medsal15.compat.curios.items;

import java.util.function.Predicate;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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
}
