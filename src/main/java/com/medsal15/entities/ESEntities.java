package com.medsal15.entities;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;
import com.medsal15.entities.projectiles.CaptainJusticeShield;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESEntities {
    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE,
            ExtraStuck.MODID);

    public static final Supplier<EntityType<CaptainJusticeShield>> CAPTAIN_JUSTICE_SHIELD = ENTITIES.register(
            "captain_justice_shield",
            () -> EntityType.Builder.<CaptainJusticeShield>of(CaptainJusticeShield::new, MobCategory.MISC)
                    .sized(1F, .25F).setTrackingRange(1).setUpdateInterval(10)
                    .build(ExtraStuck.MODID + ":captain_justice_shield"));
}
