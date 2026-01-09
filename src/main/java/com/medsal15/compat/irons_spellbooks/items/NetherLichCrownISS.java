package com.medsal15.compat.irons_spellbooks.items;

import com.medsal15.ExtraStuck;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class NetherLichCrownISS extends ExtendedArmorItem implements IPresetSpellContainer {
    public NetherLichCrownISS() {
        super(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Properties()
                .durability(ArmorItem.Type.HELMET.getDurability(32)),
                new AttributeContainer(Attributes.ARMOR, -.25F, Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.MAX_MANA, 225F, Operation.ADD_VALUE),
                new AttributeContainer(AttributeRegistry.SPELL_POWER, .25F, Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.FIRE_MAGIC_RESIST, .15F, Operation.ADD_MULTIPLIED_BASE));
    }

    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GeoArmorRenderer<>(new Model());
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        if (!ISpellContainer.isSpellContainer(itemStack)) {
            var spellContainer = ISpellContainer.create(1, true, true);
            ISpellContainer.set(itemStack, spellContainer);
        }
    }

    public static class Model extends GeoModel<NetherLichCrownISS> {
        @Override
        public ResourceLocation getModelResource(NetherLichCrownISS animatable) {
            return ExtraStuck.modid("geo/lich_crown.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(NetherLichCrownISS animatable) {
            return ExtraStuck.modid("textures/models/armor/nether_lich_crown.png");
        }

        @Override
        public ResourceLocation getAnimationResource(NetherLichCrownISS animatable) {
            return ExtraStuck.modid("animations/none.animation.json");
        }
    }
}
