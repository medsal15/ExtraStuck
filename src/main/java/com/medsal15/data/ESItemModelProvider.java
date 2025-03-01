package com.medsal15.data;

import com.medsal15.ESItems;
import com.medsal15.ExtraStuck;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ESItemModelProvider extends ItemModelProvider {
    public ESItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExtraStuck.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        modelShield(ESItems.WOODEN_SHIELD);
        modelShield(ESItems.THORN_SHIELD);
        modelShield(ESItems.WITHERED_SHIELD);
        modelThinShield(ESItems.FLAME_SHIELD);
        modelShield(ESItems.GLASS_SHIELD);
        modelShield(ESItems.REINFORCED_GLASS_SHIELD);
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
}
