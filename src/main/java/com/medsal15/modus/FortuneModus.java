package com.medsal15.modus;

import java.util.List;

import com.medsal15.items.ESItems;
import com.mraof.minestuck.inventory.captchalogue.ModusType;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.fml.LogicalSide;

public class FortuneModus extends BaseModus {
    public FortuneModus(ModusType<? extends FortuneModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int id, boolean asCard) {
        ItemStack item = super.getItem(player, id, asCard);
        if (!asCard) {
            ItemContainerContents contents = ItemContainerContents.fromItems(List.of(item));
            ItemStack cookie = new ItemStack(ESItems.FORTUNE_COOKIE.get());
            cookie.set(DataComponents.CONTAINER, contents);
            item = cookie;
        }
        return item;
    }
}
