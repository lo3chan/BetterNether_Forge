package org.betterx.betternether.mixin.common;

import org.betterx.betternether.portals.BNPortalShape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {
    @Inject(method = "onPlace", at = @At("TAIL"))
    private void bn_trySpawnPortal(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState oldState,
            boolean isMoving,
            CallbackInfo ci
    ) {
        if (level.isClientSide()) return;
        if (!isPortalDimension(level)) return;
        if (!state.is(Blocks.FIRE) && !state.is(Blocks.SOUL_FIRE)) return;
        if (level.getBlockState(pos).is(Blocks.NETHER_PORTAL)) return;

        BNPortalShape shape = new BNPortalShape(level, pos, Direction.Axis.X);
        if (!shape.isValid()) {
            shape = new BNPortalShape(level, pos, Direction.Axis.Z);
        }
        if (shape.isValid()) {
            shape.createPortalBlocks();
        }
    }

    @Unique
    private static boolean isPortalDimension(Level level) {
        return level.dimension() == Level.OVERWORLD || level.dimension() == Level.NETHER;
    }
}
