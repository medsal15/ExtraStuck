package com.medsal15.blocks.machine;

import javax.annotation.Nonnull;

import com.medsal15.blockentities.ESBlockEntities;
import com.medsal15.blockentities.VendingMachineBlockEntity;
import com.medsal15.blocks.ESBlockShapes;
import com.mraof.minestuck.block.machine.SmallMachineBlock;
import com.mraof.minestuck.blockentity.machine.IOwnable;
import com.mraof.minestuck.player.IdentifierHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class VendingMachineBlock extends SmallMachineBlock<VendingMachineBlockEntity> {
    public VendingMachineBlock(Properties properties) {
        super(ESBlockShapes.FULL_BLOCK.createRotatedShapes(), ESBlockEntities.SMALL_VENDING_MACHINE, properties);
    }

    @Override
    public boolean onDestroyedByPlayer(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
            @Nonnull Player player, boolean willHarvest, @Nonnull FluidState fluid) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null && blockEntity instanceof IOwnable ownable) {
            if (ownable.getOwner() == null || ownable.getOwner().appliesTo(player)) {
                return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
            }
        }
        return false;
    }

    @Override
    protected InteractionResult useWithoutItem(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
            @Nonnull Player player, @Nonnull BlockHitResult hit) {
        if (player instanceof ServerPlayer serverPlayer) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null && blockEntity instanceof VendingMachineBlockEntity vendingMachine) {
                if (vendingMachine.getOwner() == null)
                    vendingMachine.setOwner(IdentifierHandler.encode(player));

                // Special handling to only allow the owner to open the storage
                if (serverPlayer.isCrouching() && vendingMachine.getOwner().appliesTo(serverPlayer)) {
                    serverPlayer.openMenu(vendingMachine.storageMenuProvider, pos);
                } else {
                    serverPlayer.openMenu(vendingMachine, pos);
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            VendingMachineBlockEntity vending = level.getBlockEntity(pos, ESBlockEntities.SMALL_VENDING_MACHINE.get())
                    .orElse(null);
            @SuppressWarnings("null")
            IItemHandler itemHandler = level.getCapability(ItemHandler.BLOCK, pos, state, vending, null);
            if (vending != null && itemHandler != null && vending.getAmount() > 0) {
                int amount = vending.getAmount();
                ItemStack base = itemHandler.getStackInSlot(VendingMachineBlockEntity.SLOT_STORAGE_OUT);
                while (amount > 0) {
                    int count = Math.min(base.getMaxStackSize(), amount);
                    ItemStack copy = base.copyWithCount(count);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), copy);
                    amount -= count;
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
