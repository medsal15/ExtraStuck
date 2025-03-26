package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.entity.underling.UnderlingEntity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class CandyArrow extends AbstractArrow {
    public CandyArrow(EntityType<? extends CandyArrow> entityType, Level level) {
        super(entityType, level);
    }

    public CandyArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.CARDBOARD_ARROW.get(), shooter, level, pickup, weapon);
    }

    public CandyArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.CARDBOARD_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ESItems.SWEET_TOOTH.toStack();
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        if (ExtraStuck.isMinestuckLoaded) {

            var entity = result.getEntity();
            if (!(entity instanceof UnderlingEntity underling))
                return;
            underling.dropCandy = true;
        }
        super.onHitEntity(result);
    }

    public static class Renderer extends ArrowRenderer<CandyArrow> {
        public static final ResourceLocation TEXTURE = ResourceLocation
                .fromNamespaceAndPath(ExtraStuck.MODID, "textures/entity/candy_arrow.png");

        public Renderer(EntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public ResourceLocation getTextureLocation(@Nonnull CandyArrow entity) {
            return TEXTURE;
        }
    }
}
