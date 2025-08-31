package com.medsal15.modus;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.captchalogue.ModusType;

import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESModus {
    public static final DeferredRegister<ModusType<?>> MODUSES = DeferredRegister.create(Minestuck.id("modus_type"),
            ExtraStuck.MODID);

    public static final Supplier<ModusType<PileModus>> PILE_MODUS = MODUSES.register("pile_modus",
            () -> new ModusType<>(PileModus::new, ESItems.PILE_MODUS_CARD));
}
