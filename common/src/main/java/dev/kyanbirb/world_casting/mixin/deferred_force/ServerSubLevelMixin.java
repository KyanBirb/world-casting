package dev.kyanbirb.world_casting.mixin.deferred_force;

import dev.kyanbirb.world_casting.mixinterfaces.DeferredForceHaver;
import dev.ryanhcode.sable.api.physics.force.ForceTotal;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerSubLevel.class)
public abstract class ServerSubLevelMixin implements DeferredForceHaver {

    @Shadow
    public abstract ServerLevel getLevel();

    @Unique
    private @Nullable ForceTotal world_casting$forceTotal = null;
    @Unique
    private boolean world_casting$applyForceNextTick = false;

    @Inject(method = "tick", at = @At("TAIL"))
    private void world_casting$tick(CallbackInfo ci) {
        if(this.world_casting$forceTotal != null) {
            if(this.world_casting$applyForceNextTick) {
                ServerSubLevel self = (ServerSubLevel) (Object) this;
                SubLevelPhysicsSystem system = SubLevelPhysicsSystem.get(this.getLevel());
                RigidBodyHandle handle = system.getPhysicsHandle(self);
                handle.applyForcesAndReset(this.world_casting$forceTotal);
                this.world_casting$forceTotal = null;
                this.world_casting$applyForceNextTick = false;
            } else {
                this.world_casting$applyForceNextTick = true;
            }
        }
    }

    @Override
    public ForceTotal world_casting$getForceTotal() {
        return this.world_casting$forceTotal;
    }

    @Override
    public void world_casting$setForceTotal(ForceTotal forceTotal) {
        this.world_casting$forceTotal = forceTotal;
    }
}
