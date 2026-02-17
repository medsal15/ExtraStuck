package com.medsal15.compat.curios.items;

import java.util.Optional;

import com.medsal15.utils.ESTags;

import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class ESCuriosUtils {
    /**
     * Counts the amount of cosmic plague immunity curios equipped
     *
     * @param livingEntity
     * @return
     */
    public static int countCosmicPlagueImmune(LivingEntity livingEntity) {
        Optional<ICuriosItemHandler> oinventory = CuriosApi.getCuriosInventory(livingEntity);
        if (oinventory.isPresent()) {
            ICuriosItemHandler inventory = oinventory.get();
            return inventory.findCurios(stack -> stack.is(ESTags.Items.COSMIC_PLAGUE_CURIOS)).size();
        }
        return 0;
    }
}
