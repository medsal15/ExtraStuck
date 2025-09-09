package com.medsal15.blocks.machine;

import javax.annotation.Nonnull;

import com.medsal15.blockentities.ESBlockEntities;
import com.medsal15.blockentities.ReactorBlockEntity;
import com.mraof.minestuck.block.machine.SmallMachineBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

public class ReactorBlock extends SmallMachineBlock<ReactorBlockEntity> {
    public ReactorBlock(Properties properties) {
        super(ESBlockShapes.NUCLEAR_REACTOR.createRotatedShapes(), ESBlockEntities.REACTOR, properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level,
            @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand,
            @Nonnull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ReactorBlockEntity reactor) {
            IFluidHandler reactorFH = reactor.getFluidHandler(hitResult.getDirection());
            @SuppressWarnings("null")
            IFluidHandler stackFH = Capabilities.FluidHandler.ITEM.getCapability(stack, null);
            if (reactorFH != null && stackFH != null) {
                FluidStack held = stackFH.getFluidInTank(0).copy();
                FluidStack stored = reactorFH.getFluidInTank(0).copy();
                if (!held.isEmpty() && reactorFH.isFluidValid(0, held)) {
                    // Fill from held item
                    int takenAmount = reactorFH.fill(held, FluidAction.SIMULATE);
                    FluidStack taken = stackFH.drain(takenAmount, FluidAction.SIMULATE);
                    if (takenAmount > 0 && taken.getAmount() == takenAmount) {
                        reactorFH.fill(held, FluidAction.EXECUTE);
                        if (stack.getItem() instanceof BucketItem) {
                            // Special handling for buckets
                            if (!player.isCreative()) {
                                stack.shrink(1);
                                ItemStack bucketStack = new ItemStack(Items.BUCKET);
                                if (!player.addItem(bucketStack)) {
                                    player.drop(bucketStack, false);
                                }
                            }
                        } else {
                            stackFH.drain(takenAmount, FluidAction.EXECUTE);
                        }
                        return ItemInteractionResult.CONSUME;
                    }
                } else if (!stored.isEmpty() && stackFH.isFluidValid(0, stored)) {
                    // TODO handle stacks of multiple buckets
                    // Drain to held item
                    int takenAmount = stackFH.fill(stored, FluidAction.SIMULATE);
                    FluidStack taken = reactorFH.drain(takenAmount, FluidAction.SIMULATE);
                    if (takenAmount > 0 && taken.getAmount() == takenAmount) {
                        reactorFH.drain(takenAmount, FluidAction.EXECUTE);
                        if (stack.getItem() instanceof BucketItem) {
                            // Special handling for buckets
                            if (!player.isCreative()) {
                                stack.shrink(1);
                                Fluid fluid = taken.getFluid();
                                Item bucket = fluid.getBucket();
                                ItemStack bucketStack = new ItemStack(bucket);
                                if (!player.addItem(bucketStack)) {
                                    player.drop(bucketStack, false);
                                }
                            }
                        } else {
                            stackFH.fill(taken, FluidAction.EXECUTE);
                        }
                        return ItemInteractionResult.CONSUME;
                    }
                }
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
