package org.betterx.datagen.betternether.recipes;

import org.betterx.bclib.api.v3.datagen.BlockLootTableProvider;
import org.betterx.betternether.BetterNether;

import net.minecraft.data.PackOutput;

import java.util.List;

public class NetherBlockLootTableProvider extends BlockLootTableProvider {
    public NetherBlockLootTableProvider(
            PackOutput output
    ) {
        super(output, List.of(BetterNether.MOD_ID));
    }

}
