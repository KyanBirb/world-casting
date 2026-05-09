package dev.kyanbirb.world_casting.util

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import dev.kyanbirb.world_casting.content.iota.FragmentIota
import dev.kyanbirb.world_casting.content.iota.QuaternionIota
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer
import dev.ryanhcode.sable.sublevel.SubLevel
import net.minecraft.world.level.Level
import org.joml.Quaterniond
import org.joml.Quaterniondc

fun List<Iota>.getFragment(idx: Int, argc: Int = 0): FragmentIota {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is FragmentIota) {
        return x
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "fragment")
    }
}

fun List<Iota>.getFragmentOrNull(idx: Int, argc: Int = 0): FragmentIota? {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    return x as? FragmentIota
        ?: if(x is NullIota) {
            null
        } else {
            throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "fragment")
        }
}

fun List<Iota>.getQuaternion(idx: Int, argc: Int = 0): Quaterniondc {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is QuaternionIota) {
        return x.quaternion
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "quaternion")
    }
}

inline val SubLevel.asActionResult get() = listOf(FragmentIota(this.uniqueId))

inline val List<SubLevel>.asActionResult get() = listOf(ListIota(this.map {
    FragmentIota(it.uniqueId)
}))

inline val Level.subLevelContainer get() = SubLevelContainer.getContainer(this)

inline val Quaterniond.asActionResult get() = listOf(QuaternionIota(this))
