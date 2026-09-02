package org.betterx.datagen.betternether;

import org.betterx.bclib.api.v3.datagen.TagDataProvider;
import org.betterx.betternether.BetterNether;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;

import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NetherItemTagDataProvider extends TagDataProvider<Item> {

    public NetherItemTagDataProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture,
            ExistingFileHelper existingFileHelper
    ) {
        super(TagManager.ITEMS, List.of(BetterNether.MOD_ID, "c"), output, registriesFuture, existingFileHelper);
    }
}
