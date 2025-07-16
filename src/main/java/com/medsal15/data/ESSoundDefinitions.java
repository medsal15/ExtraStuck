package com.medsal15.data;

import com.medsal15.ESSounds;
import com.medsal15.ExtraStuck;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ESSoundDefinitions extends SoundDefinitionsProvider {
    public ESSoundDefinitions(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, ExtraStuck.MODID, fileHelper);
    }

    @Override
    public void registerSounds() {
        add(ESSounds.GOLDEN_PAN_HIT, SoundDefinition.definition().with(
                sound(ExtraStuck.modid("golden_pan_hit")).volume(0.7)));
    }
}
