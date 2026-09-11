package com.medsal15.items.crossbow;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.config.ConfigClient;
import com.medsal15.data.ESLangProvider;
import com.medsal15.utils.ESLangHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class DeepCrossbowItem extends CrossbowItem {
    private final float velocity;

    public DeepCrossbowItem(Properties properties, float velocity) {
        super(properties);
        this.velocity = velocity;
    }

    @Override
    protected void shoot(@Nonnull ServerLevel level, @Nonnull LivingEntity shooter, @Nonnull InteractionHand hand,
            @Nonnull ItemStack weapon, @Nonnull List<ItemStack> projectileItems, float velocity, float inaccuracy,
            boolean isCrit, @Nullable LivingEntity target) {
        super.shoot(level, shooter, hand, weapon, projectileItems, velocity * this.velocity, inaccuracy, isCrit,
                target);
    }

    @Override
    public int getEnchantmentLevel(@Nonnull ItemStack stack, @Nonnull Holder<Enchantment> enchantment) {
        int level = super.getEnchantmentLevel(stack, enchantment);

        if (enchantment.is(Enchantments.MENDING)) {
            level += 1;
        }

        return level;
    }

    @Override
    public ItemEnchantments getAllEnchantments(@Nonnull ItemStack stack, @Nonnull RegistryLookup<Enchantment> lookup) {
        ItemEnchantments list = super.getAllEnchantments(stack, lookup);
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(list);

        Optional<Reference<Enchantment>> echantment = lookup.get(Enchantments.MENDING);
        if (echantment.isPresent()) {
            mutable.upgrade(echantment.get(), 1);
        }

        return mutable.toImmutable();
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if (!ConfigClient.displayInnateEnchants)
            return;

        ResourceKey<Enchantment> enchantment = Enchantments.MENDING;
        MutableComponent ench = Component.translatable(ESLangHelper.getEnchantmentKey(enchantment));
        tooltipComponents
                .add(Component.translatable(ESLangProvider.INNATE_ENCHANT_KEY, 1, ench).withStyle(ChatFormatting.GRAY));
    }
}
