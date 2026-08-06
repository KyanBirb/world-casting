package dev.kyanbirb.world_casting.config;

import dev.kyanbirb.world_casting.WorldCasting;
import dev.kyanbirb.world_casting.data.WorldCastingLang;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Locale;

public class CommonConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue SENTINEL_FOLLOWS_SUB_LEVELS;
    public static final ModConfigSpec.DoubleValue MAX_MERGE_DISTANCE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        SENTINEL_FOLLOWS_SUB_LEVELS = create(builder, "Sentinel Follows Sub-Levels", "If sentinels should attempt to follow sub-levels through assembly, disassembly, splitting, etc.")
                .define("sentinel_follows_sub_level", true);

        MAX_MERGE_DISTANCE = create(builder, "Merge Max Distance", "How far the Merge spell is allowed to move blocks")
                .defineInRange("max_merge_distance", 5, 1, Double.MAX_VALUE);

//        FRAME_MAX_WIDTH = builder
//                .comment("The max side length of a frame along the x axis.")
//                .defineInRange("frame_max_width", 18, 1, Integer.MAX_VALUE);
//        FRAME_MAX_HEIGHT = builder
//                .comment("The max side length of a frame along the y axis.")
//                .defineInRange("frame_max_width", 18, 1, Integer.MAX_VALUE);
//        FRAME_MAX_LENGTH = builder
//                .comment("The max side length of a frame along the z axis.")
//                .defineInRange("frame_max_width", 18, 1, Integer.MAX_VALUE);


        SPEC = builder.build();
    }

    public static ModConfigSpec.Builder create(ModConfigSpec.Builder builder, String name, String comment) {
        String id = name.toLowerCase(Locale.ROOT).replaceAll("[^a-zA-Z0-9]", "_");
        String nameKey = WorldCasting.MOD_ID + ".config." + id;
        String commentKey = nameKey + ".tooltip";
        WorldCastingLang.LANG_MAP.put(nameKey, name);
        WorldCastingLang.LANG_MAP.put(commentKey, comment);
        return builder.comment(commentKey).translation(nameKey);
    }
}
