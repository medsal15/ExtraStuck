package com.medsal15.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

import com.medsal15.ExtraStuck;
import com.medsal15.entities.projectiles.CaptainJusticeShield;
import com.medsal15.entities.projectiles.arrows.AmethystArrow;
import com.medsal15.entities.projectiles.arrows.CandyArrow;
import com.medsal15.entities.projectiles.arrows.CardboardArrow;
import com.medsal15.entities.projectiles.arrows.ExplosiveArrow;
import com.medsal15.entities.projectiles.arrows.FlameArrow;
import com.medsal15.entities.projectiles.arrows.GlassArrow;
import com.medsal15.entities.projectiles.arrows.HealingArrow;
import com.medsal15.entities.projectiles.arrows.IronArrow;
import com.medsal15.entities.projectiles.arrows.LightningArrow;
import com.medsal15.entities.projectiles.arrows.MiningArrow;
import com.medsal15.entities.projectiles.arrows.MissedArrow;
import com.medsal15.entities.projectiles.arrows.NetherArrow;
import com.medsal15.entities.projectiles.arrows.PrismarineArrow;
import com.medsal15.entities.projectiles.arrows.QuartzArrow;
import com.medsal15.entities.projectiles.arrows.EndArrow;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESEntities {
    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE,
            ExtraStuck.MODID);

    public static final Supplier<EntityType<CaptainJusticeShield>> CAPTAIN_JUSTICE_SHIELD = ENTITIES.register(
            "captain_justice_shield",
            () -> EntityType.Builder.<CaptainJusticeShield>of(CaptainJusticeShield::new, MobCategory.MISC)
                    .sized(1F, .25F).setTrackingRange(1).setUpdateInterval(10)
                    .build(ExtraStuck.MODID + ":captain_justice_shield"));

    // #region Arrows
    public static final Supplier<EntityType<FlameArrow>> FLAME_ARROW = registerArrow("flame_arrow", FlameArrow::new);
    public static final Supplier<EntityType<NetherArrow>> NETHER_ARROW = registerArrow("nether_arrow",
            NetherArrow::new);
    public static final Supplier<EntityType<CardboardArrow>> CARDBOARD_ARROW = registerArrow("cardboard_arrow",
            CardboardArrow::new);
    public static final Supplier<EntityType<MissedArrow>> MISSED_ARROW = registerArrow("missed_arrow",
            MissedArrow::new);
    public static final Supplier<EntityType<CandyArrow>> CANDY_ARROW = registerArrow("candy_arrow", CandyArrow::new);
    public static final Supplier<EntityType<LightningArrow>> LIGHTNING_ARROW = registerArrow("lightning_arrow",
            LightningArrow::new);
    public static final Supplier<EntityType<ExplosiveArrow>> EXPLOSIVE_ARROW = registerArrow("explosive_arrow",
            ExplosiveArrow::new);
    public static final Supplier<EntityType<IronArrow>> IRON_ARROW = registerArrow("iron_arrow", IronArrow::new);
    public static final Supplier<EntityType<QuartzArrow>> QUARTZ_ARROW = registerArrow("quartz_arrow",
            QuartzArrow::new);
    public static final Supplier<EntityType<PrismarineArrow>> PRISMARINE_ARROW = registerArrow("prismarine_arrow",
            PrismarineArrow::new);
    public static final Supplier<EntityType<GlassArrow>> GLASS_ARROW = registerArrow("glass_arrow", GlassArrow::new);
    public static final Supplier<EntityType<AmethystArrow>> AMETHYST_ARROW = registerArrow("amethyst_arrow",
            AmethystArrow::new);
    public static final Supplier<EntityType<MiningArrow>> MINING_ARROW = registerArrow("mining_arrow",
            MiningArrow::new);
    public static final Supplier<EntityType<HealingArrow>> HEALING_ARROW = registerArrow("healing_arrow",
            HealingArrow::new);
    public static final Supplier<EntityType<EndArrow>> END_ARROW = registerArrow("teleport_arrow",
            EndArrow::new);
    // #endregion Arrows

    private static <T extends AbstractArrow> Supplier<EntityType<T>> registerArrow(String name, EntityFactory<T> fac) {
        return ENTITIES.register(name, () -> EntityType.Builder.<T>of(fac, MobCategory.MISC)
                .sized(.5F, .5F).setTrackingRange(1).setUpdateInterval(20).build(ExtraStuck.MODID + ":" + name));
    }

    public static Collection<EntityType<? extends AbstractArrow>> getArrows() {
        ArrayList<EntityType<? extends AbstractArrow>> list = new ArrayList<>();
        list.add(FLAME_ARROW.get());
        list.add(NETHER_ARROW.get());
        list.add(CARDBOARD_ARROW.get());
        list.add(MISSED_ARROW.get());
        list.add(CANDY_ARROW.get());
        list.add(LIGHTNING_ARROW.get());
        list.add(EXPLOSIVE_ARROW.get());
        list.add(IRON_ARROW.get());
        list.add(QUARTZ_ARROW.get());
        list.add(PRISMARINE_ARROW.get());
        list.add(GLASS_ARROW.get());
        list.add(AMETHYST_ARROW.get());
        list.add(MINING_ARROW.get());
        list.add(HEALING_ARROW.get());
        list.add(END_ARROW.get());
        return list;
    }
}
