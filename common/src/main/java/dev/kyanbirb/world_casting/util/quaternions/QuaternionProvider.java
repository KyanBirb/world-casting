package dev.kyanbirb.world_casting.util.quaternions;

import at.petrak.hexcasting.api.casting.iota.Iota;
import org.joml.Quaterniondc;

public interface QuaternionProvider {
    boolean isQuaternion(Iota iota);
    Quaterniondc getQuaternion(Iota iota);
    Iota createIota(Quaterniondc quaternion);
}
