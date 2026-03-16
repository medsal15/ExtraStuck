package com.medsal15.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

/**
 * Copied from Iron's Spells & Spellbooks
 */
public class DyableArmorRenderer<T extends Item & GeoItem> extends GeoArmorRenderer<T> {
    public DyableArmorRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight,
            int packedOverlay, int color) {
        color = 0xFFFFFFFF;
        if (bone.getName().startsWith("dye") && currentStack != null) {
            color = Minecraft.getInstance().getItemColors().getColor(currentStack, 0) | 0xFF000000;
        }
        super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, color);
    }
}
