package dev.kyanbirb.world_casting.mixin.sable_compat;

import at.petrak.hexcasting.api.casting.circles.CircleExecutionState;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CircleCastEnv.class)
public abstract class CircleCastingEnvMixin extends CastingEnvironment {

    protected CircleCastingEnvMixin(ServerLevel world) {
        super(world);
    }

    @Shadow
    public abstract CircleExecutionState circleState();

    @WrapOperation(method = "isVecInRangeEnvironment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;contains(Lnet/minecraft/world/phys/Vec3;)Z"))
    private boolean world_casting$isVecInRangeEnvironment(AABB instance, Vec3 vec, Operation<Boolean> original) {
        CircleExecutionState state = this.circleState();
        ServerLevel level = this.world;
        SubLevel thisSubLevel = Sable.HELPER.getContaining(level, state.impetusPos);
        SubLevel otherSubLevel = Sable.HELPER.getContaining(level, vec);
        if(thisSubLevel != otherSubLevel) {
            if(thisSubLevel != null && otherSubLevel != null) {
                vec = otherSubLevel.logicalPose().transformPosition(vec);
                vec = thisSubLevel.logicalPose().transformPositionInverse(vec);
            } else {
                if(thisSubLevel != null) {
                    vec = thisSubLevel.logicalPose().transformPositionInverse(vec);
                } else {
                    vec = otherSubLevel.logicalPose().transformPosition(vec);
                }
            }
        }

        return original.call(instance, vec);
    }

}
