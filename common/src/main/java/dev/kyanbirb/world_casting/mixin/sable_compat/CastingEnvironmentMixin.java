package dev.kyanbirb.world_casting.mixin.sable_compat;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.ryanhcode.sable.Sable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CastingEnvironment.class)
public class CastingEnvironmentMixin {

    @Shadow
    @Final
    protected ServerLevel world;

    @WrapMethod(method = "isVecInRange")
    private boolean world_casting$isVecInRange(Vec3 vec, Operation<Boolean> original) {
        return original.call(Sable.HELPER.projectOutOfSubLevel(world, vec));
    }
}
