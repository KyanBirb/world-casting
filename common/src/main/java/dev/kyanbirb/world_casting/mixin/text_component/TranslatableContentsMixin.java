package dev.kyanbirb.world_casting.mixin.text_component;

import com.google.common.collect.ImmutableList;
import dev.kyanbirb.world_casting.content.iota.fragment.FragmentIota;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@Mixin(TranslatableContents.class)
public class TranslatableContentsMixin {

    @Shadow
    @Final
    private String key;

    @Shadow
    private List<FormattedText> decomposedParts;

    @Shadow
    @Final
    private Object[] args;

    @Inject(method = "decompose", at = @At("HEAD"), cancellable = true)
    private void world_casting$decompose(CallbackInfo ci) {
        if(this.key.equals("world_casting.display.fragment")) {
            if(this.args.length > 0 && this.args[0] instanceof String id) {
                UUID uuid = UUID.fromString(id);
                this.decomposedParts = ImmutableList.of(FragmentIota.getFragmentComponentClient(uuid));
                ci.cancel();
            }
        }
    }

}
