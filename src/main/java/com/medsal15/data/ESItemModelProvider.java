package com.medsal15.data;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public final class ESItemModelProvider extends ItemModelProvider {
    public ESItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExtraStuck.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerShields();
        registerArrows();
        registerBlocks();
    }

    private void registerShields() {
        modelShield(ESItems.WOODEN_SHIELD);
        modelThinShield(ESItems.FLAME_SHIELD);
        modelShield(ESItems.LIGHT_SHIELD);
        modelThinShield(ESItems.HALT_SHIELD);
        modelThinShield(ESItems.NON_CONTACT_CONTRACT);
        basicItem(ESItems.SLIED.getId());
        modelLargeShield(ESItems.RIOT_SHIELD);
        modelRoundShield(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD);
        basicItem(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
        modelShield(ESItems.CAPITASHIELD);
        modelShield(ESItems.IRON_SHIELD);
        modelShield(ESItems.GOLD_SHIELD);
        modelShield(ESItems.FLUX_SHIELD);
        modelShield(ESItems.DIAMOND_SHIELD);
        modelShield(ESItems.NETHERITE_SHIELD);
        modelShield(ESItems.GARNET_SHIELD);
        modelShield(ESItems.POGO_SHIELD);
        modelShield(ESItems.RETURN_TO_SENDER);
    }

    private void modelShield(DeferredItem<Item> shield, String texture, String base) {
        var id = shield.getId().toString();
        var blocking = withExistingParent(id + "_blocking", modLoc(base + "_blocking"))
                .texture("0", modLoc(texture))
                .texture("particle", modLoc(texture));
        withExistingParent(id, modLoc(base))
                .texture("0", modLoc(texture))
                .texture("particle", modLoc(texture))
                .override().predicate(ResourceLocation.withDefaultNamespace("blocking"), 1).model(blocking);
    }

    private void modelShield(DeferredItem<Item> shield, String texture) {
        modelShield(shield, texture, "base_shield");
    }

    private void modelShield(DeferredItem<Item> shield) {
        var path = shield.getId().getPath().toString();
        modelShield(shield, "item/" + path);
    }

    private void modelThinShield(DeferredItem<Item> shield) {
        var path = shield.getId().getPath().toString();
        modelShield(shield, "item/" + path, "thin_shield");
    }

    private void modelLargeShield(DeferredItem<Item> shield) {
        var path = shield.getId().getPath().toString();
        modelShield(shield, "item/" + path, "large_shield");
    }

    private void modelRoundShield(DeferredItem<Item> shield) {
        var path = shield.getId().getPath().toString();
        modelShield(shield, "item/" + path, "round_shield");
    }

    private void registerArrows() {
        basicItem(ESItems.FLAME_ARROW.get());
        basicItem(ESItems.NETHER_ARROW.get());
        basicItem(ESItems.CARDBOARD_ARROW.get());
        basicItem(ESItems.MISSED_YOU.get());
        basicItem(ESItems.SWEET_TOOTH.get());
        basicItem(ESItems.LIGHTNING_ARROW.get());
        basicItem(ESItems.EXPLOSIVE_ARROW.get());
        basicItem(ESItems.IRON_ARROW.get());
        basicItem(ESItems.QUARTZ_ARROW.get());
        basicItem(ESItems.PRISMARINE_ARROW.get());
        basicItem(ESItems.GLASS_ARROW.get());
        basicItem(ESItems.AMETHYST_ARROW.get());
        basicItem(ESItems.PROJECDRILL.get());
        basicItem(ESItems.CRUSADER_CROSSBOLT.get());
        basicItem(ESItems.END_ARROW.get());
        basicItem(ESItems.TELERROW.get());
        basicItem(ESItems.DRAGON_ARROW.get());
    }

    private void registerBlocks() {
        withExistingParent(ESItems.CUT_GARNET.getId().toString(), modLoc("block/cut_garnet"));
        withExistingParent(ESItems.CUT_GARNET_STAIRS.getId().toString(), modLoc("block/cut_garnet_stairs"));
        withExistingParent(ESItems.CUT_GARNET_SLAB.getId().toString(), modLoc("block/cut_garnet_slab"));
        wallInventory(ESItems.CUT_GARNET_WALL.getId().toString(), modLoc("block/cut_garnet"));
        withExistingParent(ESItems.GARNET_BRICKS.getId().toString(), modLoc("block/garnet_bricks"));
        withExistingParent(ESItems.GARNET_BRICK_STAIRS.getId().toString(), modLoc("block/garnet_brick_stairs"));
        withExistingParent(ESItems.GARNET_BRICK_SLAB.getId().toString(), modLoc("block/garnet_brick_slab"));
        wallInventory(ESItems.GARNET_BRICK_WALL.getId().toString(), modLoc("block/garnet_bricks"));
        withExistingParent(ESItems.CHISELED_GARNET_BRICKS.getId().toString(), modLoc("block/chiseled_garnet_bricks"));

        withExistingParent(ESItems.CUT_RUBY.getId().toString(), modLoc("block/cut_ruby"));
        withExistingParent(ESItems.CUT_RUBY_STAIRS.getId().toString(), modLoc("block/cut_ruby_stairs"));
        withExistingParent(ESItems.CUT_RUBY_SLAB.getId().toString(), modLoc("block/cut_ruby_slab"));
        wallInventory(ESItems.CUT_RUBY_WALL.getId().toString(), modLoc("block/cut_ruby"));
        withExistingParent(ESItems.RUBY_BRICKS.getId().toString(), modLoc("block/ruby_bricks"));
        withExistingParent(ESItems.RUBY_BRICK_STAIRS.getId().toString(), modLoc("block/ruby_brick_stairs"));
        withExistingParent(ESItems.RUBY_BRICK_SLAB.getId().toString(), modLoc("block/ruby_brick_slab"));
        wallInventory(ESItems.RUBY_BRICK_WALL.getId().toString(), modLoc("block/ruby_bricks"));
        withExistingParent(ESItems.CHISELED_RUBY_BRICKS.getId().toString(), modLoc("block/chiseled_ruby_bricks"));
    }
}
