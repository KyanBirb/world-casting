package dev.kyanbirb.world_casting.mixin.sable_compat.ambit;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.ryanhcode.sable.Sable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({PlayerBasedCastEnv.class, CircleCastEnv.class})
public abstract class CastingEnvironmentImplMixin extends CastingEnvironment {

    protected CastingEnvironmentImplMixin(ServerLevel world) {
        super(world);
    }

    @WrapOperation(method = "isVecInRangeEnvironment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;distanceToSqr(Lnet/minecraft/world/phys/Vec3;)D"))
    private double world_casting$position(Vec3 instance, Vec3 vec, Operation<Double> original) {
        return Sable.HELPER.distanceSquaredWithSubLevels(this.world, instance, vec);
    }
}
