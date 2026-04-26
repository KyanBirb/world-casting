package dev.kyanbirb.world_casting.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue SENTINEL_FOLLOWS_SUB_LEVELS;
    public static final ModConfigSpec.IntValue FRAME_MAX_WIDTH;
    public static final ModConfigSpec.IntValue FRAME_MAX_HEIGHT;
    public static final ModConfigSpec.IntValue FRAME_MAX_LENGTH;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        SENTINEL_FOLLOWS_SUB_LEVELS = builder
                .comment("If sentinels should attempt to follow sub-levels through assembly, disassembly, splitting, etc.")
                .define("sentinel_follows_sub_level", true);

        FRAME_MAX_WIDTH = builder
                .comment("The max side length of a frame along the x axis.")
                .defineInRange("frame_max_width", 18, 1, Integer.MAX_VALUE);
        FRAME_MAX_HEIGHT = builder
                .comment("The max side length of a frame along the y axis.")
                .defineInRange("frame_max_width", 18, 1, Integer.MAX_VALUE);
        FRAME_MAX_LENGTH = builder
                .comment("The max side length of a frame along the z axis.")
                .defineInRange("frame_max_width", 18, 1, Integer.MAX_VALUE);


        SPEC = builder.build();
    }
}
