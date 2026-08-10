package com.medsal15.entities.projectiles.magic.orbs;

import java.util.List;

import com.medsal15.ExtraStuck;
import com.medsal15.entities.ESEntities;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LightOrb extends OrbEntity {
    public LightOrb(EntityType<? extends LightOrb> type, Level level) {
        super(type, level);
    }

    public LightOrb(Player player, Level level, double x, double y, double z) {
        super(ESEntities.LIGHT_ORB.get(), level);
        setOwner(player);
        setPos(x, y, z);
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return ExtraStuck.modid("textures/entity/orb/light.png");
    }

    @Override
    protected void onEndOfLife() {
        level().addParticle(ParticleTypes.FLASH, true, getX(), getY(), getZ(), 0, 0, 0);
        Entity owner = getOwner();
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.FIREWORK_ROCKET_TWINKLE,
                owner != null && owner instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE);

        // Apply effect to nearby entities
        int duration = 600;
        if (owner instanceof ServerPlayer serverPlayer && Title.isPlayerOfAspect(serverPlayer, EnumAspect.LIGHT))
            duration = 1200;
        List<Entity> entities = level().getEntities(this, getBoundingBox().inflate(5));
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity == owner)
                    continue;
                livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, duration));
            }
        }
    }

    @Override
    public boolean fullBright() {
        return true;
    }
}
