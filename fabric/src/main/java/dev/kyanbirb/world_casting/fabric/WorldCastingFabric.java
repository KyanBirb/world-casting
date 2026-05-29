package dev.kyanbirb.world_casting.fabric;

import dev.kyanbirb.world_casting.WorldCasting;
import dev.kyanbirb.world_casting.config.CommonConfig;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.neoforged.fml.config.ModConfig;

public class WorldCastingFabric {
    public void init() {
        WorldCasting.init();
        NeoForgeConfigRegistry.INSTANCE.register(WorldCasting.MOD_ID, ModConfig.Type.COMMON, CommonConfig.SPEC);
    }
}
