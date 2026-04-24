package com.medsal15.compat.create.potatoCannon;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.api.equipment.potatoCannon.PotatoProjectileEntityHitAction;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;

import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESPotatoEntityHitActions {
    public static final DeferredRegister<MapCodec<? extends PotatoProjectileEntityHitAction>> POTATO_PROJECTILE_ENTITY_HIT_ACTIONS = DeferredRegister
            .create(CreateBuiltInRegistries.POTATO_PROJECTILE_ENTITY_HIT_ACTION, ExtraStuck.MODID);

    public static final Supplier<MapCodec<? extends PotatoProjectileEntityHitAction>> EXPLODE = POTATO_PROJECTILE_ENTITY_HIT_ACTIONS
            .register("explode", () -> ExplodeE.CODEC);

    public record ExplodeE(float radius) implements PotatoProjectileEntityHitAction {
        public static final MapCodec<ExplodeE> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                ExtraCodecs.POSITIVE_FLOAT.fieldOf("radius").forGetter(ExplodeE::radius)).apply(inst, ExplodeE::new));

        @Override
        public boolean execute(ItemStack projectile, EntityHitResult ray, Type type) {
            var entity = ray.getEntity();
            entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), radius,
                    ExplosionInteraction.NONE);
            return false;
        }

        @Override
        public MapCodec<? extends PotatoProjectileEntityHitAction> codec() {
            return CODEC;
        }
    }
}
