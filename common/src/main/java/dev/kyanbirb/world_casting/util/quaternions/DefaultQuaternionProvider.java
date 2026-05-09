package dev.kyanbirb.world_casting.util.quaternions;

import at.petrak.hexcasting.api.casting.iota.Iota;
import dev.kyanbirb.world_casting.content.iota.QuaternionIota;
import org.joml.Quaterniondc;

public class DefaultQuaternionProvider implements QuaternionProvider {
    @Override
    public boolean isQuaternion(Iota iota) {
        return iota instanceof QuaternionIota;
    }

    @Override
    public Quaterniondc getQuaternion(Iota iota) {
        return ((QuaternionIota) iota).getQuaternion();
    }

    @Override
    public Iota createIota(Quaterniondc quaternion) {
        return new QuaternionIota(quaternion);
    }
}
