package com.medsal15.blockentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.handlers.BEStackHandler;
import com.medsal15.blockentities.handlers.FuellessWrapper;
import com.medsal15.menus.ChargerMenu;
import com.mraof.minestuck.blockentity.machine.MachineProcessBlockEntity;
import com.mraof.minestuck.blockentity.machine.UraniumPowered;
import com.mraof.minestuck.util.ExtraModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ChargerBlockEntity extends MachineProcessBlockEntity implements MenuProvider, UraniumPowered {
    public static final String TITLE = "container.extrastuck.charger";
    public static final int SLOT_IN = 0, SLOT_FUEL = 1;
    public static final short MAX_FUEL = 128;
    /** Charge gained per fuel unit */
    public static final int CHARGE_PER_FUEL = 500;
    /** Maximum charge transferred per tick, makes charging fancier */
    public static final int CHARGE_PER_TICK = 1_000;
    public static final int MAX_CHARGE = MAX_FUEL * CHARGE_PER_FUEL;

    private final DataSlot fuelHolder = new DataSlot() {
        public int get() {
            return fuel;
        };

        public void set(int value) {
            fuel = (short) value;
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
    private final DataSlot modeHolder = new DataSlot() {
        public int get() {
            return charging ? 1 : 0;
        };

        public void set(int value) {
            charging = value == 1;
        };
    };

    private short fuel = 0;
    private int charge = 0;
    private boolean charging = true;

    public ChargerBlockEntity(BlockPos pos, BlockState state) {
        super(ESBlockEntities.CHARGER.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, Provider pRegistries) {
        super.loadAdditional(nbt, pRegistries);

        fuel = nbt.getShort("fuel");
        charge = nbt.getInt("charge");
        charging = nbt.getBoolean("charging");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, Provider provider) {
        super.saveAdditional(nbt, provider);

        nbt.putShort("fuel", fuel);
        nbt.putInt("charge", charge);
        nbt.putBoolean("charging", charging);
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side == null)
            return itemHandler;

        return new FuellessWrapper(itemHandler, SLOT_IN, SLOT_FUEL + 1, SLOT_FUEL);
    }

    public IEnergyStorage getEnergyHandler(@Nullable Direction side) {
        // The top is for display, not electricity!
        if (side == Direction.UP)
            return null;
        return new EnergyStorage();
    }

    private class EnergyStorage implements IEnergyStorage {
        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            int missing = Math.min(MAX_CHARGE - ChargerBlockEntity.this.charge, toReceive);

            if (!simulate) {
                ChargerBlockEntity.this.charge += missing;
            }

            return missing;
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            int extracted = Math.min(toExtract, ChargerBlockEntity.this.charge);

            if (!simulate) {
                ChargerBlockEntity.this.charge -= extracted;
            }

            return extracted;
        }

        @Override
        public int getEnergyStored() {
            return ChargerBlockEntity.this.charge;
        }

        @Override
        public int getMaxEnergyStored() {
            return MAX_CHARGE;
        }

        @Override
        public boolean canExtract() {
            return ChargerBlockEntity.this.charge > 0;
        }

        @Override
        public boolean canReceive() {
            return ChargerBlockEntity.this.charge < MAX_CHARGE;
        }
    }

    public boolean canRefuel() {
        return fuel <= MAX_FUEL - FUEL_INCREASE;
    }

    public boolean canRecharge() {
        return charge <= MAX_CHARGE - CHARGE_PER_FUEL;
    }

    public int comparatorValue() {
        if (charge <= 0)
            return 0;
        if (charge >= MAX_CHARGE)
            return 15;
        return Math.floorDiv(charge * 14, MAX_CHARGE) + 1;
    }

    // BlockEntity
    @Override
    public void setChanged() {
        if (level != null)
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
        super.setChanged();
    }

    // These 4 below are required to update the client's item
    // Credit to @commoble on discord
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

    // MachineProcessBlockEntity
    @Override
    protected void tick() {
        boolean changed = false;
        // Refuel
        if (canRefuel() && itemHandler.getStackInSlot(SLOT_FUEL).is(ExtraModTags.Items.URANIUM_CHUNKS)) {
            addFuel((short) FUEL_INCREASE);
            itemHandler.extractItem(SLOT_FUEL, 1, false);
            changed = true;
        }

        // Recharge
        if (canRecharge() && fuel > 0) {
            charge += CHARGE_PER_FUEL;
            fuel--;
            changed = true;
        }

        // (Dis)Charge item
        if (!itemHandler.getStackInSlot(SLOT_IN).isEmpty()) {
            @SuppressWarnings("null")
            IEnergyStorage handler = Capabilities.EnergyStorage.ITEM.getCapability(itemHandler.getStackInSlot(SLOT_IN),
                    null);

            if (handler != null) {
                if (charging && handler.canReceive() && charge > 0) {
                    charge -= handler.receiveEnergy(Math.min(charge, CHARGE_PER_TICK), false);
                    changed = true;
                } else if (!charging && handler.canExtract() && charge < MAX_CHARGE) {
                    int missing = MAX_CHARGE - charge;
                    charge += handler.extractEnergy(Math.min(missing, CHARGE_PER_TICK), false);
                    changed = true;
                }
            }
        }

        if (changed) {
            setChanged();
        }
    }

    @SuppressWarnings("null")
    @Override
    protected ItemStackHandler createItemHandler() {
        return new BEStackHandler(2, (slot, stack) -> {
            switch (slot) {
                case SLOT_IN:
                    return Capabilities.EnergyStorage.ITEM.getCapability(stack, null) != null;
                case SLOT_FUEL:
                    return stack.is(ExtraModTags.Items.URANIUM_CHUNKS);
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
    public AbstractContainerMenu createMenu(int window, @Nonnull Inventory playerInventory,
            @Nonnull Player player) {
        Level l = level;
        if (l == null)
            return null;

        return new ChargerMenu(window, playerInventory, itemHandler, fuelHolder, chargeHolder, modeHolder,
                ContainerLevelAccess.create(l, worldPosition), worldPosition);
    }

    // UraniumPowered
    @Override
    public boolean atMaxFuel() {
        return fuel >= MAX_FUEL;
    }

    @Override
    public void addFuel(short amount) {
        fuel += amount;
    }
}
