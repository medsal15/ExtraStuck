package com.medsal15.compat.create.items;

import java.util.ArrayList;
import java.util.List;

import com.medsal15.compat.create.menus.GristFilterMenu;
import com.medsal15.items.ESDataComponents;
import com.medsal15.items.components.GristFilterEntry;
import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.logistics.filter.FilterItemStack;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class GristFilterItem extends FilterItem {
    public GristFilterItem(Properties properties) {
        super(properties);
    }

    @Override
    public FilterItemStack makeStackWrapper(ItemStack filter) {
        return new GristFilterItemStack(filter);
    }

    @Override
    public DataComponentType<?> getComponentType() {
        return ESDataComponents.GRIST_FILTER_DATA.get();
    }

    @Override
    public List<Component> makeSummary(ItemStack filter) {
        List<Component> list = new ArrayList<>();

        int count = 0;
        List<GristFilterEntry> entries = filter.getOrDefault(ESDataComponents.GRIST_FILTER_DATA, List.of());
        for (GristFilterEntry entry : entries) {
            if (count > 3) {
                list.add(Component.literal("- ...").withStyle(ChatFormatting.DARK_GRAY));
                break;
            }
            list.add(Component.literal("- ").append(Component.translatable(entry.mode().key(), entry.amount(),
                    Component.translatable(entry.grist().getTranslationKey()))));
            count++;
        }

        return list;
    }

    @Override
    public ItemStack[] getFilterItems(ItemStack stack) {
        return new ItemStack[0];
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return GristFilterMenu.create(id, inv, player.getMainHandItem());
    }
}
