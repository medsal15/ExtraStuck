package com.medsal15.client.model.armor;

import com.medsal15.ExtraStuck;
import com.medsal15.items.armor.ChefArmorItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ChefArmorModel extends GeoModel<ChefArmorItem> {
    @Override
    public ResourceLocation getModelResource(ChefArmorItem animatable) {
        return ExtraStuck.modid("geo/chef_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ChefArmorItem animatable) {
        return ExtraStuck.modid("textures/models/armor/chef_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ChefArmorItem animatable) {
        return ExtraStuck.modid("animations/none.animation.json");
    }
}
