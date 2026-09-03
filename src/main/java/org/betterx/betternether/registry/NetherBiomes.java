package org.betterx.betternether.registry;

import org.betterx.bclib.api.v3.levelgen.biomes.BCLBiomeRegistry;
import org.betterx.bclib.api.v3.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.interfaces.NumericProvider;
import org.betterx.betternether.BN;
import org.betterx.betternether.BetterNether;
import org.betterx.betternether.config.Configs;
import org.betterx.betternether.world.NetherBiome;
import org.betterx.betternether.world.biomes.providers.NetherGrasslandsNumericProvider;
import org.betterx.betternether.world.biomes.providers.NetherMushroomForestEdgeNumericProvider;

import net.minecraft.core.Registry;

public class NetherBiomes {
    private static boolean REGISTERED = false;

    public static void register() {
        if (REGISTERED) {
            return;
        }
        REGISTERED = true;
        BCLBiomeRegistry.registerBiomeCodec(BN.id("biome"), NetherBiome.KEY_CODEC);
        BiomeAPI.registerNetherBiomeModification((biomeID, biome) -> {
            if (!biomeID.getNamespace().equals(BetterNether.MOD_ID)) {
                NetherEntities.modifyNonBNBiome(biomeID, biome);
                NetherFeatures.modifyNonBNBiome(biomeID, biome);
            }
        });
        BiomeAPI.onFinishingNetherBiomeTags((biomeID, biome) -> {
            if (!biomeID.getNamespace().equals(BetterNether.MOD_ID)) {
                NetherStructures.addNonBNBiomeTags(biomeID, biome);
            }
        });
        registerBiomeToggles();
        registerNumericProviders();
    }

    private static void registerBiomeToggles() {
        registerBiomeToggle("bone_reef");
        registerBiomeToggle("crimson_glowing_woods");
        registerBiomeToggle("crimson_pinewood");
        registerBiomeToggle("flooded_deltas");
        registerBiomeToggle("gloomwood");
        registerBiomeToggle("gravel_desert");
        registerBiomeToggle("magma_land");
        registerBiomeToggle("nether_grasslands");
        registerBiomeToggle("nether_jungle");
        registerBiomeToggle("nether_mushroom_forest");
        registerBiomeToggle("nether_mushroom_forest_edge");
        registerBiomeToggle("nether_swampland");
        registerBiomeToggle("nether_swampland_terraces");
        registerBiomeToggle("old_fungiwoods");
        registerBiomeToggle("old_swampland");
        registerBiomeToggle("old_warped_woods");
        registerBiomeToggle("poor_nether_grasslands");
        registerBiomeToggle("soul_plain");
        registerBiomeToggle("sulfuric_bone_reef");
        registerBiomeToggle("upside_down_forest");
        registerBiomeToggle("upside_down_forest_cleared");
        registerBiomeToggle("wart_forest");
        registerBiomeToggle("wart_forest_edge");
    }

    private static void registerBiomeToggle(String path) {
        Configs.BIOMES.getBoolean(BetterNether.MOD_ID + "." + path, "enabled", true);
    }

    private static void registerNumericProviders() {
        registerNumericProvider(
                BetterNether.makeID("nether_grasslands"),
                NetherGrasslandsNumericProvider.CODEC
        );
        registerNumericProvider(
                BetterNether.makeID("nether_mushroom_forrest_edge"),
                NetherMushroomForestEdgeNumericProvider.CODEC
        );
    }

    private static void registerNumericProvider(
            net.minecraft.resources.ResourceLocation id,
            com.mojang.serialization.Codec<? extends NumericProvider> codec
    ) {
        if (!NumericProvider.NUMERIC_PROVIDER.containsKey(id)) {
            Registry.register(
                    NumericProvider.NUMERIC_PROVIDER,
                    id,
                    codec
            );
        }
    }
}
