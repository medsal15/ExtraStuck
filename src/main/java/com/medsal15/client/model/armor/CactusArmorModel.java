package com.medsal15.client.model.armor;

import com.medsal15.ExtraStuck;
import com.medsal15.items.armor.CactusArmorItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CactusArmorModel extends GeoModel<CactusArmorItem> {
    @Override
    public ResourceLocation getModelResource(CactusArmorItem animatable) {
        return ExtraStuck.modid("geo/cactus_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CactusArmorItem animatable) {
        return ExtraStuck.modid("textures/models/armor/cactus_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CactusArmorItem animatable) {
        return ExtraStuck.modid("animations/none.animation.json");
    }
}
