package org.betterx.betternether.blocks;

import org.betterx.bclib.behaviours.interfaces.BehaviourStone;
import org.betterx.bclib.interfaces.CustomItemProvider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockNetherRuby extends BlockBase implements CustomItemProvider, BehaviourStone {
    public BlockNetherRuby() {
        super(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK));
    }

    @Override
    public BlockItem getCustomItem(ResourceLocation blockID, Item.Properties settings) {
        return new BlockItem(this, settings.fireResistant());
    }
}
