package com.medsal15.blockentities;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.handlers.BEStackHandler;
import com.medsal15.blockentities.handlers.FuellessWrapper;
import com.medsal15.data.ESFluidTags;
import com.medsal15.items.ESItems;
import com.medsal15.menus.ReactorMenu;
import com.mraof.minestuck.blockentity.machine.MachineProcessBlockEntity;
import com.mraof.minestuck.blockentity.machine.UraniumPowered;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ReactorBlockEntity extends MachineProcessBlockEntity implements MenuProvider {
    public static final String TITLE = "container.extrastuck.reactor";
    public static final int SLOT_FUEL_IN = 0, SLOT_FUEL_OUT = 1;
    /** Fuel gained per energy core, converted 1 to 1 to uranium */
    public static final int FUEL_PER_CORES = 128;
    public static final int MAX_URANIUM = FUEL_PER_CORES * 4;
    /** Charge gained per fuel unit */
    public static final int CHARGE_PER_FUEL = 5_000;
    /** Max charge transferred to neighbors per tick */
    public static final int CHARGE_PER_TICK = 10_000;
    /** Max uranium transferred to neighbors per tick */
    public static final short URANIUM_PER_TICK = 8;
    public static final int MAX_CHARGE = FUEL_PER_CORES * CHARGE_PER_FUEL * 4;
    public static final int MAX_FLUID = 1_000 * 4;
    /** Fluid consumed each tick */
    public static final int FLUID_PER_TICK = 10;

    private final DataSlot fuelHolder = new DataSlot() {
        public int get() {
            return fuel;
        };

        public void set(int value) {
            fuel = value;
        };
    };
    private final DataSlot uraniumHolder = new DataSlot() {
        public int get() {
            return uranium;
        };

        public void set(int value) {
            uranium = value;
        };
    };
    private final DataSlot chargeHolder = new DataSlot() {
        public int get() {
            return charge;
        };

        public void set(int value) {
            charge = value;
        };
    };
    private final DataSlot fluidAmountHolder = new DataSlot() {
        public int get() {
            return fluid.getAmount();
        };

        public void set(int value) {
            if (!fluid.isEmpty()) {
                fluid.setAmount(value);
            }
        };
    };

    private int fuel = 0;
    private int uranium = 0;
    private int charge = 0;
    private FluidStack fluid = FluidStack.EMPTY;

    @Nullable
    private EnergyStorage energyStorage = null;
    @Nullable
    private FluidHandler fluidHandler = null;

    public ReactorBlockEntity(BlockPos pos, BlockState state) {
        super(ESBlockEntities.REACTOR.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, Provider pRegistries) {
        super.loadAdditional(nbt, pRegistries);

        fuel = nbt.getInt("fuel");
        uranium = nbt.getInt("uranium");
        charge = nbt.getInt("charge");
        Tag fluidTag = nbt.get("fluid");
        if (fluidTag != null) {
            Optional<FluidStack> fluid = FluidStack.parse(pRegistries, fluidTag);
            if (fluid.isPresent()) {
                this.fluid = fluid.get();
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, Provider provider) {
        super.saveAdditional(nbt, provider);

        nbt.putInt("fuel", fuel);
        nbt.putInt("uranium", uranium);
        nbt.putInt("charge", charge);
        if (!fluid.isEmpty()) {
            nbt.put("fluid", fluid.save(provider));
        }
    }

    public boolean canCharge() {
        return charge + CHARGE_PER_FUEL <= MAX_CHARGE;
    }

    public boolean canUranium() {
        return uranium + 1 <= MAX_URANIUM;
    }

    public boolean canDrain() {
        return fluid.getAmount() >= FLUID_PER_TICK;
    }

    public Fluid getFluid() {
        return fluid.getFluid();
    }

    public FluidStack getFluidStack() {
        return fluid.copy();
    }

    // Update client
    @Override
    public void setChanged() {
        if (level != null)
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
        super.setChanged();
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(@Nonnull Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public void onDataPacket(@Nonnull Connection net, @Nonnull ClientboundBlockEntityDataPacket pkt,
            @Nonnull Provider lookupProvider) {
        loadWithComponents(pkt.getTag(), lookupProvider);
    }

    @Override
    public void handleUpdateTag(@Nonnull CompoundTag tag, @Nonnull Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);

        if (level != null)
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side == null)
            return itemHandler;

        return new FuellessWrapper(itemHandler, SLOT_FUEL_IN, SLOT_FUEL_OUT + 1, SLOT_FUEL_IN);
    }

    public IFluidHandler getFluidHandler(@Nullable Direction side) {
        if (fluidHandler == null) {
            fluidHandler = new FluidHandler();
        }
        return fluidHandler;
    }

    private class FluidHandler implements IFluidHandler {
        private final ReactorBlockEntity reactor = ReactorBlockEntity.this;

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return reactor.fluid;
        }

        @Override
        public int getTankCapacity(int tank) {
            return MAX_FLUID;
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
            return stack.is(ESFluidTags.REACTOR_FLUIDS);
        }

        @Override
        public int fill(@Nonnull FluidStack resource, @Nonnull FluidAction action) {
            if (!reactor.fluid.isEmpty() && !FluidStack.isSameFluidSameComponents(reactor.fluid, resource))
                return 0;

            int missing = MAX_FLUID - reactor.fluid.getAmount();
            int filled = Math.min(missing, resource.getAmount());

            if (action == FluidAction.EXECUTE) {
                if (reactor.fluid.isEmpty()) {
                    reactor.fluid = new FluidStack(resource.getFluid(), filled);
                } else {
                    reactor.fluid.grow(filled);
                }
                reactor.setChanged();
            }

            return filled;
        }

        @Override
        public FluidStack drain(int maxDrain, @Nonnull FluidAction action) {
            int drained = Math.min(maxDrain, reactor.fluid.getAmount());
            FluidStack result = reactor.fluid.copy();
            result.setAmount(drained);

            if (action == FluidAction.EXECUTE) {
                reactor.fluid.shrink(drained);
                reactor.setChanged();
            }

            return result;
        }

        @Override
        public FluidStack drain(@Nonnull FluidStack resource, @Nonnull FluidAction action) {
            if (!reactor.fluid.isEmpty() && FluidStack.isSameFluidSameComponents(reactor.fluid, resource))
                return FluidStack.EMPTY;

            return drain(resource.getAmount(), action);
        }

    }

    public IEnergyStorage getEnergyHandler(@Nullable Direction side) {
        if (energyStorage == null) {
            energyStorage = new EnergyStorage();
        }
        return energyStorage;
    }

    private class EnergyStorage implements IEnergyStorage {
        private final ReactorBlockEntity reactor = ReactorBlockEntity.this;

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            int missing = Math.min(MAX_CHARGE - reactor.charge, toReceive);

            if (!simulate) {
                reactor.charge += missing;
            }

            return missing;
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            int extracted = Math.min(toExtract, reactor.charge);

            if (!simulate) {
                reactor.charge -= extracted;
            }

            return extracted;
        }

        @Override
        public int getEnergyStored() {
            return reactor.charge;
        }

        @Override
        public int getMaxEnergyStored() {
            return MAX_CHARGE;
        }

        @Override
        public boolean canExtract() {
            return reactor.charge > 0;
        }

        @Override
        public boolean canReceive() {
            // This isn't a battery
            return false;
        }
    }

    // MachineProcessBlockEntity
    @Override
    protected void tick() {
        boolean changed = false;
        Level l = level;

        // Refuel
        if (fuel <= 0 && itemHandler.getStackInSlot(SLOT_FUEL_IN).is(MSItems.ENERGY_CORE)) {
            fuel += FUEL_PER_CORES;
            itemHandler.extractItem(SLOT_FUEL_IN, 1, false);
            changed = true;
        }

        // Process
        if (canDrain() && fuel > 0 && (canCharge() || canUranium())) {
            if (canUranium()) {
                uranium++;
            }
            if (canCharge()) {
                charge += CHARGE_PER_FUEL;
            }
            fluid.shrink(FLUID_PER_TICK);
            if (fuel == 1) {
                // Add an empty core to the output
                ItemStack present = itemHandler.getStackInSlot(SLOT_FUEL_OUT);
                ItemStack output = ESItems.EMPTY_ENERGY_CORE.toStack();
                if (!present.isEmpty()) {
                    output.grow(present.getCount());
                }
                itemHandler.setStackInSlot(SLOT_FUEL_OUT, output);
            }
            fuel--;
            changed = true;
        }

        if ((uranium > 0 || charge > 0) && l != null) {
            Direction[] neighbors = Direction.values();
            for (int i = 0; i < neighbors.length && (uranium > 0 || charge > 0); i++) {
                Direction dir = neighbors[i];
                BlockPos pos = worldPosition.relative(dir);
                BlockEntity neighbe = l.getBlockEntity(pos);
                BlockState neighstate = l.getBlockState(pos);
                if (neighbe == null)
                    continue;

                // Send uranium power to neighbors
                if (uranium > 0 && neighbe instanceof UraniumPowered powered && !powered.atMaxFuel()) {
                    powered.addFuel((short) 1);
                    uranium--;
                }

                // Send FE to neighbors
                IEnergyStorage neighandler = Capabilities.EnergyStorage.BLOCK.getCapability(l, pos, neighstate, neighbe,
                        dir.getOpposite());
                if (neighandler != null && neighandler.canReceive()) {
                    int sent = neighandler.receiveEnergy(this.charge, false);
                    charge -= sent;
                }
            }
        }

        if (changed) {
            setChanged();
        }
    }

    @Override
    protected ItemStackHandler createItemHandler() {
        return new BEStackHandler(2, (slot, stack) -> {
            switch (slot) {
                case SLOT_FUEL_IN:
                    return stack.is(MSItems.ENERGY_CORE);
                default:
                    return false;
            }
        }, this);
    }

    // MenuProvider
    @Override
    public Component getDisplayName() {
        return Component.translatable(TITLE);
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int window, @Nonnull Inventory playerInventory, @Nonnull Player player) {
        Level l = level;
        if (l == null)
            return null;
        return new ReactorMenu(window, playerInventory, itemHandler, fuelHolder, uraniumHolder, chargeHolder,
                fluidAmountHolder, ContainerLevelAccess.create(l, worldPosition), worldPosition);
    }
}
