package dev.kyanbirb.world_casting.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static dev.kyanbirb.world_casting.WorldCasting.path;

public class WorldCastingBlockTags {
    public static final TagKey<Block> DISALLOW_FRACTURE = create("disallow_fracture");

    private static TagKey<Block> create(String id) {
        return TagKey.create(Registries.BLOCK, path(id));
    }
}
