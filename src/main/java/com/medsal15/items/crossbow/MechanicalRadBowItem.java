package com.medsal15.items.crossbow;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.equipment.armor.BacktankUtil;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.neoforged.fml.ModList;

public class MechanicalRadBowItem extends RadBowItem {
    public MechanicalRadBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public <T extends LivingEntity> int damageItem(@Nonnull ItemStack stack, int amount,
            @SuppressWarnings("null") @Nullable T entity,
            @Nonnull Consumer<Item> onBroken) {
        // Special support for create, consume backtank first
        if (ModList.get().isLoaded("create") && stack.has(DataComponents.MAX_DAMAGE)) {
            if (BacktankUtil.canAbsorbDamage(entity, stack.getOrDefault(DataComponents.MAX_DAMAGE, 1)))
                return 0;
        }
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public int getEnchantmentValue() {
        return Tiers.GOLD.getEnchantmentValue();
    }
}
