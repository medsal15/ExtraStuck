package com.medsal15.entities.projectiles.orbs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Quaternionf;

import com.medsal15.ExtraStuck;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public abstract class OrbEntity extends Projectile {
    protected static final EntityDataAccessor<Integer> LIFE_DATA = SynchedEntityData.defineId(OrbEntity.class,
            EntityDataSerializers.INT);

    protected int life = 0;

    public OrbEntity(EntityType<? extends OrbEntity> type, Level level) {
        super(type, level);
        setNoGravity(true);
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("life", life);
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        life = tag.getInt("life");
    }

    @Override
    protected void defineSynchedData(@Nonnull Builder builder) {
        builder.define(LIFE_DATA, 0);
    }

    @Override
    public boolean canBeHitByProjectile() {
        return true;
    }

    @Override
    public void handleEntityEvent(byte id) {
        // Removes poofing particles
        if (id != 60)
            super.handleEntityEvent(id);
    }

    @Override
    public boolean canCollideWith(@Nonnull Entity entity) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public static AttributeSupplier.Builder defaultAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1)
                .add(Attributes.GRAVITY, 0);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (!level().isClientSide) {
            destroy(false);
        }

        return true;
    }

    /**
     * Gets the EntityRayTraceResult representing the entity hit
     * <p>
     * Copy of AbstractArrow's
     */
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return ProjectileUtil.getEntityHitResult(
                this.level(), this, startVec, endVec,
                this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), this::canHitEntity);
    }

    @Override
    public void tick() {
        super.tick();

        // Handle hitting
        Vec3 movement = getDeltaMovement();
        Vec3 position = position();
        Vec3 newPos = position.add(movement);
        HitResult hitResult = level()
                .clip(new ClipContext(position, newPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitResult.getType() != HitResult.Type.MISS) {
            newPos = hitResult.getLocation();
        }
        while (!isRemoved()) {
            EntityHitResult entityHitResult = findHitEntity(position, newPos);
            if (entityHitResult != null)
                hitResult = entityHitResult;

            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                Entity hit = ((EntityHitResult) hitResult).getEntity();
                Entity owner = getOwner();
                if (hit instanceof Player hitPlayer && owner instanceof Player ownerPlayer
                        && !ownerPlayer.canHarmPlayer(hitPlayer)) {
                    hitResult = null;
                    entityHitResult = null;
                }
            }
            if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
                if (EventHooks.onProjectileImpact(this, hitResult))
                    break;
                ProjectileDeflection deflection = hitTargetOrDeflectSelf(hitResult);
                hasImpulse = true;
                if (deflection != ProjectileDeflection.NONE)
                    break;
            }

            if (entityHitResult == null)
                break;

            hitResult = null;
        }
        // Handle moving
        setPos(getX() + movement.x, getY() + movement.y, getZ() + movement.z);
        checkInsideBlocks();

        // Handle end of life
        life++;
        if (life >= maxLife() && !isRemoved()) {
            destroy(true);
        }
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        destroy(true);
    }

    protected int maxLife() {
        return 200;
    }

    protected void destroy(boolean activate) {
        if (activate)
            onEndOfLife();
        discard();
    }

    protected abstract void onEndOfLife();

    public abstract ResourceLocation getTextureLocation();

    public boolean fullBright() {
        return false;
    }

    /**
     * Generic OrbEntity renderer
     * <p>
     * Mostly a copy of EndCrystalRenderer, but without the base nor the glass
     */
    public static class Renderer<T extends OrbEntity> extends EntityRenderer<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
                ResourceLocation.fromNamespaceAndPath(ExtraStuck.MODID, "orb_entity"), "main");
        private static final float SIN_45 = (float) Math.sin(Math.PI / 4);

        private final ModelPart cube;

        public Renderer(EntityRendererProvider.Context context) {
            super(context);

            ModelPart part = context.bakeLayer(LAYER_LOCATION);
            cube = part.getChild("cube");
        }

        @Override
        public ResourceLocation getTextureLocation(@Nonnull T entity) {
            return entity.getTextureLocation();
        }

        @Override
        public void render(@Nonnull T orb, float yaw, float partialTicks, @Nonnull PoseStack poseStack,
                @Nonnull MultiBufferSource buffer, int packedLight) {
            renderCube(orb, partialTicks, poseStack, buffer, packedLight);
            // TODO figure out a way to render the symbol in front of the orb

            super.render(orb, yaw, partialTicks, poseStack, buffer, packedLight);
        }

        protected void renderCube(@Nonnull T orb, float partialTicks, @Nonnull PoseStack poseStack,
                @Nonnull MultiBufferSource buffer, int packedLight) {
            float life = ((float) orb.life + partialTicks) * 3.0F;
            if (orb.fullBright())
                packedLight = LightTexture.FULL_BRIGHT;

            poseStack.pushPose();
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(orb)));
            poseStack.translate(0, .5f, 0);
            poseStack.scale(0.875F, 0.875F, 0.875F);
            poseStack.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SIN_45, 0.0F, SIN_45));
            poseStack.mulPose(Axis.YP.rotationDegrees(life));
            poseStack.mulPose(Axis.XP.rotationDegrees(life));
            cube.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshDefinition = new MeshDefinition();
            PartDefinition partDefinition = meshDefinition.getRoot();
            partDefinition.addOrReplaceChild("cube",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-4f, -4f, -4f, 8.0F, 8.0F, 8.0F),
                    PartPose.ZERO);
            return LayerDefinition.create(meshDefinition, 32, 16);
        }
    }
}
