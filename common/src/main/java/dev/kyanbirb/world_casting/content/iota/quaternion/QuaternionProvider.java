package dev.kyanbirb.world_casting.content.iota.quaternion;

import at.petrak.hexcasting.api.casting.iota.Iota;
import org.joml.Quaterniondc;

public interface QuaternionProvider {
    boolean isQuaternion(Iota iota);
    Quaterniondc getQuaternion(Iota iota);
    Iota createIota(Quaterniondc quaternion);
}
