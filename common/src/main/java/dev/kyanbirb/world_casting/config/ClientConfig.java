package dev.kyanbirb.world_casting.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue CUSTOM_VECTOR_DISPLAY;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        CUSTOM_VECTOR_DISPLAY = builder
                .comment("If vector iotas should display local coordinates when inside of a sub-level plot")
                .define("custom_vector_display", true);

        SPEC = builder.build();
    }
}
