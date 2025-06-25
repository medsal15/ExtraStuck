package com.medsal15.items.melee;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.medsal15.data.ESLangProvider;
import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class InnateEnchantsWeapon extends WeaponItem {
    private Map<ResourceKey<Enchantment>, Integer> innate;

    public InnateEnchantsWeapon(WeaponItem.Builder builder, Properties properties,
            Map<ResourceKey<Enchantment>, Integer> enchants) {
        super(builder, properties);
        this.innate = enchants;
    }

    @Override
    public int getEnchantmentLevel(@Nonnull ItemStack stack, @Nonnull Holder<Enchantment> enchantment) {
        int level = super.getEnchantmentLevel(stack, enchantment);

        if (innate.getOrDefault(enchantment, 0) > 0) {
            level += innate.get(enchantment.getKey());
        }

        return level;
    }

    @Override
    public ItemEnchantments getAllEnchantments(@Nonnull ItemStack stack, @Nonnull RegistryLookup<Enchantment> lookup) {
        var list = super.getAllEnchantments(stack, lookup);
        var mutable = new ItemEnchantments.Mutable(list);

        for (var key : innate.keySet()) {
            var extra = innate.get(key);
            var enchant = lookup.get(key);
            if (enchant.isPresent())
                mutable.upgrade(enchant.get(), extra);
        }

        return mutable.toImmutable();
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        for (var key : innate.keySet()) {
            var extra = innate.get(key);
            var ench = Component.translatable(getEnchantmentKey(key));
            if (extra == 1) {
                tooltipComponents.add(Component.translatable(ESLangProvider.INNATE_ENCHANT_KEY, 1, ench)
                        .withStyle(ChatFormatting.GRAY));
            } else if (extra > 1) {
                tooltipComponents.add(Component.translatable(ESLangProvider.INNATE_ENCHANTS_KEY, extra, ench)
                        .withStyle(ChatFormatting.GRAY));
            }
        }
    }

    private static String getEnchantmentKey(ResourceKey<Enchantment> enchantment) {
        var location = enchantment.location();
        return "enchantment." + location.getNamespace() + "." + location.getPath();
    }
}
