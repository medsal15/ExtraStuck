package com.medsal15.client.renderers;

import javax.annotation.Nonnull;

import com.medsal15.blockentities.VendingMachineBlockEntity;
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

public class VendingMachineRenderer implements BlockEntityRenderer<VendingMachineBlockEntity> {
    private final ItemRenderer itemRenderer;

    public VendingMachineRenderer(BlockEntityRendererProvider.Context context) {
        super();

        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@Nonnull VendingMachineBlockEntity blockEntity, float partialTick, @Nonnull PoseStack poseStack,
            @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack vending = blockEntity.getItemHandler(null).getStackInSlot(VendingMachineBlockEntity.SLOT_STORAGE_OUT);
        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        if (!vending.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(.5, .5, .5);
            switch (direction) {
                case NORTH:
                    poseStack.translate(0, 0, -.2);
                    break;
                case SOUTH:
                    poseStack.translate(0, 0, .2);
                    break;
                case EAST:
                    poseStack.translate(.2, 0, 0);
                    break;
                case WEST:
                    poseStack.translate(-.2, 0, 0);
                    break;
                default:
            }
            poseStack.scale(.5F, .5F, .5F);
            poseStack.mulPose(
                    Axis.YP.rotationDegrees(-Direction.from2DDataValue((direction.get2DDataValue() + 2) % 4).toYRot()));
            itemRenderer.renderStatic(vending, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                    poseStack, bufferSource, blockEntity.getLevel(), (int) blockEntity.getBlockPos().asLong());
            poseStack.popPose();
        }
    }
}
