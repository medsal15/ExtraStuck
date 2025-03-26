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

public class NetherArrow extends AbstractArrow {
    public NetherArrow(EntityType<? extends NetherArrow> entityType, Level level) {
        super(entityType, level);
    }

    public NetherArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.NETHER_ARROW.get(), shooter, level, pickup, weapon);
    }

    public NetherArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.NETHER_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.NETHER_ARROW.get());
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        var entity = result.getEntity();
        var baseDamage = getBaseDamage();
        if (entity.getRemainingFireTicks() > 0) {
            setBaseDamage(baseDamage * 1.5);
        }
        super.onHitEntity(result);

        setBaseDamage(baseDamage);
    }

    public static class NARenderer extends ArrowRenderer<NetherArrow> {
        public static final ResourceLocation TEXTURE = ResourceLocation
                .fromNamespaceAndPath(ExtraStuck.MODID, "textures/entity/nether_arrow.png");

        public NARenderer(EntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public ResourceLocation getTextureLocation(@Nonnull NetherArrow entity) {
            return TEXTURE;
        }
    }
}
