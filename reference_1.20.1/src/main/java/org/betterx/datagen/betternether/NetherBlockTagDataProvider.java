package org.betterx.datagen.betternether;

import org.betterx.bclib.api.v3.datagen.TagDataProvider;
import org.betterx.betternether.BetterNether;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

public class NetherBlockTagDataProvider extends TagDataProvider<Block> {

    public NetherBlockTagDataProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture,
            ExistingFileHelper existingFileHelper
    ) {
        super(
                TagManager.BLOCKS,
                List.of(BetterNether.MOD_ID, "c"),
                Set.of(
                        CommonBlockTags.NETHER_MYCELIUM,
                        CommonBlockTags.WOODEN_BARREL,
                        CommonBlockTags.WOODEN_CHEST,
                        CommonBlockTags.WOODEN_COMPOSTER,
                        CommonBlockTags.WORKBENCHES
                ),
                output,
                registriesFuture,
                existingFileHelper
        );
    }
}
