package org.betterx.betternether.blocks;

import net.minecraft.world.level.block.DoorBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BNWoodlikeDoor extends DoorBlock.Wood {

    public BNWoodlikeDoor(Block source, WoodType type) {
        super(source, type.setType());
    }

    public BNWoodlikeDoor(Properties properties, WoodType type) {
        super(properties, type.setType());
    }

}
