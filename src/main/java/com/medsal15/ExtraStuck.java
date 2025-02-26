package com.medsal15;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ExtraStuck.MODID)
public class ExtraStuck {
        // Define mod id in a common place for everything to reference
        public static final String MODID = "extrastuck";
        // Directly reference a slf4j logger
        private static final Logger LOGGER = LogUtils.getLogger();
        // Create a Deferred Register to hold CreativeModeTabs which will all be
        // registered under the "extrastuck" namespace
        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
                        .create(Registries.CREATIVE_MODE_TAB, MODID);

        // Creates a creative tab with the id "extrastuck:example_tab" for the example
        // item, that is placed after the combat tab
        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS
                        .register("extrastuck_tab", () -> CreativeModeTab.builder()
                                        // The language key for the title of your CreativeModeTab
                                        .title(Component.translatable("itemGroup.extrastuck"))
                                        .icon(() -> ESItems.WOODEN_SHIELD.get().getDefaultInstance())
                                        .displayItems((parameters, output) -> {
                                                output.accept(ESItems.WOODEN_SHIELD.get()); // Add the example item to
                                                                                            // the tab.
                                                // For your own tabs, this
                                                // method is preferred over the event
                                        }).build());

        // The constructor for the mod class is the first code that is run when your mod
        // is loaded.
        // FML will recognize some parameter types like IEventBus or ModContainer and
        // pass them in automatically.
        public ExtraStuck(IEventBus modEventBus, ModContainer modContainer) {
                // Register the commonSetup method for modloading
                modEventBus.addListener(this::commonSetup);

                // Register the Deferred Register to the mod event bus so items get registered
                ESItems.ITEMS.register(modEventBus);
                // Register the Deferred Register to the mod event bus so tabs get registered
                CREATIVE_MODE_TABS.register(modEventBus);

                // Register our mod's ModConfigSpec so that FML can create and load the config
                // file for us
                modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        }

        private void commonSetup(final FMLCommonSetupEvent event) {
                if (ServerConfig.warnNoMinestuck && !ModList.get().isLoaded("minestuck"))
                        LOGGER.info("Minestuck is not loaded!");
        }

        @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
        public static class ClientModEvents {
                @SubscribeEvent
                public static void onClientSetup(FMLClientSetupEvent event) {
                        ItemProperties.register(ESItems.WOODEN_SHIELD.get(),
                                        ResourceLocation.withDefaultNamespace("blocking"),
                                        (stack, world, entity, integer) -> entity != null && entity.isUsingItem()
                                                        && entity.getUseItem() == stack ? 1.0F : 0.0F);
                }
        }
}
