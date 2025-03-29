package com.medsal15.entities;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;
import com.medsal15.entities.projectiles.CaptainJusticeShield;
import com.medsal15.entities.projectiles.arrows.CandyArrow;
import com.medsal15.entities.projectiles.arrows.CardboardArrow;
import com.medsal15.entities.projectiles.arrows.FlameArrow;
import com.medsal15.entities.projectiles.arrows.LightningArrow;
import com.medsal15.entities.projectiles.arrows.MissedArrow;
import com.medsal15.entities.projectiles.arrows.NetherArrow;

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
    public static final Supplier<EntityType<FlameArrow>> FLAME_ARROW = ENTITIES.register("flame_arrow",
            () -> EntityType.Builder.<FlameArrow>of(FlameArrow::new, MobCategory.MISC)
                    .sized(.5F, .5F).setTrackingRange(1).setUpdateInterval(20)
                    .build(ExtraStuck.MODID + ":flame_arrow"));
    public static final Supplier<EntityType<NetherArrow>> NETHER_ARROW = ENTITIES.register("nether_arrow",
            () -> EntityType.Builder.<NetherArrow>of(NetherArrow::new, MobCategory.MISC)
                    .sized(.5F, .5F).setTrackingRange(1).setUpdateInterval(20)
                    .build(ExtraStuck.MODID + ":nether_arrow"));
    public static final Supplier<EntityType<CardboardArrow>> CARDBOARD_ARROW = ENTITIES.register("cardboard_arrow",
            () -> EntityType.Builder.<CardboardArrow>of(CardboardArrow::new, MobCategory.MISC)
                    .sized(.5F, .5F).setTrackingRange(1).setUpdateInterval(20)
                    .build(ExtraStuck.MODID + ":cardboard_arrow"));
    public static final Supplier<EntityType<MissedArrow>> MISSED_ARROW = ENTITIES.register("missed_arrow",
            () -> EntityType.Builder.<MissedArrow>of(MissedArrow::new, MobCategory.MISC)
                    .sized(.5F, .5F).setTrackingRange(1).setUpdateInterval(20)
                    .build(ExtraStuck.MODID + ":missed_arrow"));
    public static final Supplier<EntityType<CandyArrow>> CANDY_ARROW = ENTITIES.register("candy_arrow",
            () -> EntityType.Builder.<CandyArrow>of(CandyArrow::new, MobCategory.MISC)
                    .sized(.5F, .5F).setTrackingRange(1).setUpdateInterval(20)
                    .build(ExtraStuck.MODID + ":candy_arrow"));
    public static final Supplier<EntityType<LightningArrow>> LIGHTNING_ARROW = ENTITIES.register("lightning_arrow",
            () -> EntityType.Builder.<LightningArrow>of(LightningArrow::new, MobCategory.MISC)
                    .sized(.5F, .5F).setTrackingRange(1).setUpdateInterval(20)
                    .build(ExtraStuck.MODID + ":lightning_arrow"));
}
