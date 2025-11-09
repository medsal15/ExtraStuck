package com.medsal15.subevents;

import static com.medsal15.ExtraStuck.modid;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.ExtraStuck;
import com.medsal15.blockentities.ESBlockEntities;
import com.medsal15.blockentities.PrinterBlockEntity;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.client.model.armor.HeavyBootsModel;
import com.medsal15.client.programs.MastermindAppScreen;
import com.medsal15.client.renderers.ChargerBlockRenderer;
import com.medsal15.client.renderers.ESArrowRenderer;
import com.medsal15.computer.ESProgramTypes;
import com.medsal15.config.ConfigClient;
import com.medsal15.config.ConfigClient.BoondollarDisplayMode;
import com.medsal15.data.ESLangProvider;
import com.medsal15.entities.ESEntities;
import com.medsal15.entities.projectiles.CaptainJusticeShield;
import com.medsal15.entities.projectiles.bullets.ItemBullet;
import com.medsal15.items.ESItems;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.components.MoonCakeSliceColor;
import com.medsal15.items.components.SteamFuelComponent;
import com.medsal15.items.crossbow.RadBowItem;
import com.medsal15.items.shields.ESShield;
import com.medsal15.items.shields.ESShield.BlockFuncs;
import com.medsal15.particles.ESParticleTypes;
import com.medsal15.particles.UraniumBlastParticle;
import com.medsal15.storage.ESBoondollarValues;
import com.medsal15.storage.ESBoondollarValues.BoondollarValue;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.client.gui.computer.ProgramGui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.registries.DeferredItem;

@EventBusSubscriber(modid = ExtraStuck.MODID, value = Dist.CLIENT)
public final class ClientEvents {
    private static RandomSource random = RandomSource.create();
    @Nullable
    private static BoondollarValue lastValue = null;
    private static int lastRandomValue;
    @Nullable
    private static Item lastItem = null;

    @SubscribeEvent
    public static void addCustomTooltip(final ItemTooltipEvent event) {
        int i = 1;
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        Player player = event.getEntity();
        List<Component> tooltip = event.getToolTip();

        if (player != null && !stack.isEmpty()) {
            // Steam-powered stuff
            if (stack.has(ESDataComponents.STEAM_FUEL)) {
                SteamFuelComponent fuel = stack.get(ESDataComponents.STEAM_FUEL);
                List<Component> list = new ArrayList<>();
                fuel.addToTooltip(event.getContext(), list::add, event.getFlags());
                tooltip.addAll(i, list);
                i += list.size();
            }

            // custom value
            boolean show_value = false;
            for (ItemStack armor : player.getInventory().armor) {
                if (armor.is(ESTags.Items.SHOW_VALUE)) {
                    show_value = true;
                    break;
                }
            }
            if (show_value && ConfigClient.boondollarDisplayMode != BoondollarDisplayMode.DISABLED) {
                boolean update_last = false;
                if (item != lastItem) {
                    lastItem = item;
                    lastValue = ESBoondollarValues.getInstance().getValue(stack).orElse(null);
                    update_last = lastValue != null;
                }
                if (lastValue != null) {
                    BoondollarValue value = lastValue;
                    Component text = null;
                    if (value.min() == value.max()) {
                        text = Component.translatable(ESLangProvider.BOONDOLLAR_VALUE_KEY, value.min());
                        lastRandomValue = 0;
                    } else {
                        switch (ConfigClient.boondollarDisplayMode) {
                            case DISABLED:
                                break;
                            case RAW:
                                text = Component.translatable(ESLangProvider.BOONDOLLAR_RANGE_KEY, value.min(),
                                        value.max());
                                lastRandomValue = 0;
                                break;
                            case AVERAGE:
                                text = Component.translatable(ESLangProvider.BOONDOLLAR_VALUE_KEY,
                                        (value.min() + value.max()) / 2);
                                lastRandomValue = 0;
                                break;
                            case RANDOM:
                                if (lastRandomValue == 0 || update_last) {
                                    lastRandomValue = random.nextIntBetweenInclusive(value.min(), value.max());
                                }
                                text = Component.translatable(ESLangProvider.BOONDOLLAR_VALUE_KEY, lastRandomValue);
                                break;
                        }
                    }
                    if (text != null) {
                        tooltip.add(i, text);
                        i++;
                    }
                }
            }
        }

        // Only for this mod
        final ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (itemId == null || !itemId.getNamespace().equals(ExtraStuck.MODID))
            return;

        // Fancy item descriptions
        String tooltip_key = stack.getDescriptionId() + ".tooltip";
        if (I18n.exists(tooltip_key)) {
            tooltip.add(i, Component.translatable(tooltip_key).withStyle(ChatFormatting.GRAY));
            i++;
        }

        // Shield info
        if (ConfigClient.displayShieldInfo) {
            if (item instanceof ESShield shield && shield.hasOnBlock(BlockFuncs.DAMAGE)) {
                tooltip.add(i,
                        Component
                                .translatable(ESLangProvider.SHIELD_DAMAGE_KEY,
                                        stack.get(ESDataComponents.SHIELD_DAMAGE).intValue())
                                .withStyle(ChatFormatting.GRAY));
                i++;
            }
        }

        // RF
        @SuppressWarnings("null")
        IEnergyStorage energyStorage = Capabilities.EnergyStorage.ITEM.getCapability(stack, null);
        if (energyStorage != null) {
            tooltip.add(i, Component.translatable(ESLangProvider.ENERGY_STORAGE_KEY,
                    NumberFormat.getInstance().format(energyStorage.getEnergyStored()),
                    NumberFormat.getInstance().format(energyStorage.getMaxEnergyStored())));
            i++;
        }
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        ItemProperties.register(ESItems.GRIST_DETECTOR.get(), ExtraStuck.modid("found"),
                (stack, world, entity, entityId) -> stack.has(ESDataComponents.GRIST_FOUND) ? 1F : 0F);

        addBrush(ESItems.OLD_BRUSH);
        addBrush(ESItems.BROOM);

        for (DeferredItem<Item> shield : ESItems.getShields()) {
            addBlocking(shield);
        }
        for (DeferredItem<Item> crossbow : ESItems.getCrossbows()) {
            addCrossbow(crossbow);
        }

        ItemProperties.register(ESItems.FLUX_SHIELD.get(), ExtraStuck.modid("charged"),
                (stack, world, entity, entityId) -> {
                    @SuppressWarnings("null")
                    IEnergyStorage handler = Capabilities.EnergyStorage.ITEM.getCapability(stack,
                            null);
                    if (handler != null
                            && handler.getEnergyStored() >= stack.getOrDefault(
                                    ESDataComponents.FLUX_MULTIPLIER, 100))
                        return 1;
                    return 0;
                });

        ItemProperties.register(ESItems.MOON_CAKE_SLICE.get(), ExtraStuck.modid("moon_cake"),
                (stack, world, entity, entityId) -> {
                    MoonCakeSliceColor color = stack.getOrDefault(
                            ESDataComponents.MOON_CAKE_SLICE_COLOR,
                            MoonCakeSliceColor.DUAL);
                    switch (color) {
                        case MoonCakeSliceColor.DUAL:
                        default:
                            return 0;
                        case MoonCakeSliceColor.DERSE:
                            return .5F;
                        case MoonCakeSliceColor.PROSPIT:
                            return 1;
                    }
                });

        addSteamBurning(ESItems.STEAM_HAMMER);

        ProgramGui.Registry.register(ESProgramTypes.MASTERMIND_CODEBREAKER, MastermindAppScreen::new);
    }

