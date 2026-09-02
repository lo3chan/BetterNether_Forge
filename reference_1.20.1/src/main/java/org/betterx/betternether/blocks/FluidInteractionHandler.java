package org.betterx.betternether.blocks;

import org.betterx.betternether.BetterNether;
import org.betterx.betternether.advancements.BNCriterion;
import org.betterx.betternether.registry.NetherBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = BetterNether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FluidInteractionHandler {
    @SubscribeEvent
    public static void onFluidPlaceBlock(BlockEvent.FluidPlaceBlockEvent event) {
        if (event.getNewState().is(Blocks.OBSIDIAN) && event.getLevel() instanceof Level level) {
            BlockPos pos = event.getPos();
            BlockState belowState = level.getBlockState(pos.below());

            if (belowState.is(Blocks.SOUL_SOIL)
                || belowState.is(Blocks.SOUL_SAND)
                || belowState.is(NetherBlocks.SOUL_SANDSTONE)) {
                event.setNewState(NetherBlocks.BLUE_OBSIDIAN.defaultBlockState());
                level.levelEvent(1501, pos, 0);

                if (!level.isClientSide) {
                    triggerCriterion(level, pos);
                }
            }
        }
    }

    private static void triggerCriterion(Level level, BlockPos pos) {
        List<ServerPlayer> players = level.getEntitiesOfClass(
                ServerPlayer.class,
                new AABB(pos).inflate(10.0, 5.0, 10.0)
        );

        for (ServerPlayer player : players) {
            BNCriterion.BREW_BLUE.trigger(player);
        }
    }
}
