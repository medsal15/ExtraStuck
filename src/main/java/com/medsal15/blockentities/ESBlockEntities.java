package com.medsal15.blockentities;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;
import com.medsal15.blocks.ESBlocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister
            .create(Registries.BLOCK_ENTITY_TYPE, ExtraStuck.MODID);

    @SuppressWarnings("null")
    public static final Supplier<BlockEntityType<CardOreBlockEntity>> CARD_ORE = BLOCK_ENTITY_TYPES
            .register("card_ore",
                    () -> BlockEntityType.Builder
                            .<CardOreBlockEntity>of(CardOreBlockEntity::new, ESBlocks.CARD_ORE.get())
                            .build(null));
    @SuppressWarnings("null")
    public static final Supplier<BlockEntityType<PrinterBlockEntity>> PRINTER = BLOCK_ENTITY_TYPES.register("printer",
            () -> BlockEntityType.Builder.<PrinterBlockEntity>of(PrinterBlockEntity::new, ESBlocks.PRINTER.get())
                    .build(null));
    @SuppressWarnings("null")
    public static final Supplier<BlockEntityType<ChargerBlockEntity>> CHARGER = BLOCK_ENTITY_TYPES.register("charger",
            () -> BlockEntityType.Builder.<ChargerBlockEntity>of(ChargerBlockEntity::new, ESBlocks.CHARGER.get())
                    .build(null));
    @SuppressWarnings("null")
    public static final Supplier<BlockEntityType<ReactorBlockEntity>> REACTOR = BLOCK_ENTITY_TYPES.register("reactor",
            () -> BlockEntityType.Builder.<ReactorBlockEntity>of(ReactorBlockEntity::new, ESBlocks.REACTOR.get())
                    .build(null));
    @SuppressWarnings("null")
    public static final Supplier<BlockEntityType<BlasterBlockEntity>> BLASTER = BLOCK_ENTITY_TYPES
            .register("uranium_blaster",
                    () -> BlockEntityType.Builder
                            .<BlasterBlockEntity>of(BlasterBlockEntity::new, ESBlocks.URANIUM_BLASTER.get())
                            .build(null));
}
