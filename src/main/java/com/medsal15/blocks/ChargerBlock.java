package com.medsal15.blocks;

import javax.annotation.Nonnull;

import com.medsal15.blockentities.ChargerBlockEntity;
import com.medsal15.blockentities.ESBlockEntities;
import com.mraof.minestuck.block.CustomVoxelShape;
import com.mraof.minestuck.block.machine.SmallMachineBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class ChargerBlock extends SmallMachineBlock<ChargerBlockEntity> {
    private static final CustomVoxelShape shape = new CustomVoxelShape(new double[][] {
            { 0, 0, 0, 16, 8, 16 }
    });

    public ChargerBlock(Properties properties) {
        super(shape.createRotatedShapes(), ESBlockEntities.CHARGER, properties);
    }

    @Override
    protected boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ChargerBlockEntity charger)
            return charger.comparatorValue();
        return 0;
    }

    @Override
    protected ItemInteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level,
            @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand,
            @Nonnull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ChargerBlockEntity charger
                && hitResult.getDirection() == Direction.UP) {
            IItemHandler handler = charger.getItemHandler(Direction.UP);
            ItemStack charging = handler.getStackInSlot(ChargerBlockEntity.SLOT_IN);
            @SuppressWarnings("null")
            IEnergyStorage energy = Capabilities.EnergyStorage.ITEM.getCapability(stack, null);

            if (charging.isEmpty() && energy != null) {
                ItemStack remainder = handler.insertItem(ChargerBlockEntity.SLOT_IN, stack, false);
                if (!ItemStack.matches(stack, remainder)) {
                    player.setItemInHand(hand, remainder);
                    return ItemInteractionResult.CONSUME;
                }
            } else if (!charging.isEmpty() && stack.isEmpty() && player.isCrouching()) {
                charging = handler.extractItem(ChargerBlockEntity.SLOT_IN, 1, false);
                player.setItemInHand(hand, charging);
                return ItemInteractionResult.CONSUME;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
