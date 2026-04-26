package dev.kyanbirb.world_casting.content.block;

import dev.kyanbirb.world_casting.index.WorldCastingBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class FrameBlock extends RotatedPillarBlock {
    public static final BooleanProperty CORNER = BooleanProperty.create("corner");

    public FrameBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(CORNER, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return getState(state, context.getLevel(), context.getClickedPos());
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        level.setBlock(pos, getState(state, level, pos), 2);
    }

    private BlockState getState(BlockState state, Level level, BlockPos pos) {
        boolean corner = false;
        for (Direction direction : Direction.values()) {
            BlockState relativeState = level.getBlockState(pos.relative(direction));
            if(!relativeState.is(WorldCastingBlocks.FRAME.value())) continue;

            Direction.Axis axis = relativeState.getValue(AXIS);
            if(axis != state.getValue(AXIS) && axis == direction.getAxis()) {
                corner = true;
                break;
            }
        }
        return state.setValue(CORNER, corner);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(CORNER));
    }
}
