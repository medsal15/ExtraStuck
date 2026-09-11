package com.medsal15.blockentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.handlers.BEStackHandler;
import com.medsal15.compat.ESCompatUtils;
import com.medsal15.compat.curios.items.ESCuriosUtils;
import com.medsal15.config.ConfigServer;
import com.medsal15.menus.WirelessChargerMenu;
import com.mraof.minestuck.api.uranium.IUraniumHandler;
import com.mraof.minestuck.api.uranium.SimpleUraniumHandler;
import com.mraof.minestuck.api.uranium.UraniumCapabilities;
import com.mraof.minestuck.api.uranium.UraniumPower;
import com.mraof.minestuck.blockentity.machine.MachineProcessBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class WirelessChargerBlockEntity extends MachineProcessBlockEntity implements MenuProvider {
    public static final String TITLE = "container.extrastuck.wireless_charger";
    public static final int SLOT_FUEL = 0;

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

    private final IUraniumHandler uraniumHandler = new SimpleUraniumHandler(ConfigServer.CHARGER_URANIUM_STORAGE::get,
            () -> this.fuel, fuel -> this.fuel = fuel) {
        public boolean canExtractUranium() {
            return false;
        }
    };

    private int fuel = 0;
    private int charge = 0;
    /** Tracks power transferred this tick */
    private int transferredFe = 0;
    private int transferredUr = 0;

    public WirelessChargerBlockEntity(BlockPos pos, BlockState state) {
        super(ESBlockEntities.WIRELESS_CHARGER.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, Provider pRegistries) {
        super.loadAdditional(nbt, pRegistries);

        fuel = nbt.getInt("fuel");
        charge = nbt.getInt("charge");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, Provider provider) {
        super.saveAdditional(nbt, provider);

        nbt.putInt("fuel", fuel);
        nbt.putInt("charge", charge);
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return itemHandler;
    }

    public IEnergyStorage getEnergyHandler(@Nullable Direction side) {
        // The top is for display, not electricity!
        if (side == Direction.UP)
            return null;
        return new EnergyStorage();
    }

    private class EnergyStorage implements IEnergyStorage {
        private final WirelessChargerBlockEntity charger = WirelessChargerBlockEntity.this;

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

    public IUraniumHandler getUraniumHandler(@Nullable Direction side) {
        return uraniumHandler;
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

    public void chargeNearby() {
        Level l = level;
        if (l == null)
            return;

        transferredFe = 0;
        transferredUr = 0;

        l.getEntitiesOfClass(ServerPlayer.class,
                new AABB(worldPosition).inflate(ConfigServer.CHARGER_WIRELESS_RADIUS.get()))
                .forEach(this::chargePlayer);
    }

    protected void chargePlayer(ServerPlayer player) {
        Inventory inventory = player.getInventory();
        int maxFe = ConfigServer.CHARGER_TRANSFER_TICK.get();
        int maxUr = ConfigServer.CHARGER_TRANSFER_TICK_URANIUM.get();

        for (ItemStack armor : inventory.armor) {
            chargeStack(armor, maxFe, maxUr);
        }
        for (ItemStack item : inventory.items) {
            chargeStack(item, maxFe, maxUr);
        }
        for (ItemStack offhand : inventory.offhand) {
            chargeStack(offhand, maxFe, maxUr);
        }

        if (ESCompatUtils.isLoaded("curios")) {
            int sent;

            int maxCharge = Math.min(maxFe - transferredFe, charge);
            sent = ESCuriosUtils.chargeFEInventory(player, maxCharge);
            transferredFe += sent;
            charge -= sent;

            int maxUranium = Math.min(maxUr - transferredUr, fuel);
            sent = ESCuriosUtils.chargeFEInventory(player, maxUranium);
            transferredUr += sent;
            fuel -= sent;
        }
    }

    protected void chargeStack(ItemStack stack, int maxFe, int maxUr) {
        if (transferredFe < maxFe && charge > 0) {
            @SuppressWarnings("null")
            IEnergyStorage energyHandler = Capabilities.EnergyStorage.ITEM.getCapability(stack, null);

            if (energyHandler != null && energyHandler.canReceive()) {
                int maxCharge = Math.min(maxFe - transferredFe, charge);
                int sent = energyHandler.receiveEnergy(maxCharge, false);
                transferredFe += sent;
                charge -= sent;
            }
        }

        if (transferredUr < maxUr && fuel > 0) {
            @SuppressWarnings("null")
            IUraniumHandler uraniumHandler = UraniumCapabilities.ITEM.getCapability(stack, null);

            if (uraniumHandler != null && uraniumHandler.canReceiveUranium()) {
                int maxUranium = Math.min(maxUr - transferredUr, fuel);
                int sent = uraniumHandler.receiveUranium(maxUranium, false);
                transferredUr += sent;
                fuel -= sent;
            }
        }
    }

    // BlockEntity
    @Override
    public void setChanged() {
        if (level != null)
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
        super.setChanged();
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

        if (charge > 0 || this.fuel > 0) {
            int ocharge = charge;
            int ofuel = this.fuel;
            chargeNearby();
            changed = ocharge != charge || ofuel != this.fuel;
        }

        if (changed) {
            setChanged();
        }
    }

    @Override
    protected ItemStackHandler createItemHandler() {
        return new BEStackHandler(1, (slot, stack) -> {
            switch (slot) {
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
    public AbstractContainerMenu createMenu(int window, @Nonnull Inventory playerInventory, @Nonnull Player player) {
        Level l = level;
        if (l == null)
            return null;

        return new WirelessChargerMenu(window, playerInventory, itemHandler, fuelHolder, chargeHolder,
                ContainerLevelAccess.create(l, worldPosition), worldPosition);
    }
}
