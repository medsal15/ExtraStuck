package com.medsal15.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.medsal15.mobeffects.ESMobEffects;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(at = @At("HEAD"), method = "dampensVibrations", cancellable = true)
    public void dampensVibrations(CallbackInfoReturnable<Boolean> cb) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            boolean silent = false;
            if (livingEntity.hasEffect(ESMobEffects.SILENT))
                silent = true;
            cb.setReturnValue(silent);
        }
    }
}
