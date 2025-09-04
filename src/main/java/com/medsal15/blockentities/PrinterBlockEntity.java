package com.medsal15.blockentities;

import java.util.Objects;
import java.util.function.BiPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.ColorHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

public class PrinterBlockEntity extends MachineProcessBlockEntity
        implements IOwnable, GristWildcardHolder, MenuProvider, IColored {
    public static final String TITLE = "container.extrastuck.printer";
    public static final int SLOT_IN = 0, SLOT_OUT = 1;
    public static final int MAX_PROGRESS = 200;

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

    private int ticks_since_update = 0;
    @Nullable
    private PlayerIdentifier owner;
    private GristType wildcard = GristTypes.BUILD.get();

    public PrinterBlockEntity(BlockPos pos, BlockState state) {
        super(ESBlockEntities.PRINTER.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, Provider pRegistries) {
        super.loadAdditional(nbt, pRegistries);

        progressTracker.load(nbt);

        wildcard = GristHelper.parseGristType(nbt.get("wildcard")).orElseGet(GristTypes.BUILD);
        owner = IdentifierHandler.load(nbt, "owner").result().orElse(null);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, Provider provider) {
        super.saveAdditional(nbt, provider);

        progressTracker.save(nbt);

        nbt.put("wildcard", GristHelper.encodeGristType(wildcard));
        if (owner != null)
            owner.saveToNBT(nbt, "owner");
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side == null)
            return itemHandler;

        if (side == Direction.UP)
            return new RangedWrapper(this.itemHandler, SLOT_IN, SLOT_IN + 1);

        return new RangedWrapper(itemHandler, SLOT_OUT, SLOT_OUT + 1);
    }

    public int comparatorValue() {
        // Unlike an alchemiter, we just want to know how full the output is
        ItemStack output = itemHandler.getStackInSlot(SLOT_OUT);
        if (output.isEmpty())
            return 0;
        return Math.floorDiv(output.getCount() * 14, output.getMaxStackSize()) + 1;
    }

    // BlockEntity
    @Override
    public void setChanged() {
        super.setChanged();

        Level l = level;
        if (l != null) {
            BlockState state = l.getBlockState(worldPosition);
            if (state.getBlock() instanceof PrinterBlock) {
                state = state.setValue(PrinterBlock.HAS_DOWEL, !itemHandler.getStackInSlot(SLOT_IN).isEmpty());
                l.setBlock(worldPosition, state, 1 | 2 | 16 | 32);
            }
        }
    }

    // MachineProcessBlockEntity
    @Override
    protected void tick() {
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
        return new CustomHandler(2,
                (slot, stack) -> slot == SLOT_IN && stack.getItem() == MSBlocks.CRUXITE_DOWEL.get().asItem());
    }

    // Copy of minestuck's as it's not public
    private class CustomHandler extends ItemStackHandler {
        private final BiPredicate<Integer, ItemStack> isValidPredicate;

        protected CustomHandler(int size, BiPredicate<Integer, ItemStack> isValidPredicate) {
            super(size);
            this.isValidPredicate = isValidPredicate;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return isValidPredicate.test(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            PrinterBlockEntity.this.setChanged();
        }
    }

    // ProgressTracker
    private boolean contentsValid() {
        Level l = level;
        if (l != null && !l.hasNeighborSignal(getBlockPos()) && !itemHandler.getStackInSlot(SLOT_IN).isEmpty()
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
        return new PrinterMenu(window, playerInventory, itemHandler, progressTracker, wildcardHolder,
                ContainerLevelAccess.create(l, worldPosition), worldPosition);
    }

    // IColored
    @Override
    public int getColor() {
        return ColorHandler.getColorFromStack(itemHandler.getStackInSlot(SLOT_IN));
    }
}
