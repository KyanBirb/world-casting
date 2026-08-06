package dev.kyanbirb.world_casting.mixin.sable_compat;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.ryanhcode.sable.Sable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "at.petrak.hexcasting.common.casting.actions.spells.great.OpTeleport$Spell")
public class OpTeleportMixin {

    @Unique
    private Entity world_casting$entity;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void world_casting$init(Entity teleportee, Vec3 delta, CallbackInfo ci) {
        this.world_casting$entity = teleportee;
    }

    @WrapOperation(method = "cast(Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;length()D"))
    private double world_casting$length(Vec3 instance, Operation<Double> original, @Local(argsOnly = true) CastingEnvironment env) {
        double distSqr = Sable.HELPER.distanceSquaredWithSubLevels(this.world_casting$entity.level(), this.world_casting$entity.position(), this.world_casting$entity.position().add(instance));
        return Math.sqrt(distSqr);
    }
}
