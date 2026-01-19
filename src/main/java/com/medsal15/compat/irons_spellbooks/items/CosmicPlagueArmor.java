package com.medsal15.compat.irons_spellbooks.items;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESArmorMaterials;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
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

public class CosmicPlagueArmor extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CosmicPlagueArmor(ArmorItem.Type slot, Properties properties) {
        super(ESArmorMaterials.COSMIC_PLAGUE, slot, properties);
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
            public <T extends LivingEntity> @Nullable HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity,
                    ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null)
                    renderer = new GeoArmorRenderer<>(new Model());
                return renderer;
            }
        });
    }

    public static class Model extends GeoModel<CosmicPlagueArmor> {
        @Override
        public ResourceLocation getModelResource(CosmicPlagueArmor animatable) {
            return ExtraStuck.modid("geo/cosmic_plague_armor.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(CosmicPlagueArmor animatable) {
            return ExtraStuck.modid("textures/models/armor/cosmic_plague.png");
        }

        @Override
        public ResourceLocation getAnimationResource(CosmicPlagueArmor animatable) {
            return ExtraStuck.modid("animations/none.animation.json");
        }
    }
}
