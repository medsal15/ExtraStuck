package com.medsal15.blockentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.handlers.BEStackHandler;
import com.medsal15.blockentities.handlers.FuellessWrapper;
import com.medsal15.config.ConfigServer;
import com.medsal15.menus.ChargerMenu;
import com.mraof.minestuck.api.uranium.IUraniumHandler;
import com.mraof.minestuck.api.uranium.SimpleUraniumHandler;
import com.mraof.minestuck.api.uranium.UraniumCapabilities;
import com.mraof.minestuck.api.uranium.UraniumPower;
import com.mraof.minestuck.blockentity.machine.MachineProcessBlockEntity;

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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ChargerBlockEntity extends MachineProcessBlockEntity implements MenuProvider {
    public static final String TITLE = "container.extrastuck.charger";
    public static final int SLOT_IN = 0, SLOT_FUEL = 1;

    private final DataSlot fuelHolder = new DataSlot() {
        public int get() {
            return fuel;
        };

        public void set(int value) {
            fuel = value;
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

    private final IUraniumHandler uraniumHandler = new SimpleUraniumHandler(ConfigServer.CHARGER_URANIUM_STORAGE::get,
            () -> this.fuel, fuel -> this.fuel = fuel) {
        public boolean canExtractUranium() {
            return false;
        }
    };

    private int fuel = 0;
    private int charge = 0;
    private boolean charging = true;

    public ChargerBlockEntity(BlockPos pos, BlockState state) {
        super(ESBlockEntities.CHARGER.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, Provider pRegistries) {
        super.loadAdditional(nbt, pRegistries);

        if (nbt.contains("fuel", 2))
            fuel = nbt.getShort("fuel");
        else
            fuel = nbt.getInt("fuel");
        charge = nbt.getInt("charge");
        charging = nbt.getBoolean("charging");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, Provider provider) {
        super.saveAdditional(nbt, provider);

        nbt.putInt("fuel", fuel);
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

    public IUraniumHandler getUraniumHandler(@Nullable Direction side) {
        return uraniumHandler;
    }

    private class EnergyStorage implements IEnergyStorage {
        private final ChargerBlockEntity charger = ChargerBlockEntity.this;

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            int missing = Math.min(ConfigServer.CHARGER_FE_STORAGE.get() - charger.charge, toReceive);

            if (!simulate) {
                charger.charge += missing;
            }

            return missing;
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            int extracted = Math.min(toExtract, charger.charge);

            if (!simulate) {
                charger.charge -= extracted;
            }

            return extracted;
        }

        @Override
        public int getEnergyStored() {
            return charger.charge;
        }

        @Override
        public int getMaxEnergyStored() {
            return ConfigServer.CHARGER_FE_STORAGE.get();
        }

        @Override
        public boolean canExtract() {
            return charger.charge > 0;
        }

        @Override
        public boolean canReceive() {
            return charger.charge < ConfigServer.CHARGER_FE_STORAGE.get();
        }
    }

    public boolean canRefuel(ItemStack fuelStack) {
        int amount = UraniumPower.getUraniumPower(fuelStack);
        return fuel + amount <= ConfigServer.CHARGER_URANIUM_STORAGE.get();
    }

    public boolean canRecharge() {
        return charge <= ConfigServer.CHARGER_FE_STORAGE.get() - ConfigServer.CHARGER_CHARGE_TICK.get();
    }

    public void addFuel(ItemStack fuelStack) {
        int amount = UraniumPower.getUraniumPower(fuelStack);
        fuel += amount;
    }

    public int comparatorValue() {
        if (charge <= 0)
            return 0;
        if (charge >= ConfigServer.CHARGER_FE_STORAGE.get())
            return 15;
        return Math.floorDiv(charge * 14, ConfigServer.CHARGER_FE_STORAGE.get()) + 1;
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
        ItemStack fuel = itemHandler.getStackInSlot(SLOT_FUEL);

        // Refuel
        if (canRefuel(fuel)) {
            addFuel(fuel);
            Level l = level;
            ItemStack taken = itemHandler.extractItem(SLOT_FUEL, 1, false);
            ItemStack remainder = taken.getCraftingRemainingItem();
            if (!remainder.isEmpty() && l != null) {
                ItemEntity remainderEntity = new ItemEntity(l, worldPosition.getX(), worldPosition.getY(),
                        worldPosition.getZ(), remainder);
                l.addFreshEntity(remainderEntity);
            }
            changed = true;
        }

        // Recharge
        if (canRecharge() && this.fuel > 0) {
            charge += ConfigServer.CHARGER_CHARGE_TICK.get();
            this.fuel--;
            changed = true;
        }

        // (Dis)Charge item
        ItemStack toCharge = itemHandler.getStackInSlot(SLOT_IN);
        if (!toCharge.isEmpty()) {
            @SuppressWarnings("null")
            IEnergyStorage energyHandler = Capabilities.EnergyStorage.ITEM.getCapability(toCharge, null);
            if (energyHandler != null) {
                if (charging && energyHandler.canReceive() && charge > 0) {
                    charge -= energyHandler.receiveEnergy(Math.min(charge, ConfigServer.CHARGER_TRANSFER_TICK.get()),
                            false);
                    changed = true;
                } else if (!charging && energyHandler.canExtract()
                        && charge < ConfigServer.CHARGER_FE_STORAGE.get()) {
                    int missing = ConfigServer.CHARGER_FE_STORAGE.get() - charge;
                    charge += energyHandler.extractEnergy(Math.min(missing, ConfigServer.CHARGER_TRANSFER_TICK.get()),
                            false);
                    changed = true;
                }
            }

            @SuppressWarnings("null")
            IUraniumHandler uraniumHandler = UraniumCapabilities.ITEM.getCapability(toCharge, null);
            if (uraniumHandler != null) {
                if (charging && uraniumHandler.canReceiveUranium() && this.fuel > 0) {
                    this.fuel -= uraniumHandler.receiveUranium(
                            Math.min(this.fuel, ConfigServer.CHARGER_TRANSFER_TICK_URANIUM.get()), false);
                    changed = true;
                } else if (!charging && uraniumHandler.canExtractUranium()
                        && this.fuel < ConfigServer.CHARGER_URANIUM_STORAGE.get()) {
                    int missing = ConfigServer.CHARGER_URANIUM_STORAGE.get() - this.fuel;
                    this.fuel += uraniumHandler
                            .extractUranium(Math.min(missing, ConfigServer.CHARGER_TRANSFER_TICK_URANIUM.get()), false);
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
                    return UraniumPower.hasUraniumPower(stack);
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
}
