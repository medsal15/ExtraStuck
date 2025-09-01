package com.medsal15.modus;

import com.medsal15.blockentities.CardOreBlockEntity;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.inventory.captchalogue.ModusType;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.fml.LogicalSide;

public class OreModus extends BaseModus {
    public OreModus(ModusType<? extends OreModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int id, boolean asCard) {
        ItemStack item = super.getItem(player, id, asCard);
        if (!item.isEmpty()) {
            ItemStack ore = ESItems.CARD_ORE.toStack();
            CompoundTag tag = new CompoundTag();
            tag.putString("id", "extrastuck:card_ore");
            tag.put(CardOreBlockEntity.ITEM_STORED, item.save(player.level().registryAccess()));
            ore.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tag));
            item = ore;
        }
        return item;
    }
}
