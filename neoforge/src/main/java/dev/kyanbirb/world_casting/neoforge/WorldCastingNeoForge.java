package dev.kyanbirb.world_casting.neoforge;


import dev.kyanbirb.world_casting.WorldCasting;
import dev.kyanbirb.world_casting.config.CommonConfig;
import dev.kyanbirb.world_casting.neoforge.data.WorldCastingLanguageProvider;
import dev.kyanbirb.world_casting.neoforge.platform.NeoForgePlatformHelper;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(WorldCasting.MOD_ID)
public class WorldCastingNeoForge {

    public WorldCastingNeoForge(IEventBus eventBus, ModContainer container) {
        WorldCasting.init();
        NeoForgePlatformHelper.init(eventBus);
        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        eventBus.addListener(this::gatherData);
    }

    private void gatherData(GatherDataEvent event) {
        if(event.includeClient()) {
            PackOutput output = event.getGenerator().getPackOutput();
            event.addProvider(new WorldCastingLanguageProvider(output, "en_us"));
        }
    }
}
