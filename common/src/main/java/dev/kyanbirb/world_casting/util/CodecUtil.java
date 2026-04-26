package dev.kyanbirb.world_casting.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;

import java.util.List;

public class CodecUtil {

    public static final Codec<Quaterniondc> QUATERNIONDC = Codec.DOUBLE.listOf(4, 4).xmap(
            CodecUtil::quaternionFromList,
            CodecUtil::quaternionAsList
    );

    public static final StreamCodec<ByteBuf, Quaterniondc> QUATERNIONDC_STREAM = ByteBufCodecs.DOUBLE.apply(ByteBufCodecs.list(4)).map(
            CodecUtil::quaternionFromList,
            CodecUtil::quaternionAsList
    );

    private static @NotNull Quaterniondc quaternionFromList(List<Double> list) {
        return new Quaterniond(list.get(0), list.get(1), list.get(2), list.get(3));
    }

    private static @NotNull List<Double> quaternionAsList(Quaterniondc quat) {
        return List.of(quat.x(), quat.y(), quat.z(), quat.w());
    }
}
