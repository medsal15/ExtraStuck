package com.medsal15.entities.projectiles;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.ESDamageTypes;
import com.medsal15.ExtraStuck;
import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

// half of this class is copied from trident-related classes
public class CaptainJusticeShield extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> FOIL_ACCESSOR = SynchedEntityData.defineId(
            CaptainJusticeShield.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> LOYALTY_ACCESSOR = SynchedEntityData.defineId(
            CaptainJusticeShield.class, EntityDataSerializers.BYTE);
    private boolean dealtDamage;
    public int clientSideReturnTridentTickCount;

    public CaptainJusticeShield(EntityType<? extends CaptainJusticeShield> entityType, Level level) {
        super(entityType, level);
    }

    public CaptainJusticeShield(Level level, LivingEntity shooter, ItemStack pickup) {
        super(ESEntities.CAPTAIN_JUSTICE_SHIELD.get(), shooter, level, pickup, null);
        this.entityData.set(FOIL_ACCESSOR, pickup.hasFoil());
        this.entityData.set(LOYALTY_ACCESSOR, getLoyaltyFromItem(pickup));
    }

    public CaptainJusticeShield(Level level, double x, double y, double z, ItemStack pickup) {
        super(ESEntities.CAPTAIN_JUSTICE_SHIELD.get(), x, y, z, level, pickup, pickup);
        this.entityData.set(FOIL_ACCESSOR, pickup.hasFoil());
        this.entityData.set(LOYALTY_ACCESSOR, getLoyaltyFromItem(pickup));
    }

    @Override
    protected void defineSynchedData(@Nonnull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FOIL_ACCESSOR, false);
        builder.define(LOYALTY_ACCESSOR, (byte) 0);
    }

    public boolean isFoil() {
        return this.entityData.get(FOIL_ACCESSOR);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        int i = this.entityData.get(LOYALTY_ACCESSOR);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * (double) i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05 * (double) i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                this.clientSideReturnTridentTickCount++;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        return entity == null || !entity.isAlive() ? false : !(entity instanceof ServerPlayer) || !entity.isSpectator();
    }

    /**
     * Gets the EntityHitResult representing the entity hit
     */
    @Nullable
    @Override
    protected EntityHitResult findHitEntity(@Nonnull Vec3 startVec, @Nonnull Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        Entity entity = result.getEntity();
        float f = 8.0F;
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().source(ESDamageTypes.CAPTAIN_JUSTICE_PROJECTILE, this,
                (Entity) (entity1 == null ? this : entity1));
        if (this.level() instanceof ServerLevel serverlevel) {
            f = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), entity, damagesource, f);
        }

        this.dealtDamage = true;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (this.level() instanceof ServerLevel serverlevel1) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverlevel1, entity, damagesource,
                        this.getWeaponItem());
            }

            if (entity instanceof LivingEntity livingentity) {
                this.doKnockback(livingentity, damagesource);
                this.doPostHurtEffects(livingentity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

    private byte getLoyaltyFromItem(ItemStack stack) {
        return this.level() instanceof ServerLevel serverlevel
                ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverlevel, stack, this), 0,
                        127)
                : 0;
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

        public static LayerDefinition createLayer() {
            MeshDefinition mesh = new MeshDefinition();
            PartDefinition root = mesh.getRoot();
            PartDefinition shield = root.addOrReplaceChild("main",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-6F, 0F, -6F, 12F, 1F, 12F), PartPose.ZERO);
            shield.addOrReplaceChild("spike_d1",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-.5F, -0.05F, -9F, 1.1F, 1.1F, 2F),
                    PartPose.rotation(0, (float) (Math.PI * -.25), 0));
            shield.addOrReplaceChild("spike_d2",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-.5F, -0.05F, -9F, 1.1F, 1.1F, 2F),
                    PartPose.rotation(0, (float) (Math.PI * .25), 0));
            shield.addOrReplaceChild("spike_d3",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-.5F, -0.05F, -9F, 1.1F, 1.1F, 2F),
                    PartPose.rotation(0, (float) (Math.PI * .75), 0));
            shield.addOrReplaceChild("spike_d4",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-.5F, -0.05F, -9F, 1.1F, 1.1F, 2F),
                    PartPose.rotation(0, (float) (Math.PI * -.75), 0));
            shield.addOrReplaceChild("spike_1",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -0.05F, -7F, 1.1F, 1.1F, 2F),
                    PartPose.rotation(0, 0, 0));
            shield.addOrReplaceChild("spike_2",
                    CubeListBuilder.create().texOffs(0, 0).addBox(1.5F, -0.05F, -7F, 1.1F, 1.1F, 2F),
                    PartPose.rotation(0, 0, 0));
            shield.addOrReplaceChild("spike_3",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -0.05F, -7F, 1.1F, 1.1F, 2F),
                    PartPose.rotation(0, (float) (Math.PI / 2), 0));
            shield.addOrReplaceChild("spike_4",
                    CubeListBuilder.create().texOffs(0, 0).addBox(1.5F, -0.05F, -7F, 1.1F, 1.1F, 2F),
                    PartPose.rotation(0, (float) (Math.PI / 2), 0));
            shield.addOrReplaceChild("spike_5",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -0.05F, -7F, 1.1F, 1.1F, 2F),
                    PartPose.rotation(0, (float) Math.PI, 0));
            shield.addOrReplaceChild("spike_6",
                    CubeListBuilder.create().texOffs(0, 0).addBox(1.5F, -0.05F, -7F, 1.1F, 1.1F, 2F),
                    PartPose.rotation(0, (float) Math.PI, 0));
            shield.addOrReplaceChild("spike_7",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -0.05F, -7F, 1.1F, 1.1F, 2F),
                    PartPose.rotation(0, (float) -(Math.PI / 2), 0));
            shield.addOrReplaceChild("spike_8",
                    CubeListBuilder.create().texOffs(0, 0).addBox(1.5F, -0.05F, -7F, 1.1F, 1.1F, 2F),
                    PartPose.rotation(0, (float) -(Math.PI / 2), 0));

            return LayerDefinition.create(mesh, 48, 13);
        }

        @Override
        public void renderToBuffer(@Nonnull PoseStack poseStack, @Nonnull VertexConsumer buffer, int packedLight,
                int packedOverlay, int color) {
            this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
        }
    }
}
