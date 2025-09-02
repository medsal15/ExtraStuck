package com.medsal15.modus;

import com.mraof.minestuck.inventory.captchalogue.ModusType;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.LogicalSide;

public class ArcheologyModus extends BaseModus {
    public ArcheologyModus(ModusType<? extends ArcheologyModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int id, boolean asCard) {
        ItemStack item = super.getItem(player, id, asCard);
        if (!item.isEmpty()) {
            ItemStack sus;
            CompoundTag tag = new CompoundTag();
            if (player.getRandom().nextBoolean()) {
                sus = new ItemStack(Items.SUSPICIOUS_GRAVEL);
            } else {
                sus = new ItemStack(Items.SUSPICIOUS_SAND);
            }
            BlockEntity.addEntityType(tag, BlockEntityType.BRUSHABLE_BLOCK);
            tag.put("item", item.save(player.level().registryAccess()));
            sus.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tag));
            item = sus;
        }
        return item;
    }
}
