package com.medsal15.modus;

import java.util.Optional;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public class FurnaceModus extends BaseModus {
    protected int fuel = 0;

    public FurnaceModus(ModusType<? extends FurnaceModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public void readFromNBT(CompoundTag nbt, Provider provider) {
        super.readFromNBT(nbt, provider);

        fuel = nbt.getInt("fuel");
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt, Provider provider) {
        nbt = super.writeToNBT(nbt, provider);

        nbt.putInt("fuel", fuel);

        return nbt;
    }

    @Override
    public boolean putItemStack(ServerPlayer player, ItemStack item) {
        int add = getFuelFor(item);
        if (add > 0) {
            this.fuel += add;
            markDirty();
            return true;
        }

        return super.putItemStack(player, item);
    }

    /**
     * Returns fuel stored in the stack
     *
     * @returns If 0 or lower, the item will be stored
     */
    protected int getFuelFor(ItemStack stack) {
        Holder<Item> holder = stack.getItemHolder();
        FurnaceFuel fuel = holder.getData(NeoForgeDataMaps.FURNACE_FUELS);
        if (fuel != null) {
            return fuel.burnTime() * stack.getCount();
        }
        return 0;
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int slot, boolean asCard) {
        if (slot == CaptchaDeckHandler.EMPTY_CARD) {
            if (list.size() < size) {
                size--;
                markDirty();
                return MSItems.CAPTCHA_CARD.toStack();
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (list.isEmpty())
            return ItemStack.EMPTY;

        if (slot == CaptchaDeckHandler.EMPTY_SYLLADEX) {
            for (ItemStack item : list) {
                CaptchaDeckHandler.launchAnyItem(player, item);
            }
            list.clear();
            markDirty();
            return ItemStack.EMPTY;
        }

        if (slot < 0 || slot >= list.size()) {
            return ItemStack.EMPTY;
        }

        ItemStack item = list.get(slot);

        // Check if there is a smelting recipe available
        Optional<? extends AbstractCookingRecipe> optionalRecipe = getRecipe(item, player.level());
        if (optionalRecipe.isPresent()) {
            AbstractCookingRecipe recipe = optionalRecipe.get();
            int cost = recipe.getCookingTime() * item.getCount();
            if (cost > fuel) {
                return ItemStack.EMPTY;
            } else {
                fuel -= cost;
                item = recipe.getResultItem(player.level().registryAccess()).copyWithCount(item.getCount());
                list.remove(slot);
            }
        } else if (fuel >= 200) {
            // Default to 200 fuel cost
            list.remove(slot);
            fuel -= 200;
        } else {
            return ItemStack.EMPTY;
        }
        markDirty();

        if (asCard) {
            size--;
            markDirty();
            item = CaptchaCardItem.createCardWithItem(item, player.server);
        }

        return item;
    }

    /** Recipe that processes the item using an amount of fuel */
    protected Optional<? extends AbstractCookingRecipe> getRecipe(ItemStack taken, Level level) {
        return level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(taken), level).stream()
                .map(RecipeHolder::value).findFirst();
    }

    public int getWidth() {
        return (int) Math.ceil(Math.sqrt(size));
    }

    public int getFuel() {
        return fuel / 200;
    }
}
