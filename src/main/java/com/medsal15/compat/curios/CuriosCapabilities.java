package com.medsal15.compat.curios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.medsal15.config.ConfigServer;
import com.medsal15.items.ESItems;
import com.medsal15.items.tools.IVision;
import com.medsal15.items.tools.VisionItem.VisionEffect;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class CuriosCapabilities {
    public static void registerCuriosCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerItem(CuriosCapability.ITEM, (stack, context) -> new ICurio() {
            public ItemStack getStack() {
                return stack;
            };

            public void curioTick(SlotContext slotContext) {
                visionTick(slotContext);
            };
        }, ESItems.VISION_SPACE, ESItems.VISION_TIME, ESItems.VISION_MIND, ESItems.VISION_HEART, ESItems.VISION_HOPE,
                ESItems.VISION_RAGE, ESItems.VISION_BREATH, ESItems.VISION_BLOOD, ESItems.VISION_LIFE,
                ESItems.VISION_DOOM, ESItems.VISION_LIGHT, ESItems.VISION_VOID);

        event.registerItem(CuriosCapability.ITEM, (stack, context) -> new ICurio() {
            public ItemStack getStack() {
                return stack;
            };
        }, ESItems.GUMMY_RING, ESItems.SILVER_WATCH);

        event.registerItem(CuriosCapability.ITEM, (stack, context) -> new ICurio() {
            public ItemStack getStack() {
                return stack;
            };

            public void curioTick(SlotContext slotContext) {
                if (!(slotContext.entity() instanceof Player player) || player.level().isClientSide
                        || player.getCooldowns().isOnCooldown(stack.getItem()))
                    return;

                double luck = player.getAttributeValue(Attributes.LUCK);
                double chanceBonus = 1;
                double chanceMalus = 1;

                if (luck > 0)
                    chanceBonus += luck;
                else
                    chanceMalus += luck;
                if (Title.isPlayerOfAspect((ServerPlayer) player, EnumAspect.TIME))
                    chanceBonus *= 2;

                if (player.getRandom().nextDouble() > chanceBonus / (chanceBonus + chanceMalus)) {
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 600));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600));
                } else {
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600));
                }

                player.getCooldowns().addCooldown(stack.getItem(), 2400);
            };
        }, ESItems.BROKEN_WATCH);
    }

    private static void visionTick(SlotContext context) {
        if (context.cosmetic())
            return;

        int limit = ConfigServer.VISION_LIMIT.get();
        if (limit == 0)
            return;

        int visions = 0;
        LivingEntity entity = context.entity();
        List<VisionEffect> effects = new ArrayList<>();
        Optional<ICuriosItemHandler> oinventory = CuriosApi.getCuriosInventory(entity);
        if (oinventory.isPresent()) {
            ICuriosItemHandler inventory = oinventory.get();
            int max = inventory.getEquippedCurios().getSlots();
            for (int i = 0; i < max; i++) {
                ItemStack stack = inventory.getEquippedCurios().getStackInSlot(i);
                if (stack.is(ESTags.Items.ACTIVE_VISION)) {
                    visions++;
                    if (stack.getItem() instanceof IVision vision)
                        effects.addAll(vision.getEffects());
                }
            }
        }
        if (visions <= limit) {
            effects.forEach(eff -> entity.addEffect(new MobEffectInstance(eff.effect(), 100, eff.amplifier())));
        }
    }
}
