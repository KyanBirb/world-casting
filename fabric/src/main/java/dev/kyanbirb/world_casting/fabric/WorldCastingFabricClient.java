package dev.kyanbirb.world_casting.fabric;

import dev.kyanbirb.world_casting.WorldCasting;
import dev.kyanbirb.world_casting.WorldCastingClient;
import dev.kyanbirb.world_casting.config.ClientConfig;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public class WorldCastingFabricClient {
    public void init() {
        WorldCastingClient.init();
        NeoForgeConfigRegistry.INSTANCE.register(WorldCasting.MOD_ID, ModConfig.Type.CLIENT, ClientConfig.SPEC);
        ConfigScreenFactoryRegistry.INSTANCE.register(WorldCasting.MOD_ID, (title, parent) -> new ConfigurationScreen(WorldCasting.MOD_ID, parent));
    }
}
