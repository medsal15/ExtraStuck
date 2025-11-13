package com.medsal15.items.melee;

import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.data.ESLangProvider;
import com.medsal15.items.components.ESDataComponents;
import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class JackpotWeapon extends WeaponItem {
    public JackpotWeapon(Builder builder, Properties properties) {
        super(builder, properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if (stack.has(ESDataComponents.ROLLS)) {
            List<Integer> lastRoll = stack.get(ESDataComponents.ROLLS);
            tooltipComponents.add(Component.translatable(ESLangProvider.JACKPOT_ROLL_KEY,
                    lastRoll.stream().map(i -> Component.translatable(ESLangProvider.JACKPOT_ROLLED_BASE + i))
                            .toArray()));
        }
    }
}
