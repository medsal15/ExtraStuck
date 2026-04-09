package com.medsal15;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.world.lands.GristLayerInfo;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ESAttachements {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
            .create(NeoForgeRegistries.ATTACHMENT_TYPES, ExtraStuck.MODID);

    public static final Supplier<AttachmentType<ESGristLayerInfo>> GRIST_LAYER = ATTACHMENT_TYPES.register(
            "grist_layer", () -> AttachmentType.builder(() -> ESGristLayerInfo.EMPTY).build());

    public static record ESGristLayerInfo(@Nullable GristType any, @Nullable GristType common,
            @Nullable GristType uncommon) {
        public static ESGristLayerInfo EMPTY = new ESGristLayerInfo(null, null, null);

        public boolean isEmpty() {
            return any == null || common == null || uncommon == null;
        }

        public static ESGristLayerInfo fromGristLayerInfo(GristLayerInfo info, int x, int z) {
            return new ESGristLayerInfo(
                    info.getAnyGristLayer().getTypeAt(x, z),
                    info.getCommonGristLayer().getTypeAt(x, z),
                    info.getUncommonGristLayer().getTypeAt(x, z));
        }

        @Override
        public final boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ESGristLayerInfo other))
                return false;

            return other.any == this.any && other.common == this.common && other.uncommon == this.uncommon;
        }
    }
}
