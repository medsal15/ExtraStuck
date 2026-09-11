package com.medsal15.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.medsal15.config.ConfigCommon;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Pseudo
@Mixin(targets = { "com.rosebushes.alchemyexpanded.item.weapon.gun.GunWeaponItem" })
// Fine, I'll fix it myself
public abstract class AEGunFix {
    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    public void fixUse(Level level, Player player, InteractionHand usedHand,
            CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cb) {
        if (!ConfigCommon.AE_GUNFIX.getAsBoolean())
            return;

        final com.rosebushes.alchemyexpanded.item.weapon.gun.GunWeaponItem self = (com.rosebushes.alchemyexpanded.item.weapon.gun.GunWeaponItem) (Object) this;
        boolean mainHand = usedHand == InteractionHand.MAIN_HAND;
        ItemStack gun = player.getItemInHand(usedHand);
        ItemStack offHandItem = mainHand ? player.getItemInHand(InteractionHand.OFF_HAND)
                : player.getItemInHand(InteractionHand.MAIN_HAND);
        boolean offHandEmpty = offHandItem.isEmpty();

        if (!mainHand && !offHandEmpty
                && offHandItem.getItem() instanceof com.rosebushes.alchemyexpanded.item.weapon.gun.GunWeaponItem
                && !self.getDualWieldable()) {
            return;
        }
        if (!self.getInnocuousDouble().isEmpty() && player.isShiftKeyDown() && self.getAmmo(gun) > 0) {
            ItemStack newItem = self.getInnocuousDouble();
            newItem = new ItemStack(newItem.getItemHolder(), newItem.getCount(), gun.getComponentsPatch());
            cb.setReturnValue(InteractionResultHolder.success(newItem));
        }
    }
}
