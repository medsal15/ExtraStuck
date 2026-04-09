package com.medsal15.compat.curios;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.medsal15.items.ESItems;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.entity.underling.UnderlingEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public final class ESCuriosEventsHandlers {
    public static void handleGummyRing(final LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof UnderlingEntity underling))
            return;

        Entity entity = event.getSource().getEntity();
        if (entity == null || !(entity instanceof LivingEntity attacker))
            return;

        Optional<ICuriosItemHandler> oinventory = CuriosApi.getCuriosInventory(attacker);
        if (oinventory.isEmpty())
            return;

        ICuriosItemHandler inventory = oinventory.get();
        int slots = inventory.getEquippedCurios().getSlots();
        for (int i = 0; i < slots; i++) {
            if (inventory.getEquippedCurios().getStackInSlot(i).is(ESItems.GUMMY_RING)) {
                underling.dropCandy = true;
                break;
            }
        }
    }

    public static boolean showValue(final ItemTooltipEvent event) {
        @Nullable
        Player player = event.getEntity();
        if (player == null)
            return false;

        Optional<ICuriosItemHandler> oinventory = CuriosApi.getCuriosInventory(player);
        if (oinventory.isEmpty())
            return false;

        ICuriosItemHandler inventory = oinventory.get();
        int slots = inventory.getEquippedCurios().getSlots();
        for (int i = 0; i < slots; i++) {
            if (inventory.getEquippedCurios().getStackInSlot(i).is(ESTags.Items.SHOW_VALUE)) {
                return true;
            }
        }
        return false;
    }

    public static boolean showGrist(final Player player) {
        Optional<ICuriosItemHandler> oinventory = CuriosApi.getCuriosInventory(player);
        if (oinventory.isEmpty())
            return false;

        ICuriosItemHandler inventory = oinventory.get();
        int slots = inventory.getEquippedCurios().getSlots();
        for (int i = 0; i < slots; i++) {
            if (inventory.getEquippedCurios().getStackInSlot(i).is(ESTags.Items.SHOW_GRIST)) {
                return true;
            }
        }
        return false;
    }
}
