package com.medsal15.items.shields;

import com.medsal15.ExtraStuck;

import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.registries.DeferredItem;

public class ChangeShield extends ESShield implements IShieldBlock {
    private DeferredItem<Item> next;
    private TagKey<DamageType> changer;

    public ChangeShield(Properties properties, DeferredItem<Item> next, TagKey<DamageType> changer) {
        super(properties);
        this.next = next;
        this.changer = changer;
    }

    public boolean onShieldBlock(LivingShieldBlockEvent event) {
        var damageSource = event.getDamageSource();
        if (!damageSource.is(this.changer))
            return false;

        var entity = event.getEntity();
        // There has to be a better way to do that
        InteractionHand hand;
        if (entity.getItemInHand(InteractionHand.MAIN_HAND).is(this)) {
            hand = InteractionHand.MAIN_HAND;
        } else if (entity.getItemInHand(InteractionHand.OFF_HAND).is(this)) {
            hand = InteractionHand.OFF_HAND;
        } else {
            ExtraStuck.LOGGER.warn("ChangeShield.onShieldBlock called without a ChangeShield. This may be a bug");
            return false;
        }
        entity.setItemInHand(hand, new ItemStack(next.get()));
        return true;
    }
}
