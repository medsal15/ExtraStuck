package com.medsal15.entities.projectiles.magic.circles;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Matrix4f;

import com.medsal15.ExtraStuck;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;

public abstract class CircleEntity extends Entity implements TraceableEntity {
    protected static final EntityDataAccessor<Integer> LIFE_DATA = SynchedEntityData.defineId(CircleEntity.class,
            EntityDataSerializers.INT);

    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;

    protected int life = 0;

    public CircleEntity(EntityType<? extends CircleEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        if (ownerUUID != null) {
            compound.putUUID("owner", ownerUUID);
        }

        compound.putInt("life", life);
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        if (compound.hasUUID("owner")) {
            ownerUUID = compound.getUUID("owner");
            cachedOwner = null;
        }

        life = compound.getInt("life");
    }

    @Override
    @Nullable
    public Entity getOwner() {
        if (cachedOwner != null && !cachedOwner.isRemoved()) {
            return cachedOwner;
        } else if (level() instanceof ServerLevel serverlevel && ownerUUID != null) {
            cachedOwner = serverlevel.getEntity(ownerUUID);
            return cachedOwner;
        } else {
            return null;
        }
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            ownerUUID = entity.getUUID();
        } else {
            ownerUUID = null;
        }
        cachedOwner = entity;
    }

    public void setOwner(@Nullable UUID owner) {
        ownerUUID = owner;
        cachedOwner = null;
    }

    @Override
    protected void defineSynchedData(@Nonnull Builder builder) {
        builder.define(LIFE_DATA, 0);
    }

    @Override
    public boolean canCollideWith(@Nonnull Entity entity) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (!level().isClientSide) {
            discard();
        }

        return true;
    }

    @Override
    public void tick() {
        super.tick();

        // Handle end of life
        life++;
        if (life >= maxLife() && !isRemoved())
            discard();
    }

    public abstract int getColor();

    public boolean tintSymbol() {
        return false;
    }

    @Nullable
    public abstract ResourceLocation getSymbolLocation();

    public boolean fullBrightSymbol() {
        return false;
    }

    public int maxLife() {
        return 200;
    }

    /**
     * Generic CircleEntity renderer
     * <p>
     * Mostly a copy of GateRenderer, but with extra stuff
     */
    public static class CircleRenderer<T extends CircleEntity> extends EntityRenderer<T> {
        private static final ResourceLocation CIRCLE = ExtraStuck.modid("textures/entity/circle/base.png");

        public CircleRenderer(EntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public ResourceLocation getTextureLocation(@Nonnull T entity) {
            return CIRCLE;
        }

        @Override
        public void render(@Nonnull T circle, float yaw, float partialTick, @Nonnull PoseStack poseStack,
                @Nonnull MultiBufferSource bufferSource, int packedLight) {
            renderCircle(circle, partialTick, poseStack, bufferSource, packedLight);
            ResourceLocation symbol = circle.getSymbolLocation();
            if (symbol != null) {
                renderSymbol(circle, poseStack, bufferSource,
                        circle.fullBrightSymbol() ? LightTexture.FULL_BRIGHT : packedLight);
            }

            super.render(circle, yaw, partialTick, poseStack, bufferSource, packedLight);
        }

        protected void renderCircle(T circle, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                int packedLight) {
            float width = (float) circle.getBbWidth() / 2f;
            float y = (float) circle.getBbHeight() / 2f;
            int color = circle.getColor();
            int r = ((color >> 16) & 255);
            int g = ((color >> 8) & 255);
            int b = (color & 255);
            float rot = circle.level().getGameTime() + partialTicks;

            poseStack.pushPose();
            poseStack.scale(width, 1, width);
            poseStack.mulPose(Axis.YP.rotation(rot / 75));
            PoseStack.Pose pose = poseStack.last();
            Matrix4f matrix4f = pose.pose();
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(CIRCLE));
            consumer.addVertex(matrix4f, -1.5F, y, -1.5F).setColor(r, g, b, 255).setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix4f, -1.5F, y, 1.5F).setColor(r, g, b, 255).setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix4f, 1.5F, y, 1.5F).setColor(r, g, b, 255).setUv(1, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix4f, 1.5F, y, -1.5F).setColor(r, g, b, 255).setUv(1, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
            poseStack.popPose();
        }

        protected void renderSymbol(T circle, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
            float width = (float) circle.getBbWidth() / 4f;
            float y = (float) circle.getBbHeight() / 2f;
            int color = circle.tintSymbol() ? circle.getColor() : 0xFFFFFF;
            int r = ((color >> 16) & 255);
            int g = ((color >> 8) & 255);
            int b = (color & 255);

            poseStack.pushPose();
            poseStack.scale(width, 1, width);
            PoseStack.Pose pose = poseStack.last();
            Matrix4f matrix4f = pose.pose();
            VertexConsumer consumer = bufferSource
                    .getBuffer(RenderType.entityCutoutNoCull(Objects.requireNonNull(circle.getSymbolLocation())));
            consumer.addVertex(matrix4f, -1.5F, y, -1.5F).setColor(r, g, b, 255).setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix4f, -1.5F, y, 1.5F).setColor(r, g, b, 255).setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix4f, 1.5F, y, 1.5F).setColor(r, g, b, 255).setUv(1, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix4f, 1.5F, y, -1.5F).setColor(r, g, b, 255).setUv(1, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
            poseStack.popPose();
        }
    }
}
