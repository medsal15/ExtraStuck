package com.medsal15.compat.curios;

import java.util.Optional;

import com.medsal15.items.ESItems;
import com.mraof.minestuck.entity.underling.UnderlingEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
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
}
