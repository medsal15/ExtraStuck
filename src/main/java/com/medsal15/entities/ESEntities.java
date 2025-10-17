package com.medsal15.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

import com.medsal15.ExtraStuck;
import com.medsal15.entities.projectiles.CaptainJusticeShield;
import com.medsal15.entities.projectiles.ThrownBeegg;
import com.medsal15.entities.projectiles.UraniumRod;
import com.medsal15.entities.projectiles.arrows.AmethystArrow;
import com.medsal15.entities.projectiles.arrows.CandyArrow;
import com.medsal15.entities.projectiles.arrows.CardboardArrow;
import com.medsal15.entities.projectiles.arrows.DragonArrow;
import com.medsal15.entities.projectiles.arrows.EndArrow;
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
import com.medsal15.entities.projectiles.arrows.TeleportArrow;
import com.medsal15.entities.projectiles.bullets.ESBullet;
import com.medsal15.entities.projectiles.bullets.ItemBullet;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            BuiltInRegistries.ENTITY_TYPE, ExtraStuck.MODID);

    public static final Supplier<EntityType<CaptainJusticeShield>> CAPTAIN_JUSTICE_SHIELD = ENTITIES.register(
            "captain_justice_shield",
            () -> EntityType.Builder.<CaptainJusticeShield>of(CaptainJusticeShield::new, MobCategory.MISC)
                    .sized(1F, .25F).setTrackingRange(1).setUpdateInterval(10)
                    .build(ExtraStuck.MODID + ":captain_justice_shield"));

    // #region Arrows
    public static final Supplier<EntityType<FlameArrow>> FLAME_ARROW = register("flame_arrow", FlameArrow::new);
    public static final Supplier<EntityType<NetherArrow>> NETHER_ARROW = register("nether_arrow",
            NetherArrow::new);
    public static final Supplier<EntityType<CardboardArrow>> CARDBOARD_ARROW = register("cardboard_arrow",
            CardboardArrow::new);
    public static final Supplier<EntityType<MissedArrow>> MISSED_ARROW = register("missed_arrow",
            MissedArrow::new);
    public static final Supplier<EntityType<CandyArrow>> CANDY_ARROW = register("candy_arrow", CandyArrow::new);
    public static final Supplier<EntityType<LightningArrow>> LIGHTNING_ARROW = register("lightning_arrow",
            LightningArrow::new);
    public static final Supplier<EntityType<ExplosiveArrow>> EXPLOSIVE_ARROW = register("explosive_arrow",
            ExplosiveArrow::new);
    public static final Supplier<EntityType<IronArrow>> IRON_ARROW = register("iron_arrow", IronArrow::new);
    public static final Supplier<EntityType<QuartzArrow>> QUARTZ_ARROW = register("quartz_arrow",
            QuartzArrow::new);
    public static final Supplier<EntityType<PrismarineArrow>> PRISMARINE_ARROW = register("prismarine_arrow",
            PrismarineArrow::new);
    public static final Supplier<EntityType<GlassArrow>> GLASS_ARROW = register("glass_arrow", GlassArrow::new);
    public static final Supplier<EntityType<AmethystArrow>> AMETHYST_ARROW = register("amethyst_arrow",
            AmethystArrow::new);
    public static final Supplier<EntityType<MiningArrow>> MINING_ARROW = register("mining_arrow",
            MiningArrow::new);
    public static final Supplier<EntityType<HealingArrow>> HEALING_ARROW = register("healing_arrow",
            HealingArrow::new);
    public static final Supplier<EntityType<EndArrow>> END_ARROW = register("end_arrow", EndArrow::new);
    public static final Supplier<EntityType<TeleportArrow>> TELEPORT_ARROW = register("teleport_arrow",
            TeleportArrow::new);
    public static final Supplier<EntityType<DragonArrow>> DRAGON_ARROW = register("dragon_arrow",
            DragonArrow::new);
    public static final Supplier<EntityType<UraniumRod>> URANIUM_ROD = register("uranium_rod",
            UraniumRod::new);
    // #endregion Arrows

    // #region Bullets
    public static final Supplier<EntityType<ESBullet>> HANDGUN_BULLET = register("handgun_bullet", ESBullet::new);
    public static final Supplier<EntityType<ESBullet>> HEAVY_HANDGUN_BULLET = register("heavy_handgun_bullet",
            ESBullet::new);
    public static final Supplier<EntityType<ItemBullet>> ITEM_BULLET = ENTITIES.register("item_bullet",
            () -> EntityType.Builder.<ItemBullet>of(ItemBullet::new, MobCategory.MISC).sized(.5F, .5F)
                    .clientTrackingRange(4).setUpdateInterval(10).build(ExtraStuck.MODID + ":item_bullet"));
    // #endregion Bullets

    public static final Supplier<EntityType<ThrownBeegg>> THROWN_BEEGG = ENTITIES.register("thrown_beegg",
            () -> EntityType.Builder.<ThrownBeegg>of(ThrownBeegg::new, MobCategory.MISC).sized(.25F, .25F)
                    .clientTrackingRange(4).updateInterval(10).build(ExtraStuck.MODID + ":thrown_beegg"));

    private static <T extends AbstractArrow> Supplier<EntityType<T>> register(String name, EntityFactory<T> fac) {
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
        list.add(TELEPORT_ARROW.get());
        list.add(DRAGON_ARROW.get());
        return list;
    }
}
