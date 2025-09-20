package com.medsal15.client.model.armor;

import com.medsal15.ExtraStuck;
import com.medsal15.items.armor.DarkKnightArmorItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DarkKnightArmorModel extends GeoModel<DarkKnightArmorItem> {
    @Override
    public ResourceLocation getModelResource(DarkKnightArmorItem animatable) {
        return ExtraStuck.modid("geo/dr_knight.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DarkKnightArmorItem animatable) {
        return ExtraStuck.modid("textures/models/armor/knight_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DarkKnightArmorItem animatable) {
        return ExtraStuck.modid("animations/none.animation.json");
    }
}
