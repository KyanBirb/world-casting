package dev.kyanbirb.world_casting.neoforge;

import dev.kyanbirb.world_casting.WorldCasting;
import dev.kyanbirb.world_casting.WorldCastingClient;
import dev.kyanbirb.world_casting.config.ClientConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(dist = Dist.CLIENT, value = WorldCasting.MOD_ID)
public class WorldCastingNeoForgeClient {

    public WorldCastingNeoForgeClient(IEventBus eventBus, ModContainer container) {
        WorldCastingClient.init();
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

}
