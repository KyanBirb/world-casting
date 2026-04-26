package dev.kyanbirb.world_casting.index;

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.kyanbirb.world_casting.PlatformHelper;
import dev.kyanbirb.world_casting.content.arithmetic.QuaternionArithmetic;
import net.minecraft.core.Holder;

public class WorldCastingArithmetics {

    public static Holder<Arithmetic> QUATERNION =
            PlatformHelper.INSTANCE.register(IXplatAbstractions.INSTANCE.getArithmeticRegistry(), "quaternion", QuaternionArithmetic::new);

    public static void init() {

    }

}
