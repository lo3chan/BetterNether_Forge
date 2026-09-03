package org.betterx.betternether.integrations.blockus;

import net.neoforged.neoforge.fml.ModList;

public class BlockusUtils {
    public static boolean hasBlockus() {
        return ModList.get().isLoaded("blockus");
    }
}
