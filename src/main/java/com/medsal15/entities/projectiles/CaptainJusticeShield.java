package com.medsal15.entities.projectiles;

import javax.annotation.Nonnull;

import com.medsal15.ESEntities;
import com.medsal15.ESItems;
import com.medsal15.ExtraStuck;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// half of this class is copied from trident-related classes
public class CaptainJusticeShield extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> FOIL_ACCESSOR = SynchedEntityData.defineId(
            CaptainJusticeShield.class,
            EntityDataSerializers.BOOLEAN);

    public CaptainJusticeShield(EntityType<? extends CaptainJusticeShield> entityType, Level level) {
        super(entityType, level);
    }

    public CaptainJusticeShield(Level level, LivingEntity shooter, ItemStack pickup) {
        super(ESEntities.CAPTAIN_JUSTICE_SHIELD.get(), shooter, level, pickup, null);
        this.entityData.set(FOIL_ACCESSOR, pickup.hasFoil());
    }

    public CaptainJusticeShield(Level level, double x, double y, double z, ItemStack pickup) {
        super(ESEntities.CAPTAIN_JUSTICE_SHIELD.get(), x, y, z, level, pickup, pickup);
        this.entityData.set(FOIL_ACCESSOR, pickup.hasFoil());
    }

    @Override
    protected void defineSynchedData(@Nonnull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FOIL_ACCESSOR, false);
    }

    public boolean isFoil() {
        return this.entityData.get(FOIL_ACCESSOR);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
    }

    public static class CJSRenderer extends EntityRenderer<CaptainJusticeShield> {
        public static final ResourceLocation TEXTURE = ResourceLocation
                .fromNamespaceAndPath(ExtraStuck.MODID, "textures/entity/captain_justice_shield.png");
        private final CJSModel model;

        public CJSRenderer(EntityRendererProvider.Context context) {
            super(context);
            model = new CJSModel(context.bakeLayer(CJSModel.LAYER_LOCATION));
        }

        public void render(@Nonnull CaptainJusticeShield entity, float entityYaw, float partialTicks,
                @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int packedLight) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
            VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(
                    buffer, this.model.renderType(this.getTextureLocation(entity)), false, entity.isFoil());
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }

        @Override
        public ResourceLocation getTextureLocation(@Nonnull CaptainJusticeShield entity) {
            return TEXTURE;
        }
    }

    public static class CJSModel extends Model {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
                ResourceLocation.fromNamespaceAndPath(ExtraStuck.MODID, "captain_justice_shield"), "main");
        private final ModelPart root;

        public CJSModel(ModelPart root) {
            super(RenderType::entitySolid);
            this.root = root;
        }

        // todo support transparency
        public static LayerDefinition createLayer() {
            MeshDefinition mesh = new MeshDefinition();
            PartDefinition root = mesh.getRoot();
            // do not touch the main part, it's good
            PartDefinition shield = root.addOrReplaceChild("main",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-6F, 0F, -6F, 12F, 1F, 12F), PartPose.ZERO);
            // todo add spikes

            return LayerDefinition.create(mesh, 48, 13);
        }

        @Override
        public void renderToBuffer(@Nonnull PoseStack poseStack, @Nonnull VertexConsumer buffer, int packedLight,
                int packedOverlay, int color) {
            this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
        }
    }
}
