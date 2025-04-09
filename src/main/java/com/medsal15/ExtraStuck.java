package com.medsal15;

import java.text.NumberFormat;

import org.slf4j.Logger;

import com.medsal15.blocks.ESBlocks;
import com.medsal15.data.ESLangProvider;
import com.medsal15.entities.ESArrowRenderer;
import com.medsal15.entities.ESEntities;
import com.medsal15.entities.projectiles.CaptainJusticeShield;
import com.medsal15.items.ESDataComponents;
import com.medsal15.items.ESItems;
import com.medsal15.items.IESEnergyStorage;
import com.medsal15.items.shields.ESShield;
import com.medsal15.items.shields.ESShield.BlockFuncs;
import com.mojang.logging.LogUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ExtraStuck.MODID)
public class ExtraStuck {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "extrastuck";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS
            .register("extrastuck_tab", () -> CreativeModeTab.builder()
                    // The language key for the title of your CreativeModeTab
                    .title(Component.translatable("itemGroup.extrastuck"))
                    .icon(() -> ESItems.WOODEN_SHIELD.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        for (var item : ESItems.getItems()) {
                            output.accept(item.get());
                        }
                    }).build());

    // The constructor for the mod class is the first code that is run when your mod
    // is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and
    // pass them in automatically.
    public ExtraStuck(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.addListener(this::onShieldBlock);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerEntityRenderers);
        modEventBus.addListener(this::registerEntityLayers);

        // Register the Deferred Register to the mod event bus so items get registered
        ESDataComponents.DATA_COMPONENTS.register(modEventBus);
        ESBlocks.BLOCKS.register(modEventBus);
        ESItems.ITEMS.register(modEventBus);
        ESEntities.ENTITIES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config
        // file for us
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void registerEntityRenderers(RegisterRenderers event) {
        event.registerEntityRenderer(ESEntities.CAPTAIN_JUSTICE_SHIELD.get(),
                CaptainJusticeShield.CJSRenderer::new);
        event.registerEntityRenderer(ESEntities.FLAME_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/flame_arrow.png")));
        event.registerEntityRenderer(ESEntities.NETHER_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/nether_arrow.png")));
        event.registerEntityRenderer(ESEntities.CARDBOARD_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/cardboard_arrow.png")));
        event.registerEntityRenderer(ESEntities.MISSED_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/missed_arrow.png")));
        event.registerEntityRenderer(ESEntities.CANDY_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/candy_arrow.png")));
        event.registerEntityRenderer(ESEntities.LIGHTNING_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/lightning_arrow.png")));
        event.registerEntityRenderer(ESEntities.EXPLOSIVE_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/explosive_arrow.png")));
        event.registerEntityRenderer(ESEntities.IRON_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/iron_arrow.png")));
        event.registerEntityRenderer(ESEntities.QUARTZ_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/quartz_arrow.png")));
        event.registerEntityRenderer(ESEntities.PRISMARINE_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/prismarine_arrow.png")));
        event.registerEntityRenderer(ESEntities.GLASS_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/glass_arrow.png")));
        event.registerEntityRenderer(ESEntities.AMETHYST_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/amethyst_arrow.png")));
        event.registerEntityRenderer(ESEntities.MINING_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/mining_arrow.png")));
        event.registerEntityRenderer(ESEntities.HEALING_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/healing_arrow.png")));
        event.registerEntityRenderer(ESEntities.END_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/end_arrow.png")));
        event.registerEntityRenderer(ESEntities.TELEPORT_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/teleport_arrow.png")));
        event.registerEntityRenderer(ESEntities.DRAGON_ARROW.get(), c -> new ESArrowRenderer(c,
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/dragon_arrow.png")));
    }

    @SubscribeEvent
    public void registerEntityLayers(RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CaptainJusticeShield.CJSModel.LAYER_LOCATION,
                CaptainJusticeShield.CJSModel::createLayer);
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.EnergyStorage.ITEM,
                (stack, u) -> new IESEnergyStorage.StackEnergyStorage(stack),
                ESItems.FLUX_SHIELD.get());
    }

    @SubscribeEvent
    public void onShieldBlock(LivingShieldBlockEvent event) {
        // Ensure we're using a thorn shield
        var item = event.getEntity().getUseItem().getItem();
        if (item instanceof ESShield shield) {
            shield.onShieldBlock(event);
            return;
        }
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            for (var shield : ESItems.getShields()) {
                addBlocking(shield);
            }
        }

        private static void addBlocking(DeferredItem<Item> item) {
            ItemProperties.register(item.get(), ResourceLocation.withDefaultNamespace("blocking"),
                    (stack, world, entity, integer) -> entity != null && entity.isUsingItem()
                            && entity.getUseItem() == stack ? 1.0F : 0.0F);
        }
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ClientGameEvents {
        @SubscribeEvent
        public static void addCustomTooltip(ItemTooltipEvent event) {
            int i = 1;
            ItemStack stack = event.getItemStack();
            var item = stack.getItem();

            // Fancy item descriptions
            final ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (itemId != null && itemId.getNamespace().equals(ExtraStuck.MODID)) {
                String name = stack.getDescriptionId() + ".tooltip";
                if (I18n.exists(name)) {
                    event.getToolTip().add(i,
                            Component.translatable(name).withStyle(ChatFormatting.GRAY));
                    i++;
                }
            }

            // Shield info
            if (ClientConfig.displayShieldInfo) {
                if (item instanceof ESShield shield && shield.hasOnBlock(BlockFuncs.DAMAGE)) {
                    event.getToolTip().add(i,
                            Component.translatable(ESLangProvider.SHIELD_DAMAGE_KEY,
                                    stack.get(ESDataComponents.SHIELD_DAMAGE)
                                            .intValue())
                                    .withStyle(ChatFormatting.GRAY));
                    i++;
                }
            }

            // Shield RF
            if (item instanceof ESShield shield && shield.hasOnBlock(BlockFuncs.USE_POWER)) {
                event.getToolTip().add(i, Component.translatable(ESLangProvider.ENERGY_STORAGE_KEY,
                        NumberFormat.getInstance().format(shield.getEnergyStored(stack)),
                        NumberFormat.getInstance().format(shield.getMaxEnergyStored(stack))));
                i++;
            }
        }
    }
}
