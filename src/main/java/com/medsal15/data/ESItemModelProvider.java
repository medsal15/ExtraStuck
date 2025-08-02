package com.medsal15.data;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
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
        registerMeleeWeapons();
        registerRangedWeapons();
        registerAmmo();
        registerBlocks();

        basicItem(ESItems.LUCK_TOKEN.get());
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
        String id = shield.getId().toString();
        ItemModelBuilder blocking = withExistingParent(id + "_blocking", modLoc(base + "_blocking"))
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
        String path = shield.getId().getPath().toString();
        modelShield(shield, "item/" + path);
    }

    private void modelThinShield(DeferredItem<Item> shield) {
        String path = shield.getId().getPath().toString();
        modelShield(shield, "item/" + path, "thin_shield");
    }

    private void modelLargeShield(DeferredItem<Item> shield) {
        String path = shield.getId().getPath().toString();
        modelShield(shield, "item/" + path, "large_shield");
    }

    private void modelRoundShield(DeferredItem<Item> shield) {
        String path = shield.getId().getPath().toString();
        modelShield(shield, "item/" + path, "round_shield");
    }

    private void registerAmmo() {
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

        basicItem(ESItems.HANDGUN_BULLET.get());
        basicItem(ESItems.HEAVY_HANDGUN_BULLET.get());
    }

    private void modelDie(DeferredItem<Item> die, String path) {
        String id = die.getId().toString();
        if (!path.endsWith("/"))
            path += "/";
        withExistingParent(id, modLoc("base_die"))
                .texture("1", path + "one")
                .texture("2", path + "two")
                .texture("3", path + "three")
                .texture("4", path + "four")
                .texture("5", path + "five")
                .texture("6", path + "six");
    }

    private void knifeWeapon(DeferredItem<Item> knife) {
        String path = knife.getId().getPath().toString();
        withExistingParent(path, ExtraStuck.modid("item/knife_weapon"))
                .texture("layer0", ExtraStuck.modid("item/" + path));
    }

    private void registerMeleeWeapons() {
        handheldItem(ESItems.GEM_BREAKER.get());

        handheldItem(ESItems.GOLD_COIN.get());
        modelDie(ESItems.D_ICE, "item/dice/ice");
        handheldItem(ESItems.TOKEN_TETRAHEDRON.get());
        knifeWeapon(ESItems.SLICE_AND_DICE);
        handheldItem(ESItems.DONE.get());
        handheldItem(ESItems.D10.get());
        handheldItem(ESItems.RAINBOW_D7.get());
        handheldItem(ESItems.D8_NIGHT.get());
        handheldItem(ESItems.CAN_DIE.get());
        handheldItem(ESItems.INFINI_DIE.get());

        handheldItem(ESItems.SILVER_BAT.get());
        handheldItem(ESItems.GOLDEN_PAN.get());

        handheldItem(ESItems.KEY_OF_TRIALS.get());
        handheldItem(ESItems.KEY_OF_OMINOUS_TRIALS.get());
        handheldItem(ESItems.OFFICE_KEY.get());
    }

    private void registerRangedWeapons() {
        handheldItem(ESItems.HANDGUN.get());
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

        withExistingParent(ESItems.COBALT_BLOCK.getId().toString(), modLoc("block/cobalt_block"));
        basicItem(ESItems.COBALT_DOOR.get());
        trapdoorBottom(ESItems.COBALT_TRAPDOOR.getId().toString(), modLoc("block/cobalt_trapdoor"));
        pressurePlate(ESItems.COBALT_PRESSURE_PLATE.getId().toString(), modLoc("block/cobalt_block"));

        withExistingParent(ESItems.SULFUROUS_STONE.getId().toString(), modLoc("block/sulfurous_stone"));
        withExistingParent(ESItems.SULFUROUS_STONE_STAIRS.getId().toString(), modLoc("block/sulfurous_stone_stairs"));
        withExistingParent(ESItems.SULFUROUS_STONE_SLAB.getId().toString(), modLoc("block/sulfurous_stone_slab"));
        wallInventory(ESItems.SULFUROUS_STONE_WALL.getId().toString(), modLoc("block/sulfurous_stone"));

        withExistingParent(ESItems.MARBLE.getId().toString(), modLoc("block/marble"));
        withExistingParent(ESItems.MARBLE_STAIRS.getId().toString(), modLoc("block/marble_stairs"));
        withExistingParent(ESItems.MARBLE_SLAB.getId().toString(), modLoc("block/marble_slab"));
        wallInventory(ESItems.MARBLE_WALL.getId().toString(), modLoc("block/marble"));
        withExistingParent(ESItems.POLISHED_MARBLE.getId().toString(), modLoc("block/polished_marble"));
        withExistingParent(ESItems.POLISHED_MARBLE_STAIRS.getId().toString(), modLoc("block/polished_marble_stairs"));
        withExistingParent(ESItems.POLISHED_MARBLE_SLAB.getId().toString(), modLoc("block/polished_marble_slab"));
        wallInventory(ESItems.POLISHED_MARBLE_WALL.getId().toString(), modLoc("block/polished_marble"));
        withExistingParent(ESItems.MARBLE_BRICKS.getId().toString(), modLoc("block/marble_bricks"));
        withExistingParent(ESItems.MARBLE_BRICK_STAIRS.getId().toString(), modLoc("block/marble_brick_stairs"));
        withExistingParent(ESItems.MARBLE_BRICK_SLAB.getId().toString(), modLoc("block/marble_brick_slab"));
        wallInventory(ESItems.MARBLE_BRICK_WALL.getId().toString(), modLoc("block/marble_bricks"));

        withExistingParent(ESItems.ZILLIUM_BRICKS.getId().toString(), modLoc("block/zillium_bricks"));
        withExistingParent(ESItems.ZILLIUM_BRICK_STAIRS.getId().toString(), modLoc("block/zillium_brick_stairs"));
        withExistingParent(ESItems.ZILLIUM_BRICK_SLAB.getId().toString(), modLoc("block/zillium_brick_slab"));
        wallInventory(ESItems.ZILLIUM_BRICK_WALL.getId().toString(), modLoc("block/zillium_bricks"));
    }
}
