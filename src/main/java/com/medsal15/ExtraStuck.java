package com.medsal15;

import org.slf4j.Logger;

import com.medsal15.blockentities.ESBlockEntities;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.computer.ESProgramTypes;
import com.medsal15.conditions.ESConditions;
import com.medsal15.config.ConfigClient;
import com.medsal15.config.ConfigCommon;
import com.medsal15.entities.ESEntities;
import com.medsal15.interpreters.ESInterpretertypes;
import com.medsal15.items.ESArmorMaterials;
import com.medsal15.items.ESItems;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.loot_modifiers.ESLootModifiers;
import com.medsal15.menus.ESMenuTypes;
import com.medsal15.mobeffects.ESMobEffects;
import com.medsal15.modus.ESModus;
import com.medsal15.particles.ESParticleTypes;
import com.medsal15.structures.processors.ESProcessors;
import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
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

        // Register the Deferred Register to the mod event bus so items get registered
        ESArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        ESBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ESBlocks.BLOCKS.register(modEventBus);
        ESConditions.CONDITIONS.register(modEventBus);
        ESDataComponents.DATA_COMPONENTS.register(modEventBus);
        ESEntities.ENTITIES.register(modEventBus);
        ESInterpretertypes.INTERPRETER_TYPES.register(modEventBus);
        ESItems.ITEMS.register(modEventBus);
        ESLootModifiers.GLM_SERIALIZERS.register(modEventBus);
        ESMenuTypes.MENU_TYPES.register(modEventBus);
        ESMobEffects.MOB_EFFECTS.register(modEventBus);
        ESModus.MODUSES.register(modEventBus);
        ESParticleTypes.PARTICLE_TYPES.register(modEventBus);
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
}
