package org.betterx.betternether.mixin.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingMenu.class)
public abstract class CraftingScreenHandlerMixin {
    @Redirect(
            method = "stillValid",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;stillValid(Lnet/minecraft/world/inventory/ContainerLevelAccess;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/Block;)Z"
            ),
            require = 0
    )
    private boolean bnx$allowAnyCraftingTableBlock(
            ContainerLevelAccess access,
            Player player,
            Block vanillaCraftingTableBlock
    ) {
        return access.evaluate((world, pos) -> {
            final boolean isVanillaOrCustomTable = world.getBlockState(pos).is(vanillaCraftingTableBlock)
                    || world.getBlockState(pos).getBlock() instanceof CraftingTableBlock;
            if (!isVanillaOrCustomTable) {
                return false;
            }

            return player.distanceToSqr(
                    (double) pos.getX() + 0.5D,
                    (double) pos.getY() + 0.5D,
                    (double) pos.getZ() + 0.5D
            ) <= 64.0D;
        }, true);
    }
}
