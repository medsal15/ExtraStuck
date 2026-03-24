package com.medsal15.entities;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.ExtraStuck;
import com.medsal15.data.ESLootTableProvider;
import com.medsal15.items.tools.LandFishingRod;
import com.medsal15.utils.ESTags;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mraof.minestuck.world.MSDimensions;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

// Mostly a copy of Fishing Hook
public class LandFishingHook extends FishingHook implements IEntityWithComplexSpawn {
    private static final EntityDataAccessor<Integer> DATA_HOOKED_ENTITY = SynchedEntityData
            .defineId(LandFishingHook.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_BITING = SynchedEntityData.defineId(LandFishingHook.class,
            EntityDataSerializers.BOOLEAN);

    private final RandomSource syncronizedRandom = RandomSource.create();
    private boolean bitingFish;
    private int luck;
    private int lureSpeed;
    private int life;
    private int nibble;
    private int timeUntilLure;
    private int timeUntilHook;
    private int outOfFluidTime;
    private float fishAngle;
    private boolean openFluid = true;
    @Nullable
    private Entity hookedEntity;
    private State currentState = State.FLYING;

    public LandFishingHook(EntityType<? extends LandFishingHook> type, Level level) {
        super(type, level);
    }

    public LandFishingHook(Player player, Level level, int luck, int lureSpeed) {
        super(player, level, luck, lureSpeed);
        this.luck = Math.max(0, luck);
        this.lureSpeed = Math.max(0, lureSpeed);
    }

    @Override
    public void onSyncedDataUpdated(@Nonnull EntityDataAccessor<?> key) {
        if (DATA_HOOKED_ENTITY.equals(key)) {
            int id = getEntityData().get(DATA_HOOKED_ENTITY);
            hookedEntity = id > 0 ? level().getEntity(id - 1) : null;
        }
        if (DATA_BITING.equals(key)) {
            bitingFish = getEntityData().get(DATA_BITING);
            if (bitingFish) {
                setDeltaMovement(getDeltaMovement().x, (double) (-.4F * Mth.nextFloat(random, .6F, 1F)),
                        getDeltaMovement().z);
            }
        }

        super.onSyncedDataUpdated(key);
    }

    @Override
    protected void defineSynchedData(@Nonnull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HOOKED_ENTITY, 0);
        builder.define(DATA_BITING, false);
    }

    private boolean shouldStopFishing(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        boolean holding = mainHand.getItem() instanceof LandFishingRod || offHand.getItem() instanceof LandFishingRod;
        if (!player.isRemoved() && player.isAlive() && holding && distanceToSqr(player) <= 1024) {
            return false;
        } else {
            discard();
            return true;
        }
    }

    @Override
    public void tick() {
        syncronizedRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level().getGameTime());
        Player player = this.getPlayerOwner();
        if (player == null) {
            discard();
            return;
        }

