package dev.kyanbirb.world_casting.index;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import dev.kyanbirb.world_casting.PlatformHelper;
import dev.kyanbirb.world_casting.content.iota.FragmentIota;
import dev.kyanbirb.world_casting.content.iota.QuaternionIota;
import net.minecraft.core.Holder;

public class WorldCastingIotaTypes {

    public static final Holder<IotaType<FragmentIota>> FRAGMENT = PlatformHelper
            .registerIotaType("fragment", () -> FragmentIota.TYPE);

    public static final Holder<IotaType<QuaternionIota>> QUATERNION = PlatformHelper
            .registerIotaType("quaternion", () -> QuaternionIota.TYPE);

    public static void init() {

    }
}
