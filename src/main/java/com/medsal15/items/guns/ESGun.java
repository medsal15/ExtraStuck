package com.medsal15.items.guns;

import static com.medsal15.data.ESItemTags.AMMO;
import static com.medsal15.data.ESLangProvider.GUN_CONTENT_KEY;
import static com.medsal15.data.ESLangProvider.GUN_EMPTY_KEY;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.items.ESItems;
import com.medsal15.items.projectiles.ESBulletItem;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.registries.DeferredItem;

public class ESGun extends ProjectileWeaponItem implements GunContainer.Filter {
    private TagKey<Item> ammo = AMMO;
    private DeferredItem<Item> next;

    public ESGun(Properties properties) {
        super(properties);
    }

    public ESGun(Properties properties, TagKey<Item> ammo) {
        super(properties);
        this.ammo = ammo;
    }

    public ESGun setNext(DeferredItem<Item> next) {
        this.next = next;
        return this;
    }

    public boolean accepts(ItemStack stack) {
        return stack.is(ammo);
    }

    /** Maximum amount of bullets held */
    public int getMaxBullets() {
        return DEFAULT_MAX_STACK_SIZE;
    }

    public float getZoom() {
        return 1F;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 5;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return s -> s.is(ammo);
    }

    @Override
    protected void shootProjectile(@Nonnull LivingEntity shooter, @Nonnull Projectile projectile, int index,
            float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, velocity, inaccuracy);
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
                IItemHandler handler = Capabilities.ItemHandler.ITEM.getCapability(stack, null);

                if (other.isEmpty()) {
                    ItemStack itemStack = handler.extractItem(0, ABSOLUTE_MAX_STACK_SIZE, false);
                    if (!itemStack.isEmpty()) {
                        ItemStack otherStack = slot.safeInsert(itemStack);
                        handler.insertItem(0, otherStack, false);
                    }
                } else if (handler.isItemValid(0, other)) {
                    ItemStack res = handler.insertItem(0, other, false);
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
                IItemHandler handler = Capabilities.ItemHandler.ITEM.getCapability(stack, null);

                if (other.isEmpty()) {
                    ItemStack itemStack = handler.extractItem(0, ABSOLUTE_MAX_STACK_SIZE, false);
                    if (!itemStack.isEmpty()) {
                        access.set(itemStack);
                    }
                } else {
                    ItemStack itemStack = handler.insertItem(0, other, false);
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

        ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        if (contents.equals(ItemContainerContents.EMPTY)) {
            tooltipComponents.add(Component.translatable(GUN_EMPTY_KEY).withStyle(ChatFormatting.GRAY));
        } else {
            ItemStack itemStack = contents.copyOne();
            tooltipComponents
                    .add(Component.translatable(GUN_CONTENT_KEY, itemStack.getCount(), itemStack.getDisplayName())
                            .withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack, @Nonnull LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching() && this.next != null) {
            ItemStack swap = new ItemStack(next.getDelegate(), stack.getCount(), stack.getComponentsPatch());

            return InteractionResultHolder.success(swap);
        }

        ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
        if (contents == null || contents.equals(ItemContainerContents.EMPTY))
            return InteractionResultHolder.fail(stack);

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity,
            int timeCharged) {
        if (entity instanceof Player player) {
            ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
            if (contents == null)
                return;

            @SuppressWarnings("null")
            IItemHandler handler = Capabilities.ItemHandler.ITEM.getCapability(stack, null);
            ItemStack ammo = handler.extractItem(0, 1, false);
            if (ammo.isEmpty())
                return;

            if (level instanceof ServerLevel serverLevel) {
                this.shoot(serverLevel, player, player.getUsedItemHand(), stack, List.of(ammo), 10, 0f, false, null);
            }
        }
    }

    @Override
    protected Projectile createProjectile(@Nonnull Level level, @Nonnull LivingEntity shooter,
            @Nonnull ItemStack weapon, @Nonnull ItemStack ammo, boolean isCrit) {
        ESBulletItem bulletItem = ammo.getItem() instanceof ESBulletItem b ? b
                : (ESBulletItem) ESItems.HANDGUN_BULLET.get();
        AbstractArrow bullet = bulletItem.createBullet(level, ammo, shooter, weapon);

        return bullet;
    }

    // TODO add sounds for unloading/loading ammo, shooting (or failing)
}
