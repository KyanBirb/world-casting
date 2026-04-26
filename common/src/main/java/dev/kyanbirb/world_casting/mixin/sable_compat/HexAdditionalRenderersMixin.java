package dev.kyanbirb.world_casting.mixin.sable_compat;

import at.petrak.hexcasting.api.player.Sentinel;
import at.petrak.hexcasting.client.render.HexAdditionalRenderers;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HexAdditionalRenderers.class)
public class HexAdditionalRenderersMixin {

    @Unique
    private static final Quaternionf world_casting$ORIENTATION = new Quaternionf();

    @WrapOperation(method = "renderSentinel", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/player/Sentinel;position()Lnet/minecraft/world/phys/Vec3;"))
    private static Vec3 world_casting$position(Sentinel instance, Operation<Vec3> original, @Local(argsOnly = true) float partialTicks) {
        Vec3 pos = original.call(instance);
        ClientSubLevel clientSubLevel = Sable.HELPER.getContainingClient(pos);
        if(clientSubLevel != null) {
            Pose3dc pose = clientSubLevel.renderPose();
            pos = pose.transformPosition(pos);
        }

        return pos;
    }

    @WrapOperation(method = "renderSentinel", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V"))
    private static void world_casting$translate(PoseStack instance, double pX, double pY, double pZ, Operation<Void> original, @Local(argsOnly = true) Sentinel sentinel, @Local(argsOnly = true) float partialTicks) {
        original.call(instance, pX, pY, pZ);
        ClientSubLevel clientSubLevel = Sable.HELPER.getContainingClient(sentinel.position());
        if(clientSubLevel != null) {
            Pose3dc pose = clientSubLevel.renderPose();
            world_casting$ORIENTATION.set(pose.orientation());
            instance.mulPose(world_casting$ORIENTATION);
        }
    }

}
