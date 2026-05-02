package dev.kyanbirb.world_casting.fabric;

import dev.kyanbirb.world_casting.WorldCasting;
import dev.kyanbirb.world_casting.config.CommonConfig;
import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import net.minecraftforge.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;

public class WorldCastingFabric {
    public void init() {
        WorldCasting.init();
        ForgeConfigRegistry.INSTANCE.register(WorldCasting.MOD_ID, ModConfig.Type.COMMON, (IConfigSpec<?>) CommonConfig.SPEC);
    }
}
