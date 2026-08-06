package dev.kyanbirb.world_casting.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import static dev.kyanbirb.world_casting.config.CommonConfig.create;

public class ClientConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue CUSTOM_VECTOR_DISPLAY;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        CUSTOM_VECTOR_DISPLAY = create(builder, "Custom Vector Display", "If vector iotas should display local coordinates when inside of a sub-level plot")
                .comment("If vector iotas should display local coordinates when inside of a sub-level plot")
                .define("custom_vector_display", true);

        SPEC = builder.build();
    }
}
