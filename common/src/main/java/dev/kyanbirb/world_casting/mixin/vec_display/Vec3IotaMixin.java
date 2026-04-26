package dev.kyanbirb.world_casting.mixin.vec_display;

import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.kyanbirb.world_casting.config.ClientConfig;
import dev.kyanbirb.world_casting.content.iota.FragmentIota;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Vec3Iota.class)
public class Vec3IotaMixin {

    @WrapMethod(method = "display(DDD)Lnet/minecraft/network/chat/Component;")
    private static Component world_casting$display(double x, double y, double z, Operation<Component> original) {
        ClientSubLevel subLevel = ClientConfig.CUSTOM_VECTOR_DISPLAY.getAsBoolean() ? Sable.HELPER.getContainingClient(x, z) : null;
        if(subLevel != null) {
            BlockPos center = subLevel.getPlot().getCenterBlock();
            return Component.literal(String.format("(%.2f, %.2f, %.2f) in ", x - center.getX(), y - center.getY(), z - center.getZ()))
                    .withStyle(ChatFormatting.RED)
                    .append(FragmentIota.display(subLevel));
        }
        return original.call(x, y, z);
    }

}
