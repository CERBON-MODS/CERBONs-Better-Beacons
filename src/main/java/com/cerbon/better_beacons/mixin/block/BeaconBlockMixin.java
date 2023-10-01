package com.cerbon.better_beacons.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BeaconBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("deprecation")
@Mixin(BeaconBlock.class)
public class BeaconBlockMixin extends Block implements SimpleWaterloggedBlock {
    @Unique private static final DirectionProperty BETTER_BEACONS_FACING = HorizontalDirectionalBlock.FACING;
    @Unique private static final BooleanProperty BETTER_BEACONS_WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected BeaconBlockMixin(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BETTER_BEACONS_WATERLOGGED, Boolean.FALSE));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(BETTER_BEACONS_FACING, context.getHorizontalDirection().getOpposite()).setValue(BETTER_BEACONS_WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(BETTER_BEACONS_WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(BETTER_BEACONS_WATERLOGGED))
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BETTER_BEACONS_FACING, BETTER_BEACONS_WATERLOGGED);
    }
}
