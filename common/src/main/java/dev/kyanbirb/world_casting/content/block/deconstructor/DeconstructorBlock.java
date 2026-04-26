package dev.kyanbirb.world_casting.content.block.deconstructor;

import dev.kyanbirb.world_casting.index.WorldCastingBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DeconstructorBlock extends Block implements EntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    private static final VoxelShape ACTIVE_SHAPE = Shapes.box(0, 0, 0, 1, 18 / 16f, 1);

    public DeconstructorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(ACTIVE, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(ACTIVE));
    }

    @Override
    protected int getLightBlock(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.getValue(ACTIVE) ? 10 : 0;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return WorldCastingBlockEntityTypes.DECONSTRUCTOR.value().create(blockPos, blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if(pState.getValue(ACTIVE)) {
            return ACTIVE_SHAPE;
        }
        return super.getShape(pState, pLevel, pPos, pContext);
    }
}
