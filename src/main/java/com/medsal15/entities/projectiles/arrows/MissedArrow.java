package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class MissedArrow extends AbstractArrow {
    public MissedArrow(EntityType<? extends MissedArrow> entityType, Level level) {
        super(entityType, level);
    }

    public MissedArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.MISSED_ARROW.get(), shooter, level, pickup, weapon);
    }

    public MissedArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.MISSED_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.MISSED_YOU.get());
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        return;
    }

    public static class MARenderer extends ArrowRenderer<MissedArrow> {
        public static final ResourceLocation TEXTURE = ResourceLocation
                .fromNamespaceAndPath(ExtraStuck.MODID, "textures/entity/missed_arrow.png");

        public MARenderer(EntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public ResourceLocation getTextureLocation(@Nonnull MissedArrow entity) {
            return TEXTURE;
        }
    }
}
