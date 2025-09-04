package com.medsal15.items;

import java.util.List;

import javax.annotation.Nonnull;

import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.entity.item.VitalityGelEntity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class MagnetItem extends TieredItem {
    private static final double RANGE = 5;

    public MagnetItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand usedHand) {
        boolean success = false;
        AABB box = new AABB(player.blockPosition()).inflate(RANGE);
        List<Entity> entities = level.getEntities(player, box,
                e -> e instanceof ItemEntity || e instanceof GristEntity || e instanceof VitalityGelEntity);

        for (Entity entity : entities) {
            if (entity instanceof ItemEntity itemEntity) {
                ItemStack stack = itemEntity.getItem();
                if (player.addItem(stack)) {
                    success = true;
                    itemEntity.discard();
                }
            } else if (entity instanceof GristEntity gristEntity) {
                gristEntity.playerTouch(player);
                success = true;
            } else if (entity instanceof VitalityGelEntity gelEntity) {
                gelEntity.playerTouch(player);
                success = true;
            }
        }

        if (success) {
            EquipmentSlot slot = null;
            if (usedHand == InteractionHand.MAIN_HAND)
                slot = EquipmentSlot.MAINHAND;
            else if (usedHand == InteractionHand.OFF_HAND)
                slot = EquipmentSlot.OFFHAND;
            if (slot != null) {
                player.getItemInHand(usedHand).hurtAndBreak(1, player, slot);
            }

            return InteractionResultHolder.consume(player.getItemInHand(usedHand));
        }

        return super.use(level, player, usedHand);
    }
}
