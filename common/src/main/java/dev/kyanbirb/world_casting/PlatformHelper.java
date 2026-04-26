package dev.kyanbirb.world_casting;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ServiceLoader;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public interface PlatformHelper {
    PlatformHelper INSTANCE = ServiceLoader.load(PlatformHelper.class).findFirst().orElseThrow(() -> new RuntimeException("Unable to find %s implementation".formatted(PlatformHelper.class.getName())));

    static boolean isModLoaded(String modId) {
        return INSTANCE.modLoaded(modId);
    }

    static <T extends Iota> Holder<IotaType<T>> registerIotaType(String id, Supplier<IotaType<T>> supplier) {
        return (Holder<IotaType<T>>) (Object) INSTANCE.register(IXplatAbstractions.INSTANCE.getIotaTypeRegistry(), id, supplier::get);
    }

    static Holder<ActionRegistryEntry> registerAction(String id, Supplier<ActionRegistryEntry> supplier) {
        return INSTANCE.register(IXplatAbstractions.INSTANCE.getActionRegistry(), id, supplier);
    }

    static <T extends Item> Holder<T> registerItem(String id, Supplier<T> supplier) {
        return (Holder<T>) INSTANCE.register(BuiltInRegistries.ITEM, id, supplier::get);
    }

    static <T extends Block> Holder<T> registerBlock(String id, Supplier<T> supplier) {
        return (Holder<T>) INSTANCE.register(BuiltInRegistries.BLOCK, id, supplier::get);
    }

    static <T extends Block> Holder<T> registerBlockWithItem(String id, Supplier<T> supplier) {
        Holder<T> holder = registerBlock(id, supplier);
        registerItem(id, () -> new BlockItem(holder.value(), new Item.Properties()));
        return holder;
    }

    @SafeVarargs
    static <T extends BlockEntity> Holder<BlockEntityType<T>> registerBlockEntityType(String id, BiFunction<BlockPos, BlockState, T> factory, Holder<? extends Block>... validBlocks) {
        return (Holder<BlockEntityType<T>>) (Object) INSTANCE.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, () -> {
            Block[] blocks = new Block[validBlocks.length];
            for (int i = 0; i < validBlocks.length; i++) {
                blocks[i] = validBlocks[i].value();
            }
            return INSTANCE.createBuilder(factory, blocks).build(null);
        });
    }

    <T extends BlockEntity> BlockEntityType.Builder<T> createBuilder(BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks);

    boolean modLoaded(String modId);

    <T> Holder<T> register(Registry<T> registry, String id, Supplier<T> supplier);

}
