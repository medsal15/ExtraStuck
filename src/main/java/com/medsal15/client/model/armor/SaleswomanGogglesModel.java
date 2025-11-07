package com.medsal15.client.model.armor;

import com.medsal15.ExtraStuck;
import com.medsal15.items.armor.SaleswomanGogglesItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SaleswomanGogglesModel extends GeoModel<SaleswomanGogglesItem> {
    @Override
    public ResourceLocation getModelResource(SaleswomanGogglesItem animatable) {
        return ExtraStuck.modid("geo/saleswoman_glasses.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SaleswomanGogglesItem animatable) {
        return ExtraStuck.modid("textures/models/armor/saleswoman_glasses.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SaleswomanGogglesItem animatable) {
        return ExtraStuck.modid("animations/none.animation.json");
    }
}
