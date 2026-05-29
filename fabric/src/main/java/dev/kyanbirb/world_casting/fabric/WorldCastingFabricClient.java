package dev.kyanbirb.world_casting.fabric;

import dev.kyanbirb.world_casting.WorldCasting;
import dev.kyanbirb.world_casting.WorldCastingClient;
import dev.kyanbirb.world_casting.config.ClientConfig;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.neoforged.fml.config.ModConfig;

public class WorldCastingFabricClient {
    public void init() {
        WorldCastingClient.init();
        NeoForgeConfigRegistry.INSTANCE.register(WorldCasting.MOD_ID, ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }
}
