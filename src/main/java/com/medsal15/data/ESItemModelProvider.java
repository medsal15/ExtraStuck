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
}
