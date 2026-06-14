package dev.kyanbirb.world_casting.mixin.vec_display;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kyanbirb.world_casting.config.ClientConfig;
import dev.kyanbirb.world_casting.content.iota.FragmentIota;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(GuiSpellcasting.class)
public class GuiSpellcastingMixin {

    @WrapOperation(method = "calculateIotaDisplays", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/iota/Iota;displayWithMaxWidth(ILnet/minecraft/client/gui/Font;)Lnet/minecraft/util/FormattedCharSequence;"))
    private FormattedCharSequence world_casting$displayWithMaxWidth(Iota instance, int maxWidth, Font font, Operation<FormattedCharSequence> original) {
        if(instance instanceof Vec3Iota vec3Iota && ClientConfig.CUSTOM_VECTOR_DISPLAY.getAsBoolean()) {
            Component display = instance.display();
            Vec3 pos = vec3Iota.getVec3();
            ClientSubLevel clientSubLevel = Sable.HELPER.getContainingClient(pos);
            if(clientSubLevel != null) {
                display = FragmentIota.getVec3Display(clientSubLevel, pos);
            }

            List<FormattedCharSequence> splitted = font.split(display, maxWidth - font.width("..."));
            if (splitted.isEmpty()) {
                return FormattedCharSequence.EMPTY;
            } else if (splitted.size() == 1) {
                return splitted.getFirst();
            } else {
                FormattedCharSequence first = splitted.getFirst();
                return FormattedCharSequence.fromPair(first, Component.literal("...").withStyle(ChatFormatting.GRAY).getVisualOrderText());
            }
        }
        return original.call(instance, maxWidth, font);
    }

}
