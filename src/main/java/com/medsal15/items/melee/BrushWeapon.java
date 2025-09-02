package com.medsal15.items.melee;

import javax.annotation.Nonnull;

import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class BrushWeapon extends WeaponItem {
    public BrushWeapon(WeaponItem.Builder builder, Properties properties) {
        super(builder, properties);
    }

    @Override
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.BRUSH;
    }
}
