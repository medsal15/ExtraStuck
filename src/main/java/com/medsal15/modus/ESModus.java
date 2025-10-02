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
    public static final Supplier<ModusType<FortuneModus>> FORTUNE_MODUS = MODUSES.register("fortune_modus",
            () -> new ModusType<>(FortuneModus::new, ESItems.FORTUNE_MODUS_CARD));
    public static final Supplier<ModusType<OreModus>> ORE_MODUS = MODUSES.register("ore_modus",
            () -> new ModusType<>(OreModus::new, ESItems.ORE_MODUS_CARD));
    public static final Supplier<ModusType<ArcheologyModus>> ARCHEOLOGY_MODUS = MODUSES.register("archeology_modus",
            () -> new ModusType<>(ArcheologyModus::new, ESItems.ARCHEOLOGY_MODUS_CARD));
    public static final Supplier<ModusType<VoidModus>> VOID_MODUS = MODUSES.register("void_modus",
            () -> new ModusType<>(VoidModus::new, ESItems.VOID_MODUS_CARD));
    public static final Supplier<ModusType<EnderModus>> ENDER_MODUS = MODUSES.register("ender_modus",
            () -> new ModusType<>(EnderModus::new, ESItems.ENDER_MODUS_CARD));
}
