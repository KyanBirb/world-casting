package dev.kyanbirb.world_casting.content.action.pattern.sub_level

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import dev.kyanbirb.world_casting.util.asActionResult
import dev.ryanhcode.sable.Sable

class OpGetContaining : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if(args[0] is EntityIota) {
            val entity = args.getEntity(env.world, 0, argc)
            var subLevel = Sable.HELPER.getTrackingOrVehicleSubLevel(entity)
            if(subLevel == null) {
                subLevel = Sable.HELPER.getContaining(entity)
            }
            if(subLevel != null && env.isEntityInRange(entity)) {
                return subLevel.asActionResult
            }
            return listOf(NullIota())
        } else {
            val pos = args.getVec3(0, argc)
            val subLevel = Sable.HELPER.getContaining(env.world, pos)
            if(subLevel != null && env.isVecInRange(pos)) {
                return subLevel.asActionResult
            }
            return listOf(NullIota())
        }
    }
}