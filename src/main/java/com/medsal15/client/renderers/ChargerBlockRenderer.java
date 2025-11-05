package com.medsal15.client.renderers;

import javax.annotation.Nonnull;

import com.medsal15.blockentities.ChargerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ChargerBlockRenderer implements BlockEntityRenderer<ChargerBlockEntity> {
    private final ItemRenderer itemRenderer;

    public ChargerBlockRenderer(BlockEntityRendererProvider.Context context) {
        super();

        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@Nonnull ChargerBlockEntity blockEntity, float partialTick, @Nonnull PoseStack poseStack,
            @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack charging = blockEntity.getItemHandler(null).getStackInSlot(0);
        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        if (!charging.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(.5, .6, .5);
            poseStack.scale(.4F, .4F, .4F);
            poseStack.mulPose(
                    Axis.YP.rotationDegrees(-Direction.from2DDataValue((direction.get2DDataValue() + 2) % 4).toYRot()));
            itemRenderer.renderStatic(charging, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack,
                    bufferSource, blockEntity.getLevel(), (int) blockEntity.getBlockPos().asLong());
            poseStack.popPose();
        }
    }
}
