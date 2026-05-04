package dev.kyanbirb.world_casting.mixin.sable_compat;

import at.petrak.hexcasting.common.casting.actions.raycast.OpEntityRaycast;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(OpEntityRaycast.class)
public class OpEntityRaycastMixin {

    @WrapOperation(method = "getEntityHitResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;clip(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;)Ljava/util/Optional;"))
    private Optional<Vec3> world_casting$clip(AABB instance, Vec3 start, Vec3 end, Operation<Optional<Vec3>> original, @Local(name = "nextEntity") Entity nextEntity) {
        SubLevel subLevel = Sable.HELPER.getContaining(nextEntity);
        if(subLevel != null) {
            start = subLevel.logicalPose().transformPositionInverse(start);
            end = subLevel.logicalPose().transformPositionInverse(end);
        }

        return original.call(instance, start, end);
    }

    @WrapOperation(method = "getEntityHitResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;distanceToSqr(Lnet/minecraft/world/phys/Vec3;)D"))
    private double world_casting$distanceToSqr(Vec3 instance, Vec3 vec, Operation<Double> original, @Local(argsOnly = true) Level level) {
        return Sable.HELPER.distanceSquaredWithSubLevels(level, instance, vec);
    }

}
