package org.betterx.betternether.mixin.common;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DiggerItem.class)
public abstract class MiningToolItemMixin extends TieredItem implements Vanishable {
    public MiningToolItemMixin(Tier tier, Properties properties) {
        super(tier, properties);
    }


    @Inject(method = "isCorrectToolForDrops", at = @At(value = "RETURN"), cancellable = true)
    private void effectiveOn(BlockState state, CallbackInfoReturnable<Boolean> info) {
        if (state.is(org.betterx.worlds.together.tag.v3.CommonBlockTags.NETHER_PORTAL_FRAME)) {
            info.setReturnValue(info.getReturnValue() && this.getTier().getLevel() >= 3);
        }
    }
}
