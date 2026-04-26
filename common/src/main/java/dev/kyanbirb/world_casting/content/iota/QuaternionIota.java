package dev.kyanbirb.world_casting.content.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.mojang.serialization.MapCodec;
import dev.kyanbirb.world_casting.index.WorldCastingIotaTypes;
import dev.kyanbirb.world_casting.util.CodecUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;

public class QuaternionIota extends Iota {
    public static final QuaternionIota IDENTITY =
            new QuaternionIota(new Quaterniond());

    private final Quaterniondc quaternion;

    public QuaternionIota(Quaterniondc quaternion) {
        super(WorldCastingIotaTypes.QUATERNION::value);
        this.quaternion = quaternion;
    }

    @Override
    public boolean isTruthy() {
        return true;
    }

    @Override
    protected boolean toleratesOther(Iota iota) {
        if(typesMatch(this, iota) && iota instanceof QuaternionIota quaternionIota) {
            return quaternionIota.quaternion.equals(this.quaternion);
        }

        return false;
    }

    @Override
    public Component display() {
        String string = "(%.2f + %.2fi + %.2fj + %.2fk)"
                .formatted(this.quaternion.w(), this.quaternion.x(), this.quaternion.y(), this.quaternion.z());
        return Component.literal(string).withColor(0xff_ff8d4c);
    }

    @Override
    public int hashCode() {
        return this.quaternion.hashCode();
    }

    public Quaterniondc getQuaternion() {
        return quaternion;
    }

    public static final IotaType<QuaternionIota> TYPE = new IotaType<>() {

        @Override
        public MapCodec<QuaternionIota> codec() {
            return CodecUtil.QUATERNIONDC.xmap(
                    QuaternionIota::new,
                    QuaternionIota::getQuaternion
            ).fieldOf("value");
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, QuaternionIota> streamCodec() {
            return CodecUtil.QUATERNIONDC_STREAM.map(
                    QuaternionIota::new,
                    QuaternionIota::getQuaternion
            ).mapStream(b -> b);
        }

        @Override
        public int color() {
            return 0xffff8d4c;
        }

    };
}