        if (level().isClientSide || !shouldStopFishing(player)) {
            if (onGround()) {
                life++;
                if (life >= 1200) {
                    discard();
                    return;
                }
            } else {
                life = 0;
            }

            float y = 0;
            BlockPos pos = blockPosition();
            FluidState fluidState = level().getFluidState(pos);
            if (fluidState.is(ESTags.Fluids.FISHABLE_FLUIDS)) {
                y = fluidState.getHeight(level(), pos);
            }

            boolean inFluid = y > 0;
            if (currentState == State.FLYING) {
                if (hookedEntity != null) {
                    setDeltaMovement(Vec3.ZERO);
                    currentState = State.HOOKED_ENTITY;
                    return;
                }

                if (inFluid) {
                    setDeltaMovement(getDeltaMovement().multiply(.3, .2, .3));
                    currentState = State.BOBBING;
                    return;
                }

                checkCollision();
            } else {
                if (currentState == State.HOOKED_ENTITY) {
                    Entity hookedEntity = this.hookedEntity;
                    if (hookedEntity != null) {
                        if (!hookedEntity.isRemoved() && hookedEntity.level().dimension() == level().dimension()) {
                            setPos(hookedEntity.getX(), hookedEntity.getY(.8), hookedEntity.getZ());
                        } else {
                            setHookedEntity(null);
                            currentState = State.FLYING;
                        }
                    }

                    return;
                }

                if (currentState == State.BOBBING) {
                    Vec3 deltaMovement = getDeltaMovement();
                    double relativeY = getY() + deltaMovement.y - pos.getY() - y;
                    if (Math.abs(relativeY) < .01) {
                        relativeY += Math.signum(relativeY) * .1;
                    }

                    setDeltaMovement(deltaMovement.x * .9, deltaMovement.y - relativeY * random.nextFloat() * .2,
                            deltaMovement.z * .9);
                    if (nibble <= 0 && timeUntilHook <= 0) {
                        openFluid = true;
                    } else {
                        openFluid = openFluid && outOfFluidTime < 10 && calculateOpenFluid(pos);
                    }

                    if (inFluid) {
                        outOfFluidTime = Math.max(0, outOfFluidTime - 1);

                        if (bitingFish) {
                            setDeltaMovement(getDeltaMovement().add(0,
                                    -.1 * syncronizedRandom.nextFloat() * syncronizedRandom.nextFloat(), 0));
                        }

                        if (!level().isClientSide) {
                            catchingFish(pos);
                        }
                    } else {
                        outOfFluidTime = Math.min(10, outOfFluidTime + 1);
                    }
                }
            }

            if (!fluidState.is(ESTags.Fluids.FISHABLE_FLUIDS)) {
                setDeltaMovement(getDeltaMovement().add(0, -.03, 0));
            }

            move(MoverType.SELF, getDeltaMovement());
            updateRotation();

            if (currentState == State.FLYING && (onGround() || horizontalCollision)) {
                setDeltaMovement(Vec3.ZERO);
            }

            setDeltaMovement(getDeltaMovement().scale(.92));
            reapplyPosition();
        }
    }

    private void checkCollision() {
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitResult.getType() == HitResult.Type.MISS
                || !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitResult))
            onHit(hitResult);
    }

    @Override
    protected boolean canHitEntity(@Nonnull Entity target) {
        return super.canHitEntity(target) || target.isAlive() && target instanceof ItemEntity;
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide) {
            setHookedEntity(result.getEntity());
        }
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult result) {
        super.onHitBlock(result);
        setDeltaMovement(getDeltaMovement().normalize().scale(result.distanceTo(this)));
    }

    private boolean calculateOpenFluid(BlockPos pos) {
        FishType type = FishType.INVALID;

        for (int y = -1; y <= 2; y++) {
            FishType newType = getOpenFluidTypeForArea(pos.offset(-2, y, -2), pos.offset(2, y, 2));
            switch (newType) {
                case INVALID:
                    return false;
                case ABOVE_FLUID:
                    if (type == FishType.INVALID)
                        return false;
                    break;
                case INSIDE_FLUID:
                    if (type == FishType.ABOVE_FLUID)
                        return false;
                    break;
            }

            type = newType;
        }

        return true;
    }

    private FishType getOpenFluidTypeForArea(BlockPos pos1, BlockPos pos2) {
        return BlockPos.betweenClosedStream(pos1, pos2).map(this::getOpenFluidTypeForBlock)
                .reduce((a, b) -> a == b ? a : FishType.INVALID).orElse(FishType.INVALID);
    }

    private FishType getOpenFluidTypeForBlock(BlockPos pos) {
        BlockState state = level().getBlockState(pos);
        if (!state.isAir()) {
            FluidState fluidState = state.getFluidState();
            return fluidState.is(ESTags.Fluids.FISHABLE_FLUIDS) && fluidState.isSource()
                    && state.getCollisionShape(level(), pos).isEmpty() ? FishType.INSIDE_FLUID : FishType.INVALID;
        }
        return FishType.ABOVE_FLUID;
    }

    private void catchingFish(BlockPos pos) {
        ServerLevel level = (ServerLevel) level();
        int i = 1;
        BlockPos above = pos.above();

        if (random.nextFloat() < .25 && level.isRainingAt(above))
            i++;
        if (random.nextFloat() < .5 && !level.canSeeSky(above))
            i--;

        if (nibble > 0) {
            nibble--;
            if (nibble <= 0) {
                timeUntilLure = 0;
                timeUntilHook = 0;
                getEntityData().set(DATA_BITING, false);
            }
        } else if (timeUntilHook > 0) {
            timeUntilHook--;
            if (timeUntilHook > 0) {
                fishAngle = (float) (random.nextGaussian() * 4);
                float angle = fishAngle * (float) (Math.PI / 180);
                float sin = Mth.sin(angle);
                float cos = Mth.cos(angle);
                double x = getX() + sin * timeUntilHook * .1;
                double y = Mth.floor(getY()) + 1D;
                double z = getZ() + cos * timeUntilHook * .1;
                BlockState state = level.getBlockState(BlockPos.containing(x, y - 1, z));
                if (!state.isAir() && level.getFluidState(pos).is(ESTags.Fluids.FISHABLE_FLUIDS)) {
                    if (random.nextFloat() < .15) {
                        level.sendParticles(ParticleTypes.CRIT, x, y - .1, z, 1, sin, .1, cos, 0);
                    }

                    float fsin = sin * .04F;
                    float fcos = cos * .04F;
                    level.sendParticles(ParticleTypes.FISHING, x, y, z, 0, fcos, .01, -fsin, 1);
                    level.sendParticles(ParticleTypes.FISHING, x, y, z, 0, -fcos, .01, fsin, 1);
                }
            } else {
                playSound(SoundEvents.FISHING_BOBBER_SPLASH, .25F, 1 + (random.nextFloat() - random.nextFloat()) * .4F);
                double y = getY() + .5;
                level.sendParticles(ParticleTypes.BUBBLE, getX(), y, getZ(), (int) (1F + getBbWidth() * 20F),
                        getBbWidth(), 0, getBbWidth(), .2);
                level.sendParticles(ParticleTypes.FISHING, getX(), y, getZ(), (int) (1F + getBbWidth() * 20F),
                        getBbWidth(), 0, getBbWidth(), .2);
                nibble = Mth.nextInt(random, 20, 40);
                getEntityData().set(DATA_BITING, true);
            }
        } else if (timeUntilLure > 0) {
            timeUntilLure -= i;
            float chance = .15F;
            if (timeUntilLure < 20) {
                chance += (float) (20 - timeUntilLure) * .05F;
            } else if (timeUntilLure < 40) {
                chance += (float) (40 - timeUntilLure) * .02F;
            } else if (timeUntilLure < 60) {
                chance += (float) (60 - timeUntilLure) * .01F;
            }

            if (random.nextFloat() < chance) {
                float f1 = Mth.nextFloat(random, 0, 360) * (float) (Math.PI / 180);
                float f2 = Mth.nextFloat(random, 25, 60);
                double x = getX() + (double) (Mth.sin(f1) * f2) * .1;
                double y = (double) ((float) Mth.floor(getY()) + 1);
                double z = getZ() + (double) (Mth.cos(f1) * f2) * .1;
                BlockState state = level.getBlockState(BlockPos.containing(x, y - 1, z));
                if (!state.isAir() && level.getFluidState(pos).is(ESTags.Fluids.FISHABLE_FLUIDS)) {
                    level.sendParticles(ParticleTypes.SPLASH, x, y, z, 2 + random.nextInt(2), .1, 0, .1, 0);
                }
            }

            if (timeUntilLure <= 0) {
                fishAngle = Mth.nextFloat(random, 0, 360);
                timeUntilHook = Mth.nextInt(random, 20, 80);
            }
        } else {
            timeUntilLure = Mth.nextInt(random, 100, 600);
            timeUntilLure -= lureSpeed;
        }
    }

    @Override
    public int retrieve(@Nonnull ItemStack stack) {
        Player player = getPlayerOwner();
        if (!level().isClientSide && player != null && !shouldStopFishing(player)) {
            int damage = 0;
            net.neoforged.neoforge.event.entity.player.ItemFishedEvent event = null;
            if (hookedEntity != null) {
                pullEntity(hookedEntity);
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) player, stack, this,
                        Collections.emptyList());
                level().broadcastEntityEvent(this, (byte) 31);
                damage = hookedEntity instanceof ItemEntity ? 3 : 5;
            } else if (nibble > 0) {
                LootParams params = new LootParams.Builder((ServerLevel) level())
                        .withParameter(LootContextParams.ORIGIN, position())
                        .withParameter(LootContextParams.TOOL, stack)
                        .withParameter(LootContextParams.THIS_ENTITY, this)
                        .withParameter(LootContextParams.ATTACKING_ENTITY, Objects.requireNonNull(getOwner()))
                        .withLuck(luck + player.getLuck())
                        .create(LootContextParamSets.FISHING);

                ResourceKey<LootTable> lootTableId = null;
                if (MSDimensions.isLandDimension(getServer(), level().dimension())) {
                    lootTableId = ESLootTableProvider.TableSubProvider.LAND_FISHING;
                } else {
                    lootTableId = BuiltInLootTables.FISHING;
                }

                LootTable lootTable = Objects.requireNonNull(getServer()).reloadableRegistries()
                        .getLootTable(lootTableId);
                if (lootTable == null) {
                    discard();
                    return 0;
                }
                List<ItemStack> loot = lootTable.getRandomItems(params);
                event = new net.neoforged.neoforge.event.entity.player.ItemFishedEvent(loot, this.onGround() ? 2 : 1,
                        this);
                net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(event);
                if (event.isCanceled()) {
                    discard();
                    return event.getRodDamage();
                }
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) player, stack, this, loot);

                for (ItemStack item : loot) {
                    ItemEntity itemEntity = new ItemEntity(level(), getX(), getY(), getZ(), item) {
                        @Override
                        public boolean displayFireAnimation() {
                            return false;
                        }

                        @Override
                        public void lavaHurt() {
                        }

                        @Override
                        public boolean fireImmune() {
                            return true;
                        }
                    };
                    double x = player.getX() - getX();
                    double y = player.getY() - getY();
                    double z = player.getZ() - getZ();
                    double speed = .1;
                    itemEntity.setDeltaMovement(
                            x * speed,
                            y * speed + Math.sqrt(Math.sqrt(x * x + y * y + z * z)) * .08,
                            z * speed);
                    level().addFreshEntity(itemEntity);
                    player.level().addFreshEntity(
                            new ExperienceOrb(player.level(), player.getX(), player.getY() + .5, player.getZ() + .5,
                                    random.nextInt(6) + 1));
                    if (item.is(ItemTags.FISHES)) {
                        player.awardStat(Stats.FISH_CAUGHT, 1);
                    }
                }

                damage = 1;
            }

            if (onGround()) {
                damage = 2;
            }

            discard();
            if (event != null)
                return event.getRodDamage();
            return damage;
        } else {
            return 0;
        }
    }

    @Override
    public void onClientRemoval() {
        updateOwnerInfo(null);
    }

    @Override
    public void setOwner(@Nullable Entity owner) {
        super.setOwner(owner);
        updateOwnerInfo(this);
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double max = 64.0;
        return distance < (max * max);
    }

    @Override
    public void remove(@Nonnull RemovalReason reason) {
        updateOwnerInfo(null);
        super.remove(reason);
    }

    private void updateOwnerInfo(@Nullable LandFishingHook fishingHook) {
        Player player = getPlayerOwner();
        if (player != null) {
            player.fishing = fishingHook;
        }
    }

    private void setHookedEntity(@Nullable Entity entity) {
        hookedEntity = entity;
        getEntityData().set(DATA_HOOKED_ENTITY, entity == null ? 0 : entity.getId() + 1);
    }

    @Override
    public EntityType<?> getType() {
        return ESEntities.LAND_FISHING_HOOK.get();
    }

    @Override
    protected void pullEntity(@Nonnull Entity pulled) {
        Entity owner = getOwner();
        if (owner != null) {
            Vec3 delta = new Vec3(owner.getX() - getX(), owner.getY() - getY(), owner.getZ() - getZ()).scale(.1);
            pulled.setDeltaMovement(delta);
        }
    }

    @Override
    public void writeSpawnData(@Nonnull RegistryFriendlyByteBuf buffer) {
    }

    @Override
    public void readSpawnData(@Nonnull RegistryFriendlyByteBuf additionalData) {
    }

    public boolean isOpenFluid() {
        return openFluid;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public static enum State {
        FLYING,
        HOOKED_ENTITY,
        BOBBING;
    }

    public static enum FishType {
        ABOVE_FLUID,
        INSIDE_FLUID,
        INVALID;
    }

    public static class Renderer extends EntityRenderer<LandFishingHook> {
        private static final ResourceLocation TEXTURE_LOCATION = ExtraStuck.modid("textures/entity/fishing_hook.png");
        private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEXTURE_LOCATION);
        private static final double VIEW_BOBBING_SCALE = 960.0D;

        public Renderer(EntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public void render(@Nonnull LandFishingHook fishingHook, float entityYaw, float partialTick,
                @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int packedLight) {
            Player player = fishingHook.getPlayerOwner();

            if (player == null)
                return;

            poseStack.pushPose();
            poseStack.pushPose();
            poseStack.scale(.5F, .5F, .5F);
            poseStack.mulPose(entityRenderDispatcher.cameraOrientation());

            PoseStack.Pose poseHook = poseStack.last();
            VertexConsumer consumerHook = buffer.getBuffer(RENDER_TYPE);
            vertex(consumerHook, poseHook, packedLight, 0.0F, 0, 0, 1);
            vertex(consumerHook, poseHook, packedLight, 1.0F, 0, 1, 1);
            vertex(consumerHook, poseHook, packedLight, 1.0F, 1, 1, 0);
            vertex(consumerHook, poseHook, packedLight, 0.0F, 1, 0, 0);
            poseStack.popPose();
            float f = player.getAttackAnim(partialTick);
            float f1 = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
            Vec3 handPos = getPlayerHandPos(player, f1, partialTick);
            Vec3 hookPos = fishingHook.getPosition(partialTick).add(0, .25, 0);
            float x = (float) (handPos.x - hookPos.x);
            float y = (float) (handPos.y - hookPos.y);
            float z = (float) (handPos.z - hookPos.z);

            VertexConsumer consumerString = buffer.getBuffer(RenderType.lineStrip());
            PoseStack.Pose poseString = poseStack.last();
            for (int i = 0; i <= 16; i++) {
                stringVertex(x, y, z, consumerString, poseString, fraction(i, 16), fraction(i + 1, 16));
            }

            poseStack.popPose();
            super.render(fishingHook, entityYaw, partialTick, poseStack, buffer, packedLight);
        }

        private Vec3 getPlayerHandPos(Player player, float f, float partialTick) {
            int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
            ItemStack stack = player.getMainHandItem();
            if (!(stack.getItem() instanceof LandFishingRod))
                i = -i;

            if (entityRenderDispatcher.options.getCameraType().isFirstPerson()
                    && player == Minecraft.getInstance().player) {
                double scale = VIEW_BOBBING_SCALE / (double) (entityRenderDispatcher.options.fov().get().intValue());
                Vec3 vec3 = entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float) i * .525F, -.1F)
                        .scale(scale).yRot(f * .5F).xRot(f * .7F);
                return player.getEyePosition(partialTick).add(vec3);
            } else {
                float f1 = Mth.lerp(partialTick, player.yBodyRotO, player.yBodyRot) * (float) (Math.PI / 180.0);
                double sin = Mth.sin(f1);
                double cos = Mth.cos(f1);
                float playerScale = player.getScale();
                double scale1 = (double) i * .35 * (double) playerScale;
                double scale2 = 0.8 * (double) playerScale;
                float f2 = player.isCrouching() ? -0.1875F : 0.0F;
                return player.getEyePosition(partialTick).add(-cos * scale1 - sin * scale2,
                        (double) f2 - 0.45 * (double) playerScale, -sin * scale1 + cos * scale2);
            }
        }

        private static float fraction(int numerator, int denominator) {
            return (float) numerator / (float) denominator;
        }

        private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int packedLight, float x, int y, int u,
                int v) {
            consumer.addVertex(pose, x - 0.5F, (float) y - 0.5F, 0.0F)
                    .setColor(-1)
                    .setUv((float) u, (float) v)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(packedLight)
                    .setNormal(pose, 0.0F, 1.0F, 0.0F);
        }

        private static void stringVertex(
                float x, float y, float z, VertexConsumer consumer, PoseStack.Pose pose, float stringFraction,
                float nextStringFraction) {
            float f = x * stringFraction;
            float f1 = y * (stringFraction * stringFraction + stringFraction) * 0.5F + 0.25F;
            float f2 = z * stringFraction;
            float f3 = x * nextStringFraction - f;
            float f4 = y * (nextStringFraction * nextStringFraction + nextStringFraction) * 0.5F + 0.25F - f1;
            float f5 = z * nextStringFraction - f2;
            float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
            f3 /= f6;
            f4 /= f6;
            f5 /= f6;
            consumer.addVertex(pose, f, f1, f2).setColor(-16777216).setNormal(pose, f3, f4, f5);
        }

        /**
         * Returns the location of an entity's texture.
         */
        public ResourceLocation getTextureLocation(@Nonnull LandFishingHook entity) {
            return TEXTURE_LOCATION;
        }
    }
}
