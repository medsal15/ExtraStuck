package com.medsal15;

import org.slf4j.Logger;

import com.medsal15.blockentities.ChargerBlockEntity;
import com.medsal15.blockentities.ESBlockEntities;
import com.medsal15.blockentities.PrinterBlockEntity;
import com.medsal15.blockentities.ReactorBlockEntity;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.computer.ESProgramTypes;
import com.medsal15.config.ConfigClient;
import com.medsal15.config.ConfigCommon;
import com.medsal15.entities.ESEntities;
import com.medsal15.interpreters.ESInterpretertypes;
import com.medsal15.items.ESArmorMaterials;
import com.medsal15.items.ESDataComponents;
import com.medsal15.items.ESEnergyStorage;
import com.medsal15.items.ESItems;
import com.medsal15.items.guns.ESGun;
import com.medsal15.items.guns.GunContainer;
import com.medsal15.items.shields.ESShield;
import com.medsal15.loot_modifiers.ESLootModifiers;
import com.medsal15.menus.ESMenuTypes;
import com.medsal15.mobeffects.ESMobEffects;
import com.medsal15.modus.ESModus;
import com.medsal15.structures.processors.ESProcessors;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
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
                    .displayItems(ESItems::addToCreativeTab).build());

    public static ResourceLocation modid(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    // The constructor for the mod class is the first code that is run when your mod
    // is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and
    // pass them in automatically.
    public ExtraStuck(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        // modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.addListener(this::onShieldBlock);
        NeoForge.EVENT_BUS.addListener(this::onDamage);
        NeoForge.EVENT_BUS.addListener(this::modifyFov);
        modEventBus.addListener(this::registerCapabilities);

        // Register the Deferred Register to the mod event bus so items get registered
        ESArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        ESBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ESBlocks.BLOCKS.register(modEventBus);
        ESDataComponents.DATA_COMPONENTS.register(modEventBus);
        ESEntities.ENTITIES.register(modEventBus);
        ESInterpretertypes.INTERPRETER_TYPES.register(modEventBus);
        ESItems.ITEMS.register(modEventBus);
        ESLootModifiers.GLM_SERIALIZERS.register(modEventBus);
        ESMenuTypes.MENU_TYPES.register(modEventBus);
        ESMobEffects.MOB_EFFECTS.register(modEventBus);
        ESModus.MODUSES.register(modEventBus);
        ESProcessors.PROCESSORS.register(modEventBus);
        ESProgramTypes.PROGRAM_TYPES.register(modEventBus);
        ESSounds.SOUND_EVENTS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config
        // file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, ConfigCommon.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ConfigClient.SPEC);
    }

    // private void commonSetup(final FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.EnergyStorage.ITEM,
                (stack, u) -> new ESEnergyStorage(stack),
                ESItems.FLUX_SHIELD.get(), ESItems.OVERCHARGED_MAGNEFORK.get(), ESItems.UNDERCHARGED_MAGNEFORK.get(),
                ESItems.FIELD_CHARGER.get());

        event.registerItem(Capabilities.ItemHandler.ITEM,
                (stack, u) -> new GunContainer(1, stack),
                ESItems.HANDGUN.get());

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ESBlockEntities.PRINTER.get(),
                PrinterBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ESBlockEntities.CHARGER.get(),
                ChargerBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ESBlockEntities.REACTOR.get(),
                ReactorBlockEntity::getItemHandler);

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ESBlockEntities.CHARGER.get(),
                ChargerBlockEntity::getEnergyHandler);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ESBlockEntities.REACTOR.get(),
                ReactorBlockEntity::getEnergyHandler);

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ESBlockEntities.REACTOR.get(),
                ReactorBlockEntity::getFluidHandler);
    }

    @SubscribeEvent
    public void onShieldBlock(LivingShieldBlockEvent event) {
        // Ensure we're using a thorn shield
        Item item = event.getEntity().getUseItem().getItem();
        if (item instanceof ESShield shield) {
            shield.onShieldBlock(event);
            return;
        }
    }

    @SubscribeEvent
    public void onDamage(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getHealth() > event.getAmount())
            return;

        InteractionHand hand;
        if (entity.getMainHandItem().is(ESItems.ANTI_DIE)) {
            hand = InteractionHand.MAIN_HAND;
        } else if (entity.getOffhandItem().is(ESItems.ANTI_DIE)) {
            hand = InteractionHand.OFF_HAND;
        } else {
            // Not holding anti die
            return;
        }

        // Prevent death and consume anti die
        entity.level().playSeededSound(null, (int) entity.getX(), (int) entity.getY(), (int) entity.getZ(),
                SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1F, 1F, entity.level().random.nextLong());
        Minecraft minecraft = Minecraft.getInstance();
        GameRenderer renderer = minecraft.gameRenderer;
        renderer.displayItemActivation(ESItems.ANTI_DIE.toStack());
        event.setCanceled(true);
        entity.setItemInHand(hand, ItemStack.EMPTY);
    }

    @SubscribeEvent
    public void modifyFov(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();

        ItemStack stack = player.getUseItem();
        float zoom = 1F;

        if (!stack.isEmpty() && stack.getItem() instanceof ESGun gun) {
            zoom = gun.getZoom();
        }
        if (zoom != 1f) {
            event.setNewFovModifier(zoom);
        }
    }
}
