package com.medsal15.items.bows;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

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
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

public class SilentShotBowItem extends FastBowItem {
    public SilentShotBowItem(Properties properties, float velocity) {
        super(properties, velocity);
    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse
     * button).
     */
    @Override
    public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entityLiving,
            int timeLeft) {
        if (entityLiving instanceof Player player) {
            ItemStack itemstack = player.getProjectile(stack);
            if (!itemstack.isEmpty()) {
                int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                i = net.neoforged.neoforge.event.EventHooks.onArrowLoose(stack, level, player, i, !itemstack.isEmpty());
                if (i < 0)
                    return;
                float f = getPowerForTime(i);
                if (!((double) f < 0.1)) {
                    List<ItemStack> list = draw(stack, itemstack, player);
                    if (level instanceof ServerLevel serverlevel && !list.isEmpty()) {
                        this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 3.0F, 1.0F,
                                f == 1.0F, null);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
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
