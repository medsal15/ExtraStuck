package com.medsal15.modus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;

public class CompactModus extends BaseModus {
    public static final byte STRICTNESS_MODE = 0;
    public static final byte SHIFT_INDEX_UP = 1;
    public static final byte SHIFT_INDEX_DOWN = 2;

    protected boolean strict = false;
    protected List<List<Integer>> groups;

    public CompactModus(ModusType<? extends CompactModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public void initModus(ItemStack item, ServerPlayer player, NonNullList<ItemStack> prev, int size) {
        super.initModus(item, player, prev, size);

        groups = new ArrayList<>();
    }

    @Override
    public void readFromNBT(CompoundTag nbt, Provider provider) {
        super.readFromNBT(nbt, provider);

        strict = nbt.getBoolean("strict");

        groups = new ArrayList<>();
        if (nbt.contains("groups")) {
            parseGroups(nbt.getCompound("groups"));
        }
    }

    private void parseGroups(CompoundTag nbt) {
        if (!nbt.contains("size")) {
            return;
        }
        int size = nbt.getInt("size");

        for (int i = 0; i < size; i++) {
            if (!nbt.contains("group" + i))
                continue;

            List<Integer> list = new ArrayList<>();
            Arrays.stream(nbt.getIntArray("group" + i)).boxed().forEach(n -> list.add(n));

            groups.add(list);
        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt, Provider provider) {
        nbt.putBoolean("strict", strict);

        CompoundTag groupsTag = new CompoundTag();
        groupsTag.putInt("size", groups.size());
        for (int i = 0; i < groups.size(); i++) {
            groupsTag.putIntArray("group" + i, groups.get(i));
        }
        nbt.put("groups", groupsTag);

        return super.writeToNBT(nbt, provider);
    }

    @Override
    public void setValue(ServerPlayer player, byte type, int value) {
        switch (type) {
            case STRICTNESS_MODE:
                if (strict != value > 0) {
                    strict = value > 0;
                    changed = true;
                    computeGroups();
                    markDirty();
                }
                break;
            case SHIFT_INDEX_UP:
                if (value < groups.size() && value >= 0) {
                    List<Integer> group = new ArrayList<>(groups.get(value));
                    group.addFirst(group.removeLast());
                    groups.set(value, group);
                    changed = true;
                    markDirty();
                }
                break;
            case SHIFT_INDEX_DOWN:
                if (value < groups.size() && value >= 0) {
                    List<Integer> group = new ArrayList<>(groups.get(value));
                    group.addLast(group.removeFirst());
                    groups.set(value, group);
                    changed = true;
                    markDirty();
                }
                break;
        }
    }

    /** Total cards used */
    public int groupedCount() {
        int sum = 0;
        for (List<Integer> group : groups) {
            sum += group.size();
        }
        return sum;
    }

    @Override
    public boolean putItemStack(ServerPlayer player, ItemStack item) {
        if (size <= list.size() || item.isEmpty())
            return false;

        if (list.size() < size) {
            list.add(item);

            addToGroup(item, list.indexOf(item));
            markDirty();

            return true;
        }

        return false;
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
                CaptchaDeckHandler.ejectAnyItem(player, item);
            }
            list.clear();
            groups.clear();
            markDirty();
            return ItemStack.EMPTY;
        }

        if (slot < 0 || slot >= list.size())
            return ItemStack.EMPTY;

        ItemStack item = list.remove(slot);
        removeFromGroup(slot);

        if (asCard) {
            size--;
            item = CaptchaCardItem.createCardWithItem(item, player.server);
        }

        markDirty();

        return item;
    }

    protected void computeGroups() {
        if (list == null)
            return;
        List<ItemStack> stacks = new ArrayList<>();
        groups = new ArrayList<>();
        BiFunction<ItemStack, ItemStack, Boolean> match = strict ? ItemStack::isSameItemSameComponents
                : ItemStack::isSameItem;

        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = list.get(i);
            if (!stack.isEmpty()) {
                OptionalInt result = IntStream.range(0, stacks.size())
                        .filter(j -> match.apply(stack.copyWithCount(1), stacks.get(j)))
                        .findFirst();

                if (result.isPresent()) {
                    groups.get(result.getAsInt()).add(i);
                } else {
                    stacks.add(stack.copyWithCount(1));
                    List<Integer> newList = new ArrayList<>();
                    newList.add(i);
                    groups.add(newList);
                }
            }
        }
    }

    protected void addToGroup(ItemStack stack, int index) {
        BiFunction<ItemStack, ItemStack, Boolean> match = strict ? ItemStack::isSameItemSameComponents
                : ItemStack::isSameItem;

        for (int i = 0; i < groups.size(); i++) {
            List<Integer> group = groups.get(i);
            ItemStack reference = list.get(group.get(0));
            if (match.apply(stack, reference)) {
                group = new ArrayList<>(group);
                group.add(index);
                groups.set(i, group);
                return;
            }
        }

        List<Integer> list = new ArrayList<>();
        list.add(index);
        groups.add(list);
    }

    protected void removeFromGroup(int index) {
        for (int i = 0; i < groups.size(); i++) {
            List<Integer> group = groups.get(i);
            group = group.stream().filter(n -> n != index).map(n -> n > index ? n - 1 : n).toList();
            if (group.size() == 0) {
                groups.remove(i);
                i--;
            } else {
                groups.set(i, group);
            }
        }
    }

    public List<List<Integer>> getGroups() {
        return groups;
    }

    public void switchStrictness() {
        strict = !strict;
        computeGroups();
        markDirty();
    }

    public boolean isStrict() {
        return strict;
    }
}
