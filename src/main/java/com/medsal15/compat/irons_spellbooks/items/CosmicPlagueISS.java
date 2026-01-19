package com.medsal15.compat.irons_spellbooks.items;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESArmorMaterials;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.IDisableJacket;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class CosmicPlagueISS extends ImbuableChestplateArmorItem implements IDisableJacket {
    public CosmicPlagueISS(ArmorItem.Type type, Properties properties) {
        super(ESArmorMaterials.COSMIC_PLAGUE, type, properties,
                new AttributeContainer(AttributeRegistry.MAX_MANA, 175, Operation.ADD_VALUE),
                new AttributeContainer(AttributeRegistry.ENDER_SPELL_POWER, .15, Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.NATURE_SPELL_POWER, .15, Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.SPELL_POWER, .05, Operation.ADD_MULTIPLIED_BASE));
    }

    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GeoArmorRenderer<>(new Model());
    }

    public static class Model extends GeoModel<CosmicPlagueISS> {
        @Override
        public ResourceLocation getModelResource(CosmicPlagueISS animatable) {
            return ExtraStuck.modid("geo/cosmic_plague_armor.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(CosmicPlagueISS animatable) {
            return ExtraStuck.modid("textures/models/armor/cosmic_plague.png");
        }

        @Override
        public ResourceLocation getAnimationResource(CosmicPlagueISS animatable) {
            return ExtraStuck.modid("animations/none.animation.json");
        }
    }
}
