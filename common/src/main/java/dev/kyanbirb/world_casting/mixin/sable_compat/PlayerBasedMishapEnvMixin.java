package dev.kyanbirb.world_casting.mixin.sable_compat;

import at.petrak.hexcasting.api.casting.eval.MishapEnvironment;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedMishapEnv;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.ryanhcode.sable.Sable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerBasedMishapEnv.class)
public abstract class PlayerBasedMishapEnvMixin extends MishapEnvironment {
    protected PlayerBasedMishapEnvMixin(ServerLevel world, @Nullable ServerPlayer caster) {
        super(world, caster);
    }

    @WrapOperation(method = "yeetHeldItemsTowards", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;subtract(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 world_casting$yeetHeldItemsTowards(Vec3 instance, Vec3 vec, Operation<Vec3> original) {
        return original.call(Sable.HELPER.projectOutOfSubLevel(this.world, instance), vec);
    }
}
