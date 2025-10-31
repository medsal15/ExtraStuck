package com.medsal15.items;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.data.ESLangProvider;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.components.GristLayer;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.world.lands.GristLayerInfo;
import com.mraof.minestuck.world.lands.GristTypeLayer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class GristDetectorItem extends Item {
    public GristDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand usedHand) {
        if (player.isCrouching()) {
            ItemStack stack = player.getItemInHand(usedHand);
            GristLayer layer = stack.getOrDefault(ESDataComponents.GRIST_LAYER, GristLayer.COMMON);
            stack.set(ESDataComponents.GRIST_LAYER, layer.next());
            return InteractionResultHolder.success(stack);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slotId,
            boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level instanceof ServerLevel serverLevel) {
            Optional<GristLayerInfo> optionalInfo = GristLayerInfo.get(serverLevel);
            if (optionalInfo.isPresent()) {
                GristLayerInfo info = optionalInfo.get();
                GristLayer layer = stack.getOrDefault(ESDataComponents.GRIST_LAYER, GristLayer.COMMON);
                GristTypeLayer layerInfo = null;
                switch (layer) {
                    case GristLayer.UNCOMMON:
                        layerInfo = info.getUncommonGristLayer();
                        break;
                    case GristLayer.ANY:
                        layerInfo = info.getAnyGristLayer();
                        break;
                    case GristLayer.COMMON:
                    default:
                        layerInfo = info.getCommonGristLayer();
                        break;
                }
                GristType found = layerInfo.getTypeAt(entity.blockPosition().getX(), entity.blockPosition().getZ());
                stack.set(ESDataComponents.GRIST_FOUND, found);
            } else {
                stack.remove(ESDataComponents.GRIST_FOUND);
            }
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> components, @Nonnull TooltipFlag tooltipFlag) {
        GristLayer layer = stack.getOrDefault(ESDataComponents.GRIST_LAYER, GristLayer.COMMON);
        String layerKey = ExtraStuck.MODID + ".grist_detector." + layer.name().toLowerCase();
        components.add(Component.translatable(ESLangProvider.GRIST_DETECTOR_MODE, Component.translatable(layerKey)));

        if (stack.has(ESDataComponents.GRIST_FOUND)) {
            GristType found = stack.get(ESDataComponents.GRIST_FOUND);
            if (found != null) {
                components.add(Component.translatable(ESLangProvider.GRIST_DETECTOR_LOCATED,
                        Component.translatable(found.getTranslationKey())));
            }
        }
    }
}
