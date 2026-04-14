package com.medsal15.data;

import com.medsal15.ExtraStuck;
import com.mraof.minestuck.data.CassetteSongsProvider;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.JukeboxSong;

public class ESCassetteSongsProvider extends CassetteSongsProvider {
    public static final ResourceLocation DEAD_KING_LULLABY = ResourceLocation.fromNamespaceAndPath("irons_spellbooks",
            "dead_king_lullaby");
    public static final ResourceLocation FLAME_STILL_BURNS = ResourceLocation.fromNamespaceAndPath("irons_spellbooks",
            "flame_still_burns");
    public static final ResourceLocation WHISPERS_OF_ICE = ResourceLocation.fromNamespaceAndPath("irons_spellbooks",
            "whispers_of_ice");

    public ESCassetteSongsProvider(PackOutput output) {
        super(output, ExtraStuck.MODID);
    }

    @Override
    protected void registerSongs() {
        addHitEffect(fromLocation(DEAD_KING_LULLABY), new MobEffectInstance(MobEffectRegistry.BLIGHT, 100, 1),
                DEAD_KING_LULLABY);
        addSelfEffect(fromLocation(FLAME_STILL_BURNS), new MobEffectInstance(MobEffectRegistry.HASTENED, 100),
                FLAME_STILL_BURNS);
        addHitEffect(fromLocation(WHISPERS_OF_ICE), new MobEffectInstance(MobEffectRegistry.CHILLED, 100),
                WHISPERS_OF_ICE);
    }

    protected ResourceKey<JukeboxSong> fromLocation(ResourceLocation location) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, location);
    }
}
