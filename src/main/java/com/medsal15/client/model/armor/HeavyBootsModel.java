package com.medsal15.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class HeavyBootsModel {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0);
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-2.2F, 8.2F, -2.0F, 4.0F,
                5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.05F, 12.0F, 0.0F));

        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.8F, 8.2F, -2.0F, 4.0F,
                5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-0.05F, 12.0F, 0.0F));

        return LayerDefinition.create(mesh, 16, 9);
    }
}
