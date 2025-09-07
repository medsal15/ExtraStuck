package com.medsal15.client.model.armor;

import com.medsal15.ExtraStuck;
import com.medsal15.items.armor.LightKnightArmorItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LightKnightArmorModel extends GeoModel<LightKnightArmorItem> {
    @Override
    public ResourceLocation getModelResource(LightKnightArmorItem animatable) {
        return ExtraStuck.modid("geo/dr_knight.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LightKnightArmorItem animatable) {
        return ExtraStuck.modid("textures/models/armor/light_knight_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LightKnightArmorItem animatable) {
        return ExtraStuck.modid("animations/dr_knight.animation.json");
    }
}
