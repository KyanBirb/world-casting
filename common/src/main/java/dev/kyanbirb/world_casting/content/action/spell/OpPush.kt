package dev.kyanbirb.world_casting.content.action.spell

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kyanbirb.world_casting.mixinterfaces.DeferredForceHaver
import dev.ryanhcode.sable.Sable
import dev.ryanhcode.sable.sublevel.ServerSubLevel
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem
import org.joml.Vector3d

object OpPush : SpellAction {
    override val argc: Int
        get() = 2

    const val COST_SCALE = 1 / 1000f

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val forcePos = args.getVec3(0, argc)
        val globalForce = args.getVec3(1, argc)

        env.assertVecInRange(forcePos)

        val subLevel = (Sable.HELPER.getContaining(env.world, forcePos) ?: throw MishapBadLocation(forcePos, "not_in_sub_level")) as ServerSubLevel
        val aabb = subLevel.plot.boundingBox
        val subLevelSize = aabb.width() + aabb.height() + aabb.length()

        val force = Vector3d(globalForce.toVector3f())
        val cost = MediaConstants.DUST_UNIT.toDouble() * force.length() * subLevelSize.toDouble() * COST_SCALE
        force.mul(subLevel.massTracker.mass)
        subLevel.logicalPose().transformNormalInverse(force)

        val particlePos = subLevel.logicalPose().transformPosition(forcePos)

        return SpellAction.Result(
            Spell(subLevel, Vector3d(forcePos.x, forcePos.y, forcePos.z), force),
            cost.toLong(),
            listOf(
                ParticleSpray(
                    particlePos,
                    globalForce.normalize(),
                    0.0,
                    0.1
                )
            )
        )
    }

    private data class Spell(val subLevel: ServerSubLevel, val forcePos: Vector3d, val force: Vector3d) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val deferredForceHaver = subLevel as DeferredForceHaver
            if(deferredForceHaver.`world_casting$hasForceTotal`()) {
                deferredForceHaver.`world_casting$getForceTotal`().applyImpulseAtPoint(subLevel, forcePos, force)
            } else {
                val physicsSystem = SubLevelPhysicsSystem.get(env.world)
                val physicsHandle = physicsSystem.getPhysicsHandle(subLevel as ServerSubLevel)
                physicsHandle.applyImpulseAtPoint(forcePos, force)
            }
        }
    }

}
