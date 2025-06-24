package com.medsal15.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.capabilities.Capabilities;

import static com.medsal15.data.ESItemTags.AMMO;
import static com.medsal15.data.ESLangProvider.GUN_CONTENT_KEY;
import static com.medsal15.data.ESLangProvider.GUN_EMPTY_KEY;

import java.util.List;
import javax.annotation.Nonnull;

public class GunItem extends Item implements FilterContainer.Filter {
    private TagKey<Item> ammo;

    public GunItem(Properties properties) {
        super(properties);
        this.ammo = AMMO;
    }

    public GunItem(Properties properties, TagKey<Item> ammo) {
        super(properties);
        this.ammo = ammo;
    }

    public boolean accepts(ItemStack stack) {
        return stack.is(ammo);
    }

    @Override
    public boolean overrideStackedOnOther(@Nonnull ItemStack stack, @Nonnull Slot slot, @Nonnull ClickAction action,
            @Nonnull Player player) {
        if (stack.getCount() != 1 || action != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemContainerContents contents = stack.get(DataComponents.CONTAINER);

            if (contents == null) {
                return false;
            } else {
                ItemStack other = slot.getItem();
                @SuppressWarnings("null")
                var handler = Capabilities.ItemHandler.ITEM.getCapability(stack, null);

                if (other.isEmpty()) {
                    var itemStack = handler.extractItem(0, ABSOLUTE_MAX_STACK_SIZE, false);
                    if (!itemStack.isEmpty()) {
                        var otherStack = slot.safeInsert(itemStack);
                        handler.insertItem(0, otherStack, false);
                    }
                } else if (handler.isItemValid(0, other)) {
                    var res = handler.insertItem(0, other, false);
                    slot.setByPlayer(res);
                }

                return true;
            }
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(@Nonnull ItemStack stack, @Nonnull ItemStack other, @Nonnull Slot slot,
            @Nonnull ClickAction action, @Nonnull Player player, @Nonnull SlotAccess access) {
        if (stack.getCount() != 1) {
            return false;
        }
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            ItemContainerContents contents = stack.get(DataComponents.CONTAINER);

            if (contents == null) {
                return false;
            } else {
                @SuppressWarnings("null")
                var handler = Capabilities.ItemHandler.ITEM.getCapability(stack, null);

                if (other.isEmpty()) {
                    var itemStack = handler.extractItem(0, ABSOLUTE_MAX_STACK_SIZE, false);
                    if (!itemStack.isEmpty()) {
                        access.set(itemStack);
                    }
                } else {
                    var itemStack = handler.insertItem(0, other, false);
                    access.set(itemStack);
                }

                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        var contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        if (contents.equals(ItemContainerContents.EMPTY)) {
            tooltipComponents.add(Component.translatable(GUN_EMPTY_KEY).withStyle(ChatFormatting.GRAY));
        } else {
            var itemStack = contents.copyOne();
            tooltipComponents
                    .add(Component.translatable(GUN_CONTENT_KEY, itemStack.getCount(), itemStack.getDisplayName())
                            .withStyle(ChatFormatting.GRAY));
        }
    }

    // TODO use to zoom

    // TODO attack to shoot (and damage if applicable)

    // TODO add sounds for unloading/loading ammo, shooting (or failing)
}
