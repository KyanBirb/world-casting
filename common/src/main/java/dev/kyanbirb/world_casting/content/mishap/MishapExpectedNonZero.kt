package dev.kyanbirb.world_casting.content.mishap

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.TreeList
import dev.kyanbirb.world_casting.content.iota.fragment.FragmentIota
import dev.ryanhcode.sable.Sable
import net.minecraft.network.chat.Component
import net.minecraft.world.item.DyeColor
import net.minecraft.world.phys.Vec3
import kotlin.math.sqrt

class MishapExpectedNonZero() : Mishap() {
    override fun accentColor(
        ctx: CastingEnvironment,
        errorCtx: Context
    ): FrozenPigment = dyeColor(DyeColor.GRAY)

    override fun execute(
        env: CastingEnvironment,
        errorCtx: Context,
        stack: TreeList<Iota>
    ): TreeList<Iota> {
        return stack
    }

    override fun errorMessage(
        ctx: CastingEnvironment,
        errorCtx: Context
    ): Component {
        return error("world_casting.expected_non_zero")
    }
}