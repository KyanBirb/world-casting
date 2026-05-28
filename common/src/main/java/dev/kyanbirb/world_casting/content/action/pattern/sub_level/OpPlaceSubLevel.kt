package dev.kyanbirb.world_casting.content.action.pattern.sub_level

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.ktxt.UseOnContext
import dev.kyanbirb.world_casting.util.asActionResult
import dev.kyanbirb.world_casting.util.getQuaternion
import dev.kyanbirb.world_casting.util.subLevelContainer
import dev.ryanhcode.sable.companion.math.JOMLConversion
import dev.ryanhcode.sable.companion.math.Pose3d
import dev.ryanhcode.sable.physics.config.block_properties.PhysicsBlockPropertyHelper
import dev.ryanhcode.sable.sublevel.storage.SubLevelRemovalReason
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

class OpPlaceSubLevel : ConstMediaAction {
    override val argc: Int
        get() = 2

    override val mediaCost: Long
        get() = MediaConstants.DUST_UNIT / 8

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val orientation = args.getQuaternion(1, argc)
        val position = args.getVec3(0, argc)

        env.assertPosInRange(BlockPos.containing(position))

        val blockPos = BlockPos.containing(position);
        var blockHit = BlockHitResult(
            Vec3.atCenterOf(blockPos), env.castingEntity?.direction ?: Direction.NORTH, blockPos, false
        )

        var itemUseCtx = env.queryForMatchingStack { it.item is BlockItem }
            ?.let { UseOnContext(env.world, env.castingEntity as? ServerPlayer, env.castingHand, it, blockHit) }
            ?: throw MishapBadOffhandItem.of(ItemStack.EMPTY, "placeable")
        var placeContext = BlockPlaceContext(itemUseCtx)

        val worldState = env.world.getBlockState(blockPos)
        if(!worldState.canBeReplaced(placeContext))
            throw MishapBadBlock.of(blockPos, "replaceable")

        val stack = env.queryForMatchingStack { it.item is BlockItem }
        if(stack != null && !stack.isEmpty) {
            val defaultState = (stack.item as BlockItem).block.defaultBlockState()
            if(PhysicsBlockPropertyHelper.getMass(env.world, blockPos, defaultState) > 0.0) {

                val spoofedStack = stack.copy()
                spoofedStack.count = 1

                val container = env.world.subLevelContainer
                val pose = Pose3d()
                pose.position().set(JOMLConversion.toJOML(position))
                pose.orientation().set(orientation)
                val subLevel = container!!.allocateNewSubLevel(pose)
                val plot = subLevel.plot
                plot.newEmptyChunk(plot.centerChunk)

                val centerPos = plot.centerBlock

                blockHit = BlockHitResult(centerPos.center, env.castingEntity?.direction ?: Direction.NORTH, centerPos, false)
                itemUseCtx = UseOnContext(env.world, env.castingEntity as? ServerPlayer, env.otherHand, spoofedStack, blockHit)
                placeContext = BlockPlaceContext(itemUseCtx)

                if(env.withdrawItem({ ItemStack.isSameItemSameComponents(it, stack) }, 1, false)) {
                    val result = spoofedStack.useOn(placeContext)
                    if(result != InteractionResult.FAIL) {
                        env.withdrawItem({ ItemStack.isSameItemSameComponents(it, stack) }, 1, true)
                        return subLevel.asActionResult
                    } else {
                        container.removeSubLevel(subLevel, SubLevelRemovalReason.REMOVED)
                    }
                }

            }

        }

        throw MishapBadOffhandItem.of(stack, "placeable")
    }

}