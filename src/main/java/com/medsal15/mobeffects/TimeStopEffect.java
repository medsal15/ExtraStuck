package com.medsal15.mobeffects;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.neoforged.neoforge.common.NeoForgeMod;

public class TimeStopEffect extends MobEffect {
    private static final ResourceLocation ID_MOVEMENT_SPEED = ExtraStuck.modid("time_stop_move");
    private static final ResourceLocation ID_FLY_SPEED = ExtraStuck.modid("time_stop_fly");
    private static final ResourceLocation ID_SWIM_SPEED = ExtraStuck.modid("time_stop_swim");
    private static final ResourceLocation ID_KB_RESIST = ExtraStuck.modid("time_stop_knockback");
    private static final ResourceLocation ID_ATTACK_SPEED = ExtraStuck.modid("time_stop_attack");

    public TimeStopEffect(MobEffectCategory category, int color) {
        super(category, color);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, ID_MOVEMENT_SPEED, -1F, Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.FLYING_SPEED, ID_FLY_SPEED, -1F, Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(NeoForgeMod.SWIM_SPEED, ID_SWIM_SPEED, -1F, Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ID_KB_RESIST, 1F, Operation.ADD_VALUE);
        addAttributeModifier(Attributes.ATTACK_SPEED, ID_ATTACK_SPEED, -1F, Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(@Nonnull LivingEntity entity, int amplifier) {
        entity.setDeltaMovement(0, 0, 0);

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
