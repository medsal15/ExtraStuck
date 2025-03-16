package com.medsal15.data;

import com.medsal15.ESItems;
import com.medsal15.ExtraStuck;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ESLangProvider extends LanguageProvider {
    public ESLangProvider(PackOutput output) {
        super(output, ExtraStuck.MODID, "en_us");
    }

    public static final String SHIELD_DAMAGE_KEY = ExtraStuck.MODID + ".shield_damage";
    public static final String SHIELD_EFFECT_KEY = ExtraStuck.MODID + ".shield_effect";
    public static final String SHIELD_SELF_EFFECT_KEY = ExtraStuck.MODID + ".shield_self_effect";
    public static final String SLIED_DROP_KEY = ESItems.SLIED.get().getDescriptionId() + ".drop";

    @Override
    protected void addTranslations() {
        add("itemGroup.extrastuck", "ExtraStuck");

        add(SHIELD_DAMAGE_KEY, "Deals %s damage to melee attackers");
        add(SHIELD_EFFECT_KEY, "Applies %s (%s) to melee attackers");
        add(SHIELD_SELF_EFFECT_KEY, "Applies %s (%s) when attacked");

        add("patchouli.extrastuck.title", "ExtraStuck Guide");
        add("patchouli.extrastuck.landing", "Unofficial ExtraStuck Walkthrough (100%% official)");

        addItem(ESItems.WOODEN_SHIELD, "Wooden Shield");
        addItemTooltip(ESItems.WOODEN_SHIELD, "This cheap shield is 100% fire-ready");
        addItem(ESItems.FLAME_SHIELD, "Flame Shield");
        addItemTooltip(ESItems.FLAME_SHIELD, "Uh oh, it seems your wooden shield is burning!");
        addItem(ESItems.HALT_SHIELD, "Halt Shield");
        addItemTooltip(ESItems.HALT_SHIELD, "Forces your enemies to turn back");
        addItem(ESItems.NON_CONTACT_CONTRACT, "Non-Contact Contract");
        addItemTooltip(ESItems.NON_CONTACT_CONTRACT, "This piece of paper has the ability to prevent contact with you");
        addItem(ESItems.SLIED, "slied");
        addItemTooltip(ESItems.SLIED, "Can this even block damage?");
        add(SLIED_DROP_KEY, "Expectedly, it could not");
        addItem(ESItems.RIOT_SHIELD, "Riot Shield");
        addItem(ESItems.CAPITASHIELD, "Capitashield");
        addItemTooltip(ESItems.CAPITASHIELD, "Protects you, at a cost");
        addItem(ESItems.IRON_SHIELD, "Iron Shield");
        addItem(ESItems.GOLD_SHIELD, "Gold Shield");
        addItem(ESItems.DIAMOND_SHIELD, "Diamond Shield");
        addItem(ESItems.NETHERITE_SHIELD, "Netherite Shield");
        addItem(ESItems.GARNET_SHIELD, "Garnet Shield");
        addItem(ESItems.POGO_SHIELD, "Pogo Shield");
        addItemTooltip(ESItems.POGO_SHIELD, "The shield that bounces back!");
        addItem(ESItems.RETURN_TO_SENDER, "Return to Sender");
        addItemTooltip(ESItems.RETURN_TO_SENDER, "The shield that shoots back!");
    }

    protected void addItemTooltip(Supplier<? extends Item> key, String text) {
        add(((Item) (key.get())).getDescriptionId() + ".tooltip", text);
    }
}