    private static void addBlocking(DeferredItem<Item> item) {
        ItemProperties.register(item.get(), ResourceLocation.withDefaultNamespace("blocking"),
                (stack, world, entity, entityid) -> entity != null && entity.isUsingItem()
                        && entity.getUseItem() == stack ? 1F : 0F);
    }

    private static void addCrossbow(DeferredItem<Item> item) {
        ItemProperties.register(item.get(), ResourceLocation.withDefaultNamespace("pulling"), (stack, world,
                entity,
                entityId) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack
                        && !RadBowItem.isCharged(stack) ? 1F : 0F);
        ItemProperties.register(item.get(), ResourceLocation.withDefaultNamespace("pull"),
                (stack, world, entity, entityId) -> {
                    if (entity == null || RadBowItem.isCharged(stack))
                        return 0F;
                    return (float) (stack.getUseDuration(entity)
                            - entity.getUseItemRemainingTicks())
                            / (float) (CrossbowItem.getChargeDuration(stack, entity));
                });
        ItemProperties.register(item.get(), ResourceLocation.withDefaultNamespace("charged"), (stack, world,
                entity, entityId) -> RadBowItem.isCharged(stack) ? 1F : 0F);
    }

    private static void addBrush(DeferredItem<Item> item) {
        ItemProperties.register(item.get(), ResourceLocation.withDefaultNamespace("brushing"),
                (stack, world,
                        entity, entityId) -> entity != null && entity.getUseItem() == stack
                                ? (float) (entity.getUseItemRemainingTicks() % 10) / 10F
                                : 0F);
    }

    /**
     * If the steam-powered weapon is enabled, the property
     * <code>extrastuck:burning</code> is set to 1
     */
    private static void addSteamBurning(DeferredItem<Item> item) {
        ItemProperties.register(item.get(), ExtraStuck.modid("burning"), (stack, world, entity, entityId) -> {
            if (!stack.has(ESDataComponents.STEAM_FUEL))
                return 0;
            return stack.get(ESDataComponents.STEAM_FUEL).burning() ? 1 : 0;
        });
    }

