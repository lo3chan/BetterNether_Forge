package org.betterx.betternether.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class BlocksHelper {
    public static final int FLAG_UPDATE_BLOCK = 1;
    public static final int FLAG_SEND_CLIENT_CHANGES = 2;
    public static final int FLAG_NO_RERENDER = 4;
    public static final int FORCED_UPDATE = FLAG_UPDATE_BLOCK | FLAG_SEND_CLIENT_CHANGES;
    public static final int SET_SILENT = FLAG_UPDATE_BLOCK | FLAG_SEND_CLIENT_CHANGES | FLAG_NO_RERENDER;

    public static void setWithoutUpdate(LevelAccessor world, BlockPos pos, BlockState state) {
        world.setBlock(pos, state, SET_SILENT);
    }

    public static void setWithoutUpdate(LevelAccessor world, BlockPos pos, Block block) {
        world.setBlock(pos, block.defaultBlockState(), SET_SILENT);
    }

    public static boolean findSurroundingSurface(LevelAccessor world, BlockPos.MutableBlockPos pos, Direction dir, int dist, Predicate<BlockState> predicate) {
        for (int i = 0; i < dist; i++) {
            if (predicate.test(world.getBlockState(pos))) {
                return true;
            }
            pos.move(dir);
        }
        return false;
    }

    public static boolean isTerrain(BlockState state) {
        // Assume default true for simplifications unless explicitly checked by tags
        return state.canOcclude();
    }
}
