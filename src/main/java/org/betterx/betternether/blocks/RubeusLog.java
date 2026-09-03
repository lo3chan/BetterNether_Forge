package org.betterx.betternether.blocks;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.betterx.betternether.blocks.BNBlockProperties.TripleShape;
import org.betterx.bclib.interfaces.tools.AddMineableAxe;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;

public class RubeusLog extends RotatedPillarBlock implements AddMineableAxe {
    public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;

    public RubeusLog(MapColor color, Block striped) {
        super(color, striped, false);
        this.registerDefaultState(this.defaultBlockState()
                                      .setValue(AXIS, Direction.Axis.Y)
                                      .setValue(SHAPE, TripleShape.BOTTOM));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(SHAPE);
    }
}
