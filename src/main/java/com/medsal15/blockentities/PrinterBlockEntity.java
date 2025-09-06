package com.medsal15.blockentities;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.handlers.BEStackHandler;
import com.medsal15.blockentities.handlers.FuellessWrapper;
import com.medsal15.blocks.PrinterBlock;
import com.medsal15.menus.PrinterMenu;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.IColored;
import com.mraof.minestuck.blockentity.machine.GristWildcardHolder;
import com.mraof.minestuck.blockentity.machine.IOwnable;
import com.mraof.minestuck.blockentity.machine.MachineProcessBlockEntity;
import com.mraof.minestuck.blockentity.machine.ProgressTracker;
import com.mraof.minestuck.blockentity.machine.UraniumPowered;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.ColorHandler;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

public class PrinterBlockEntity extends MachineProcessBlockEntity
        implements IOwnable, GristWildcardHolder, MenuProvider, IColored, UraniumPowered {
    public static final String TITLE = "container.extrastuck.printer";
    public static final int SLOT_IN = 0, SLOT_OUT = 1, SLOT_FUEL = 2;
    public static final int MAX_PROGRESS = 200;
    public static final short MAX_FUEL = 128;

    private final ProgressTracker progressTracker = new ProgressTracker(ProgressTracker.RunType.ONCE_OR_LOOPING,
            MAX_PROGRESS, this::setChanged, this::contentsValid);
    private final DataSlot wildcardHolder = new DataSlot() {
        public int get() {
            return GristTypes.REGISTRY.getId(getWildcardGrist());
        };

        public void set(int value) {
            GristType grist = GristTypes.REGISTRY.byId(value);
            if (grist == null)
                grist = GristTypes.BUILD.get();
            setWildcardGrist(grist);
        };
    };
    private final DataSlot fuelHolder = new DataSlot() {
        public int get() {
            return fuel;
        };

        public void set(int value) {
            fuel = (short) value;
        };
    };

    private int ticks_since_update = 0;
    @Nullable
    private PlayerIdentifier owner;
    private GristType wildcard = GristTypes.BUILD.get();
    private short fuel = 0;

    public PrinterBlockEntity(BlockPos pos, BlockState state) {
        super(ESBlockEntities.PRINTER.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, Provider provider) {
        super.loadAdditional(nbt, provider);

        progressTracker.load(nbt);

        fuel = nbt.getShort("fuel");
        wildcard = GristHelper.parseGristType(nbt.get("wildcard")).orElseGet(GristTypes.BUILD);
        owner = IdentifierHandler.load(nbt, "owner").result().orElse(null);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, Provider provider) {
        super.saveAdditional(nbt, provider);

        progressTracker.save(nbt);

        nbt.putShort("fuel", fuel);
        nbt.put("wildcard", GristHelper.encodeGristType(wildcard));
        if (owner != null)
            owner.saveToNBT(nbt, "owner");
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side == null)
            return itemHandler;

        if (side == Direction.UP)
            return new RangedWrapper(this.itemHandler, SLOT_IN, SLOT_IN + 1);

        return new FuellessWrapper(itemHandler, SLOT_OUT, SLOT_FUEL + 1, SLOT_FUEL);
    }

    public int comparatorValue() {
        // Unlike an alchemiter, we just want to know how full the output is
        ItemStack output = itemHandler.getStackInSlot(SLOT_OUT);
        if (output.isEmpty())
            return 0;
        return Math.floorDiv(output.getCount() * 14, output.getMaxStackSize()) + 1;
    }

    public boolean canRefuel() {
        return fuel <= MAX_FUEL - FUEL_INCREASE;
    }

    // BlockEntity
    @Override
    public void setChanged() {
        Level l = level;
        if (l != null) {
            BlockState state = l.getBlockState(worldPosition);
            if (state.getBlock() instanceof PrinterBlock) {
                ItemStack dowel = itemHandler.getStackInSlot(SLOT_IN);
                state = state.setValue(PrinterBlock.HAS_DOWEL, !dowel.isEmpty());
                l.setBlock(worldPosition, state,
                        Block.UPDATE_ALL_IMMEDIATE | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_SUPPRESS_DROPS);
            }
        }
        super.setChanged();
    }

    // These 4 below are required to update the client's tint
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
        // Refuel
        if (canRefuel() && itemHandler.getStackInSlot(SLOT_FUEL).is(ExtraModTags.Items.URANIUM_CHUNKS)) {
            addFuel((short) FUEL_INCREASE);
            itemHandler.extractItem(SLOT_FUEL, 1, false);
        }

        Level l = level;
        if (l != null && l.hasNeighborSignal(getBlockPos())) {
            // Enable on redstone signal
            progressTracker.set(ProgressTracker.RUN_INDEX, 1);
            progressTracker.set(ProgressTracker.LOOPING_INDEX, 1);
        } else if (!this.contentsValid() || fuel <= 0) {
            // Halt if it can't work
            progressTracker.set(ProgressTracker.RUN_INDEX, 0);
            progressTracker.set(ProgressTracker.LOOPING_INDEX, 0);
        }
        progressTracker.tick(this::processContents);

        if (ticks_since_update == 20 && level != null) {
            level.updateNeighbourForOutputSignal(getBlockPos(), getBlockState().getBlock());
            ticks_since_update = 0;
        } else {
            ticks_since_update++;
        }
    }

    @Override
    protected ItemStackHandler createItemHandler() {
        return new BEStackHandler(3,
                (slot, stack) -> {
                    switch (slot) {
                        case SLOT_IN:
                            return stack.getItem() == MSBlocks.CRUXITE_DOWEL.get().asItem();
                        case SLOT_FUEL:
                            return stack.is(ExtraModTags.Items.URANIUM_CHUNKS);
                        default:
                            return false;
                    }
                }, this);
    }

    // ProgressTracker
    private boolean contentsValid() {
        Level l = level;
        if (l != null && !itemHandler.getStackInSlot(SLOT_IN).isEmpty()
                && owner != null) {
            ItemStack print = EncodedItemComponent.getEncodedOrBlank(itemHandler.getStackInSlot(SLOT_IN));

            if (print.isEmpty())
                return false;

            ItemStack output = itemHandler.getStackInSlot(SLOT_OUT);
            if (!output.isEmpty() && (!ItemStack.isSameItemSameComponents(print, output)
                    || output.getMaxStackSize() < output.getCount() + print.getCount()))
                return false;

            GristSet cost = GristCostRecipe.findCostForItem(print, wildcard, false, l);

            return GristCache.get(l, owner).canAfford(cost);
        } else {
            return false;
        }
    }

    private void processContents() {
        Level l = level;
        Objects.requireNonNull(l);

        // Print
        ItemStack dowel = itemHandler.getStackInSlot(SLOT_IN);
        ItemStack print = EncodedItemComponent.getEncodedOrBlank(dowel);
        GristSet cost = GristCostRecipe.findCostForItem(print, wildcard, false, l);
        Objects.requireNonNull(cost);

        if (GristCache.get(l, owner).tryTake(cost, GristHelper.EnumSource.CLIENT)) {
            AlchemyEvent event = new AlchemyEvent(owner, this, dowel, print, cost);
            NeoForge.EVENT_BUS.post(event);
            print = event.getItemResult();

            ItemStack output = itemHandler.getStackInSlot(SLOT_OUT);
            if (!output.isEmpty())
                print.grow(output.getCount());

            fuel--;
            itemHandler.setStackInSlot(SLOT_OUT, print);
        }
    }

    // IOwnable
    @Override
    public PlayerIdentifier getOwner() {
        return owner;
    }

    @Override
    public void setOwner(PlayerIdentifier player) {
        owner = player;
    }

    // GristWildcardHolder
    @Override
    public void setWildcardGrist(GristType grist) {
        if (wildcard != grist) {
            wildcard = grist;
            setChanged();
        }
    }

    public GristType getWildcardGrist() {
        return wildcard;
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
        return new PrinterMenu(window, playerInventory, itemHandler, progressTracker, wildcardHolder, fuelHolder,
                ContainerLevelAccess.create(l, worldPosition), worldPosition);
    }

    // IColored
    @Override
    public int getColor() {
        return ColorHandler.getColorFromStack(itemHandler.getStackInSlot(SLOT_IN));
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
