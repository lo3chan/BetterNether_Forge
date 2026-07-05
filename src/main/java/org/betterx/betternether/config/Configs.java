package org.betterx.betternether.config;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.config.PathConfig;
import org.betterx.betternether.BetterNether;

import net.minecraft.resources.ResourceLocation;

public class Configs {
    public static final PathConfig MAIN = new PathConfig(BetterNether.MOD_ID, "main");
    public static final PathConfig GENERATOR = new PathConfig(BetterNether.MOD_ID, "generator");
    public static final PathConfig BLOCKS = new PathConfig(BetterNether.MOD_ID, "blocks");
    public static final PathConfig ITEMS = new PathConfig(BetterNether.MOD_ID, "items");
    public static final PathConfig BIOMES = new PathConfig(BetterNether.MOD_ID, "biomes");
    public static final PathConfig MOBS = new PathConfig(BetterNether.MOD_ID, "mobs");
    public static final PathConfig RECIPES = new PathConfig(BetterNether.MOD_ID, "recipes");
    public static final PathConfig STRUCTURES = new PathConfig(BetterNether.MOD_ID, "structures");
    public static final PathConfig CLIENT_CONFIG = new PathConfig(BetterNether.MOD_ID, "client", false);

    public static void saveConfigs() {
        MAIN.saveChanges();
        GENERATOR.saveChanges();
        BLOCKS.saveChanges();
        ITEMS.saveChanges();
        MOBS.saveChanges();
        RECIPES.saveChanges();
        STRUCTURES.saveChanges();
        BIOMES.saveChanges();

        if (BCLib.isClient()) {
            CLIENT_CONFIG.saveChanges();
        }
    }

    public static boolean isStructureEnabled(ResourceLocation id) {
        return !BetterNether.MOD_ID.equals(id.getNamespace()) || STRUCTURES.getBooleanRoot(id.getPath(), true);
    }
}