    @SubscribeEvent
    public static void registerEntityRenderers(final RegisterRenderers event) {
        event.registerEntityRenderer(ESEntities.CAPTAIN_JUSTICE_SHIELD.get(),
                CaptainJusticeShield.CJSRenderer::new);

        event.registerEntityRenderer(ESEntities.FLAME_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/flame.png")));
        event.registerEntityRenderer(ESEntities.NETHER_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/nether.png")));
        event.registerEntityRenderer(ESEntities.CARDBOARD_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/cardboard.png")));
        event.registerEntityRenderer(ESEntities.MISSED_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/missed.png")));
        event.registerEntityRenderer(ESEntities.CANDY_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/candy.png")));
        event.registerEntityRenderer(ESEntities.LIGHTNING_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/lightning.png")));
        event.registerEntityRenderer(ESEntities.EXPLOSIVE_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/explosive.png")));
        event.registerEntityRenderer(ESEntities.IRON_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/iron.png")));
        event.registerEntityRenderer(ESEntities.QUARTZ_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/quartz.png")));
        event.registerEntityRenderer(ESEntities.PRISMARINE_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/prismarine.png")));
        event.registerEntityRenderer(ESEntities.GLASS_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/glass.png")));
        event.registerEntityRenderer(ESEntities.AMETHYST_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/amethyst.png")));
        event.registerEntityRenderer(ESEntities.MINING_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/mining.png")));
        event.registerEntityRenderer(ESEntities.HEALING_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/healing.png")));
        event.registerEntityRenderer(ESEntities.END_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/end.png")));
        event.registerEntityRenderer(ESEntities.TELEPORT_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/teleport.png")));
        event.registerEntityRenderer(ESEntities.DRAGON_ARROW.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/arrow/dragon.png")));

        event.registerEntityRenderer(ESEntities.URANIUM_ROD.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/uranium_rod.png")));

        event.registerEntityRenderer(ESEntities.HANDGUN_BULLET.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/bullet/handgun.png")));
        event.registerEntityRenderer(ESEntities.HEAVY_HANDGUN_BULLET.get(), c -> new ESArrowRenderer(c,
                modid("textures/entity/bullet/heavy_handgun.png")));
        /**
         * TODO figure out why the item disappears after an instant
         *
         * TODO? copy egg display
         */
        event.registerEntityRenderer(ESEntities.ITEM_BULLET.get(), c -> new ThrownItemRenderer<ItemBullet>(c));
        event.registerEntityRenderer(ESEntities.THROWN_BEE_LARVA.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ESEntities.THROWN_BEENADE.get(), ThrownItemRenderer::new);

        event.registerBlockEntityRenderer(ESBlockEntities.CHARGER.get(), ChargerBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayers(final RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CaptainJusticeShield.CJSModel.LAYER_LOCATION,
                CaptainJusticeShield.CJSModel::createLayer);
    }

    @SubscribeEvent
    public static void registerExtensions(final RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(@Nonnull LivingEntity livingEntity,
                    @Nonnull ItemStack itemStack, @Nonnull EquipmentSlot equipmentSlot,
                    @Nonnull HumanoidModel<?> original) {
                return new HumanoidModel<>(HeavyBootsModel.createBodyLayer().bakeRoot());
            }
        }, ESItems.HEAVY_BOOTS);
    }

    @SubscribeEvent
    public static void registerBlockColorHandlers(final RegisterColorHandlersEvent.Block event) {
        // Tints the cruxite dowel on top with its color
        event.register((state, tintGetter, pos, tintIndex) -> {
            if (tintGetter != null && pos != null && tintIndex == 0) {
                Optional<PrinterBlockEntity> oprinter = tintGetter.getBlockEntity(pos,
                        ESBlockEntities.PRINTER.get());
                if (oprinter.isPresent()) {
                    PrinterBlockEntity printer = oprinter.get();
                    return printer.getColor();
                }
            }

            return 0xFFFFFFFF;
        }, ESBlocks.PRINTER.get());
    }

    @SubscribeEvent
    public static void registerItemColorHandlers(final RegisterColorHandlersEvent.Item event) {
        event.register((stack, index) -> {
            if (stack.has(ESDataComponents.GRIST_FOUND) && index == 1) {
                GristType type = stack.get(ESDataComponents.GRIST_FOUND);
                return type.getUnderlingColor() | 0xFF000000;
            }
            return -1;
        }, ESItems.GRIST_DETECTOR.get());
    }

    @SubscribeEvent
    public static void registerParticleProviders(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ESParticleTypes.URANIUM_BLAST.get(), UraniumBlastParticle.Provider::new);
    }
}
