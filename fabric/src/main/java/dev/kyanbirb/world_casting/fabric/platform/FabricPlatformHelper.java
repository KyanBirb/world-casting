package dev.kyanbirb.world_casting.fabric.platform;

import dev.kyanbirb.world_casting.PlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static dev.kyanbirb.world_casting.WorldCasting.path;

public class FabricPlatformHelper implements PlatformHelper {

    @Override
    public <T extends BlockEntity> BlockEntityType.Builder<T> createBuilder(BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks) {
        return BlockEntityType.Builder.of(factory::apply, validBlocks);
    }

    @Override
    public boolean modLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public <T> Holder<T> register(Registry<T> registry, String id, Supplier<T> supplier) {
        return Holder.direct(Registry.register(registry, path(id), supplier.get()));
    }

    @Override
    public ModelResourceLocation modelResourceLocation(ResourceLocation resourceLocation) {
        return new ModelResourceLocation(resourceLocation, "standalone");
    }

}
