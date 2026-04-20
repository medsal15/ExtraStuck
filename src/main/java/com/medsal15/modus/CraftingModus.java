package com.medsal15.modus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.neoforged.fml.LogicalSide;

//TODO verify recipes on load/reload
public class CraftingModus extends Modus {
    /** Cards available */
    protected int size;
    protected NonNullList<CraftingModusRecipe> list;

    // Client side
    protected boolean changed;
    protected NonNullList<CraftingModusRecipe> recipes;

    public CraftingModus(ModusType<? extends CraftingModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public void readFromNBT(CompoundTag nbt, Provider provider) {
        size = nbt.getInt("size");
        list = NonNullList.create();

        for (int i = 0; i < size / 10; i++) {
            if (nbt.contains("recipe" + i, Tag.TAG_COMPOUND))
                list.add(CraftingModusRecipe.parse(provider, nbt.getCompound("recipe" + i)).orElseThrow());
            else
                break;
        }

        if (side == LogicalSide.CLIENT) {
            if (recipes == null)
                recipes = NonNullList.create();
            changed = true;
        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt, Provider provider) {
        nbt.putInt("size", size);

        for (int i = 0; i < list.size(); i++) {
            nbt.put("recipe" + i, list.get(i).save(provider));
        }

        return nbt;
    }

    @Override
    public void initModus(ItemStack item, ServerPlayer player, NonNullList<ItemStack> prev, int size) {
        this.size = size;
        list = NonNullList.create();

        if (side == LogicalSide.CLIENT) {
            recipes = NonNullList.create();
            changed = true;
        }
    }

    // The trick is to not store anything in the modus ;)
    @Override
    public NonNullList<ItemStack> getItems() {
        if (changed) {
            fillRecipes(list);
        }

        return NonNullList.create();
    }

    protected void fillRecipes(NonNullList<CraftingModusRecipe> list) {
        if (recipes != null)
            recipes.clear();
        else
            recipes = NonNullList.create();
        for (CraftingModusRecipe recipe : list) {
            recipes.add(recipe);
        }
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int slot, boolean asCard) {
        if (slot == CaptchaDeckHandler.EMPTY_CARD) {
            if (list.size() * 10 < size) {
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
            list.clear();
            markDirty();
            return ItemStack.EMPTY;
        }

        if (slot < 0 || slot > list.size())
            return ItemStack.EMPTY;

        CraftingModusRecipe recipe = list.get(slot);
        ItemStack result = ItemStack.EMPTY;

        if (player.hasInfiniteMaterials()) {
            result = recipe.result.copy();
        } else if (canCraftRecipe(player, recipe)) {
            List<ItemStack> subsum = recipe.sum.stream().map(stack -> stack.copy()).toList();
            for (ItemStack stack : subsum) {
                for (ItemStack istack : player.getInventory().items) {
                    if (stack.getCount() > 0 && ItemStack.isSameItemSameComponents(stack, istack)) {
                        // TODO? support damageables
                        int max = Math.min(stack.getCount(), istack.getCount());
                        ItemStack remainder = stack.getCraftingRemainingItem();
                        if (!remainder.isEmpty())
                            CaptchaDeckHandler.ejectAnyItem(player, remainder);
                        istack.shrink(max);
                        stack.shrink(max);
                    }
                }
            }
            result = recipe.result.copy();
            player.awardStat(Stats.ITEM_CRAFTED.get(result.getItem()));
        }

        if (asCard && !result.isEmpty() && remainingCards() > 0) {
            size--;
            markDirty();
            result = CaptchaCardItem.createCardWithItem(result, player.server);
        }

        return result;
    }

    @Override
    public boolean putItemStack(ServerPlayer player, ItemStack item) {
        return false;
    }

    @Override
    public boolean increaseSize(ServerPlayer player) {
        if (hasHitMaxCards(player, size)) {
            player.displayClientMessage(Component.translatable(CAPTCHA_LIMIT), true);
            return false;
        }

        size++;
        markDirty();

        return true;
    }

    @Override
    public boolean canSwitchFrom(Modus modus) {
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int remainingCards() {
        return size - list.size() * 10;
    }

    public NonNullList<CraftingModusRecipe> getRecipes() {
        if (changed) {
            fillRecipes(list);
        }

        return recipes;
    }

    public void addRecipe(CraftingModusRecipe recipe) {
        list.add(recipe);
        changed = true;
        markDirty();
    }

    protected boolean canCraftRecipe(ServerPlayer player, CraftingModusRecipe recipe) {
        var allneeded = recipe.sum.stream().map(stack -> stack.copy()).toList();

        for (ItemStack needed : allneeded) {
            player.getInventory().items.forEach(stack -> {
                if (!needed.isEmpty() && ItemStack.isSameItemSameComponents(stack, needed)) {
                    needed.shrink(stack.getCount());
                }
            });
        }

        return allneeded.stream().allMatch(stack -> stack.isEmpty());
    }

    public static class CraftingModusRecipe {
        public final CraftingInput ingredients;
        public final ItemStack result;
        public final List<ItemStack> sum;

        public CraftingModusRecipe(CraftingInput ingredients, ItemStack result) {
            this.ingredients = ingredients;
            this.result = result;

            sum = new ArrayList<>();
            for (ItemStack stack : ingredients.items()) {
                if (!stack.isEmpty()) {
                    Optional<ItemStack> ostack = sum.stream()
                            .filter(sub -> ItemStack.isSameItemSameComponents(stack, sub)).findFirst();
                    if (ostack.isPresent()) {
                        ostack.get().grow(stack.getCount());
                    } else {
                        sum.add(stack.copy());
                    }
                }
            }
        }

        public static Optional<CraftingModusRecipe> parse(Provider provider, CompoundTag nbt) {
            ItemStack result = ItemStack.EMPTY;
            List<ItemStack> ingredients = new ArrayList<>(9);

            if (nbt.contains("result", Tag.TAG_COMPOUND))
                result = ItemStack.parse(provider, nbt.getCompound("result")).orElseThrow();
            else
                return Optional.empty();

            for (int i = 0; i < 9; i++) {
                if (nbt.contains("item" + i, Tag.TAG_COMPOUND))
                    ingredients.add(i, ItemStack.parse(provider, nbt.getCompound("item" +
                            i)).orElseThrow());
                else
                    ingredients.add(i, ItemStack.EMPTY);
            }

            int width = nbt.getInt("width");
            int height = nbt.getInt("height");

            return Optional.of(new CraftingModusRecipe(CraftingInput.of(width, height, ingredients), result));
        }

        public Tag save(Provider provider) {
            CompoundTag nbt = new CompoundTag();

            nbt.put("result", result.save(provider));

            for (int i = 0; i < ingredients.size(); i++) {
                ItemStack item = ingredients.getItem(i);
                if (!item.isEmpty())
                    nbt.put("item" + i, item.save(provider));
            }
            nbt.putInt("width", ingredients.width());
            nbt.putInt("height", ingredients.height());

            return nbt;
        }
    }
}
