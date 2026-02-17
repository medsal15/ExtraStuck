package com.medsal15.items;

import javax.annotation.Nonnull;

import com.medsal15.config.ConfigServer;
import com.medsal15.mobeffects.ESMobEffects;
import com.medsal15.utils.ESTags;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CosmicPlagueSporeItem extends Item {
    public CosmicPlagueSporeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slotId,
            boolean isSelected) {
        if (level.isClientSide)
            return;

        if (ConfigServer.COSMIC_SPORE_DIMENSIONS.get().contains(level.dimension().location().toString()) ||
                level.dimensionTypeRegistration().is(ESTags.DimensionTypes.COSMIC_DIMENSION_TYPES)) {
            if (entity instanceof LivingEntity livingEntity) {
                MobEffectInstance effect;
                if (ConfigServer.COSMIC_SPORE_HARDMODE.getAsBoolean()) {
                    effect = new MobEffectInstance(ESMobEffects.COSMIC_PLAGUE, 100, 1);
                } else {
                    effect = new MobEffectInstance(MobEffects.POISON, 100, 4);
                }
                livingEntity.addEffect(effect);
            }
        }
    }
}
