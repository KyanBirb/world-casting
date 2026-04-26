package dev.kyanbirb.world_casting.fabric;

import dev.kyanbirb.world_casting.WorldCastingClient;
import dev.kyanbirb.world_casting.event.WorldCastingClientEvents;
import dev.kyanbirb.world_casting.index.WorldCastingAdditionalModels;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class WorldCastingFabricClient {
    public void init() {
        WorldCastingClient.init();
        ModelLoadingPlugin.register((context -> context.addModels(WorldCastingAdditionalModels.ALL_MODELS.stream().map(ModelResourceLocation::id).toList())));
        WorldCastingClientEvents.registerBlockEntityRenderers(BlockEntityRenderers::register);
    }
}
