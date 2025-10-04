package com.medsal15.subevents;

import static com.medsal15.ExtraStuck.modid;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.blockentities.ESBlockEntities;
import com.medsal15.blockentities.PrinterBlockEntity;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.client.model.armor.HeavyBootsModel;
import com.medsal15.client.programs.MastermindAppScreen;
import com.medsal15.client.renderers.ChargerBlockRenderer;
import com.medsal15.client.renderers.ESArrowRenderer;
import com.medsal15.computer.ESProgramTypes;
import com.medsal15.entities.ESEntities;
import com.medsal15.entities.projectiles.CaptainJusticeShield;
import com.medsal15.entities.projectiles.bullets.ItemBullet;
import com.medsal15.items.ESDataComponents;
import com.medsal15.items.ESItems;
import com.medsal15.items.crossbow.RadBowItem;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.client.gui.computer.ProgramGui;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredItem;

@EventBusSubscriber(modid = ExtraStuck.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
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

        ProgramGui.Registry.register(ESProgramTypes.MASTERMIND_CODEBREAKER, MastermindAppScreen::new);
    }

    private static void addBlocking(DeferredItem<Item> item) {
        ItemProperties.register(item.get(), ResourceLocation.withDefaultNamespace("blocking"),
                (stack, world, entity, entityid) -> entity != null && entity.isUsingItem()
                        && entity.getUseItem() == stack ? 1F : 0F);
    }

    private static void addCrossbow(DeferredItem<Item> item) {
        ItemProperties.register(item.get(), ResourceLocation.withDefaultNamespace("pulling"), (stack, world,
                entity, entityId) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack
                        && !RadBowItem.isCharged(stack) ? 1F : 0F);
        ItemProperties.register(item.get(), ResourceLocation.withDefaultNamespace("pull"),
                (stack, world, entity, entityId) -> {
                    if (entity == null || RadBowItem.isCharged(stack))
                        return 0F;
                    return (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks())
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
         */
        event.registerEntityRenderer(ESEntities.ITEM_BULLET.get(), c -> new ThrownItemRenderer<ItemBullet>(c));

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
}
