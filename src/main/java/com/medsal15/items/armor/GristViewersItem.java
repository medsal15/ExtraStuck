package com.medsal15.items.armor;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.medsal15.ExtraStuck;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GristViewersItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public GristViewersItem(Holder<ArmorMaterial> material, ArmorItem.Type slot, Properties properties) {
        super(material, slot, properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "idle", 0,
                state -> state.setAndContinue(DefaultAnimations.IDLE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity,
                    ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null)
                    renderer = new GeoArmorRenderer<>(new Model());
                return renderer;
            }
        });
    }

    public static class Model extends GeoModel<GristViewersItem> {
        @Override
        public ResourceLocation getModelResource(GristViewersItem animatable) {
            return ExtraStuck.modid("geo/grist_viewers.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(GristViewersItem animatable) {
            return ExtraStuck.modid("textures/models/armor/grist_viewers.png");
        }

        @Override
        public ResourceLocation getAnimationResource(GristViewersItem animatable) {
            return ExtraStuck.modid("animations/none.animation.json");
        }
    }
}
