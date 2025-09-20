package com.medsal15.client.model.armor;

import com.medsal15.ExtraStuck;
import com.medsal15.items.armor.SalesmanGogglesItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SalesmanGogglesModel extends GeoModel<SalesmanGogglesItem> {
    @Override
    public ResourceLocation getModelResource(SalesmanGogglesItem animatable) {
        return ExtraStuck.modid("geo/salesman_goggles.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SalesmanGogglesItem animatable) {
        return ExtraStuck.modid("textures/models/armor/salesman_goggles.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SalesmanGogglesItem animatable) {
        return ExtraStuck.modid("animations/none.animation.json");
    }
}
