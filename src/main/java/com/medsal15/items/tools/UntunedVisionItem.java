package com.medsal15.items.tools;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.medsal15.config.ConfigServer;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.player.Title;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class UntunedVisionItem extends Item {
    public UntunedVisionItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (ConfigServer.VISION_TUNING.getAsBoolean() && player instanceof ServerPlayer serverPlayer) {
            Optional<Title> otitle = Title.getTitle(serverPlayer);
            ItemStack result = ItemStack.EMPTY;
            if (otitle.isPresent()) {
                Title title = otitle.get();
                Holder<Item> item = switch (title.heroAspect()) {
                    case SPACE -> ESItems.VISION_SPACE;
                    case TIME -> ESItems.VISION_TIME;
                    case MIND -> ESItems.VISION_MIND;
                    case HEART -> ESItems.VISION_HEART;
                    case HOPE -> ESItems.VISION_HOPE;
                    case RAGE -> ESItems.VISION_RAGE;
                    case BREATH -> ESItems.VISION_BREATH;
                    case BLOOD -> ESItems.VISION_BLOOD;
                    case LIFE -> ESItems.VISION_LIFE;
                    case DOOM -> ESItems.VISION_DOOM;
                    case LIGHT -> ESItems.VISION_LIGHT;
                    case VOID -> ESItems.VISION_VOID;
                };
                result = new ItemStack(item, 1, stack.getComponentsPatch());
            } else if (!ConfigServer.VISION_TUNING_SAFE.getAsBoolean()) {
                result = new ItemStack(ESItems.VISION_DULL, 1, stack.getComponentsPatch());
            }

            if (!result.isEmpty()) {
                stack.consume(1, serverPlayer);
                if (!player.getInventory().add(result)) {
                    player.drop(stack, false);
                }
                return InteractionResultHolder.consume(stack);
            }
        }
        return super.use(level, player, hand);
    }
}
