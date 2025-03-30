package com.medsal15.entities;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class ESArrowRenderer extends ArrowRenderer<AbstractArrow> {
    private ResourceLocation texture;

    public ESArrowRenderer(EntityRendererProvider.Context context, ResourceLocation texture) {
        super(context);
        this.texture = texture;
    }

    @Override
    public ResourceLocation getTextureLocation(@Nonnull AbstractArrow entity) {
        return texture;
    }
}
