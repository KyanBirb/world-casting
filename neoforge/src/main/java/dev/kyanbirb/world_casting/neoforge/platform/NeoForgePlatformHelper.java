package dev.kyanbirb.world_casting.neoforge.platform;

import dev.kyanbirb.world_casting.PlatformHelper;
import dev.kyanbirb.world_casting.WorldCasting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class NeoForgePlatformHelper implements PlatformHelper {
    public static final Map<ResourceKey<Registry<?>>, DeferredRegister<?>> REGISTERS = new HashMap<>();

    public static void init(IEventBus eventBus) {
        for (DeferredRegister<?> register : REGISTERS.values()) {
            register.register(eventBus);
        }
    }

    public boolean modLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public <T extends BlockEntity> BlockEntityType.Builder<T> createBuilder(BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks) {
        return BlockEntityType.Builder.of(factory::apply, validBlocks);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Holder<T> register(Registry<T> registry, String id, Supplier<T> supplier) {
        ResourceKey<Registry<?>> key = (ResourceKey<Registry<?>>) (Object) registry.key();
        if(!REGISTERS.containsKey(key)) {
            REGISTERS.put(key, DeferredRegister.create(registry, WorldCasting.MOD_ID));
        }
        DeferredRegister<T> deferredRegister = (DeferredRegister<T>) REGISTERS.get(key);
        return deferredRegister.register(id, supplier);
    }

}
