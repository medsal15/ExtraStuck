package com.medsal15.items.shields;

import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

public interface IShieldBlock {
    /**
     * Deals with the shield blocking
     *
     * @return True if the event is handled, false otherwise
     */
    public boolean onShieldBlock(LivingShieldBlockEvent event);
}
