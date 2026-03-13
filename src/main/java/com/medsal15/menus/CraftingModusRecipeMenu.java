package com.medsal15.menus;

import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.menus.slot.GhostItemSlot;
import com.medsal15.modus.CraftingModus;
import com.medsal15.modus.CraftingModus.CraftingModusRecipe;
import com.medsal15.network.ESPackets.CraftingModusRecipeMenuQuit;
import com.medsal15.network.ESPackets.CraftingModusRecipeMenuSync;
import com.mraof.minestuck.inventory.ContainerHelper;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;

import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.NonInteractiveResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class CraftingModusRecipeMenu extends AbstractContainerMenu {
    // Slot positions
    public static final int INVENTORY_X = 10;
    public static final int INVENTORY_Y = 148;

    public static final int RECIPE_X_START = 25;
    public static final int RECIPE_Y_START = 43;
    public static final int RECIPE_X_SPACING = 16 + 4;
    public static final int RECIPE_Y_SPACING = 16 + 8;

    public static final int OUTPUT_X = 103;
    public static final int OUTPUT_Y = 68;

    private final NonNullList<ItemStack> inputList = NonNullList.withSize(9, ItemStack.EMPTY);
    private final Container inputContainer = new ItemHandlerContainer(new ItemStackHandler(inputList) {
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (!simulate)
                stacks.set(slot, ItemStack.EMPTY);
            return ItemStack.EMPTY;
        };

        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (!simulate)
                stacks.set(slot, stack.copyWithCount(1));
            return stack;
        };

        public int getSlotLimit(int slot) {
            return 1;
        };
    });
    private final Container output = new SimpleContainer(1);
    private final Player player;

    private final DataSlot indexData = new DataSlot() {
        public int get() {
            return index;
        };

        public void set(int value) {
            index = value;
        };
    };
    private final DataSlot recipesCountData = new DataSlot() {
        public int get() {
            return recipesSize;
        };

        public void set(int value) {
            recipesSize = value;
        };
    };
    private int index;
    private int recipesSize;
    private List<RecipeHolder<CraftingRecipe>> recipes = List.of();

    public CraftingModusRecipeMenu(int windowId, Inventory playerInventory) {
        super(ESMenuTypes.CRAFTING_MODUS_RECIPE.get(), windowId);

        this.player = playerInventory.player;

        addDataSlot(indexData);
        addDataSlot(recipesCountData);
        addItemSlots(playerInventory);
    }

    private void addItemSlots(Inventory playerInventory) {
        for (int col = 0; col < 3; col++)
            for (int row = 0; row < 3; row++) {
                int slot = col + row * 3;
                int x = RECIPE_X_START + col * RECIPE_X_SPACING;
                int y = RECIPE_Y_START + row * RECIPE_Y_SPACING;
                addSlot(new GhostItemSlot(inputContainer, slot, x, y));
            }

        addSlot(new NonInteractiveResultSlot(output, 0, OUTPUT_X, OUTPUT_Y));

        ContainerHelper.addPlayerInventorySlots(this::addSlot, INVENTORY_X, INVENTORY_Y, playerInventory);
    }

    private void refreshRecipes() {
        if (player instanceof ServerPlayer serverPlayer) {
            Level level = serverPlayer.level();
            CraftingInput input = CraftingInput.of(3, 3, inputList);
            recipes = level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, input, level);
            recipesSize = recipes.size();
            index = 0;
            if (recipes.size() >= 1) {
                setOutputFrom(0);
            } else {
                PacketDistributor.sendToPlayer(serverPlayer,
                        new CraftingModusRecipeMenuSync(0, 0, ItemStack.EMPTY),
                        new CustomPacketPayload[0]);
            }
        }
    }

    @Override
    public void clicked(int slotId, int button, @Nonnull ClickType clickType, @Nonnull Player player) {
        if (slotId != -999) {
            Slot slot = getSlot(slotId);
            if (slot instanceof GhostItemSlot && !slot.getItem().isEmpty()
                    && (clickType == ClickType.PICKUP || clickType == ClickType.PICKUP_ALL)) {
                // TODO test if this removes
                slot.set(ItemStack.EMPTY);
                return;
            }
        }
        super.clicked(slotId, button, clickType, player);
    }

    @SuppressWarnings("null")
    private void setOutputFrom(int index) {
        if (index > recipes.size())
            return;

        output.setItem(0, recipes.get(index).value().assemble(CraftingInput.of(3, 3, inputList), null));
    }

    public int recipeCount() {
        return recipesCountData.get();
    }

    public void setRecipeCount(int count) {
        recipesCountData.set(count);
    }

    public int recipeIndex() {
        return indexData.get();
    }

    public void setRecipeIndex(int index) {
        indexData.set(index);
    }

    public void setOutput(ItemStack output) {
        this.output.setItem(0, output);
    }

    public void nextRecipe() {
        if (recipesSize > 1) {
            index = (index + 1) % recipesSize;
            setOutputFrom(index);
            if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer,
                        new CraftingModusRecipeMenuSync(index, recipesSize, output.getItem(0)),
                        new CustomPacketPayload[0]);
            }
        }
    }

    public void saveRecipe(ServerPlayer player) {
        if (!output.getItem(0).isEmpty() && !inputContainer.isEmpty()) {
            var modus = CaptchaDeckHandler.getModus(player);
            if (modus != null && modus instanceof CraftingModus craftingModus) {
                craftingModus.addRecipe(new CraftingModusRecipe(CraftingInput.of(3, 3, inputList), output.getItem(0)));
                craftingModus.checkAndResend(player);
                PacketDistributor.sendToPlayer(player, CraftingModusRecipeMenuQuit.INSTANCE,
                        new CustomPacketPayload[0]);
            }
        }
    }

    // AbstractContainerMenu
    @Override
    public boolean stillValid(@Nonnull Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        return ItemStack.EMPTY;
    }

    public class ItemHandlerContainer implements Container {
        private final IItemHandlerModifiable inventory;

        public ItemHandlerContainer(IItemHandlerModifiable inventory) {
            this.inventory = inventory;
        }

        @Override
        public int getContainerSize() {
            return inventory.getSlots();
        }

        @Override
        public ItemStack getItem(int slot) {
            return inventory.getStackInSlot(slot);
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            ItemStack taken = inventory.getStackInSlot(slot);
            if (!taken.isEmpty())
                taken.split(amount);
            return ItemStack.EMPTY;
        }

        @Override
        public void setItem(int slot, @Nonnull ItemStack stack) {
            inventory.setStackInSlot(slot, stack);
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            ItemStack held = inventory.getStackInSlot(slot);
            if (!held.isEmpty()) {
                setItem(slot, ItemStack.EMPTY);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public boolean isEmpty() {
            for (int i = 0; i < inventory.getSlots(); i++)
                if (!inventory.getStackInSlot(i).isEmpty())
                    return false;
            return true;
        }

        @Override
        public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
            return inventory.isItemValid(slot, stack);
        }

        @Override
        public void clearContent() {
            for (int i = 0; i < inventory.getSlots(); i++)
                inventory.setStackInSlot(i, ItemStack.EMPTY);
        }

        @Override
        public void setChanged() {
            CraftingModusRecipeMenu.this.refreshRecipes();
        }

        @Override
        public boolean stillValid(@Nonnull Player player) {
            return false;
        }
    }
}
