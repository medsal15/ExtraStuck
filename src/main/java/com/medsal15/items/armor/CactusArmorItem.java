package com.medsal15.items.armor;

import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.medsal15.client.model.armor.CactusArmorModel;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CactusArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CactusArmorItem(Holder<ArmorMaterial> material, ArmorItem.Type slot, Properties properties) {
        super(material, slot, properties);
    }

    @Override
    public int getEnchantmentLevel(@Nonnull ItemStack stack, @Nonnull Holder<Enchantment> enchantment) {
        int level = super.getEnchantmentLevel(stack, enchantment);

        if (enchantment.is(Enchantments.THORNS)) {
            level += 1;
        }

        return level;
    }

    @Override
    public ItemEnchantments getAllEnchantments(@Nonnull ItemStack stack, @Nonnull RegistryLookup<Enchantment> lookup) {
        ItemEnchantments list = super.getAllEnchantments(stack, lookup);
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(list);

        Optional<Reference<Enchantment>> echantment = lookup.get(Enchantments.THORNS);
        if (echantment.isPresent()) {
            mutable.upgrade(echantment.get(), 1);
        }

        return mutable.toImmutable();
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
                    renderer = new GeoArmorRenderer<>(new CactusArmorModel());
                return renderer;
            }
        });
    }
}
