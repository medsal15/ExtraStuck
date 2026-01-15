package com.medsal15.compat.jei;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.jei.MinestuckJeiPlugin;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class ExtrastuckJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ExtraStuck.MODID, "extrastuck");
    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ESItems.PRINTER.toStack(), MinestuckJeiPlugin.GRIST_COST);
        registration.addRecipeCatalyst(ESItems.DISPRINTER.toStack(), MinestuckJeiPlugin.GRIST_COST);
    }
}
