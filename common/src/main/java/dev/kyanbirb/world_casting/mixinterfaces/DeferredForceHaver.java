package dev.kyanbirb.world_casting.mixinterfaces;

import dev.ryanhcode.sable.api.physics.force.ForceTotal;

public interface DeferredForceHaver {
    ForceTotal world_casting$getForceTotal();
    void world_casting$setForceTotal(ForceTotal forceTotal);
    default boolean world_casting$hasForceTotal() {
        return this.world_casting$getForceTotal() != null;
    }
}
