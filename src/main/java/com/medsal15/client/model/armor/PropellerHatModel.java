package com.medsal15.client.model.armor;

import com.medsal15.ExtraStuck;
import com.medsal15.items.armor.PropellerHatItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PropellerHatModel extends GeoModel<PropellerHatItem> {
    @Override
    public ResourceLocation getModelResource(PropellerHatItem animatable) {
        return ExtraStuck.modid("geo/propeller_hat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PropellerHatItem animatable) {
        return ExtraStuck.modid("textures/models/armor/propeller_hat.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PropellerHatItem animatable) {
        return ExtraStuck.modid("animations/propeller_hat.animation.json");
    }
}
