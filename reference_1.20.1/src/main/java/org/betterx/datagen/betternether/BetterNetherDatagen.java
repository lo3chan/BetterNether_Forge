package org.betterx.datagen.betternether;

import org.betterx.betternether.BetterNether;
import org.betterx.betternether.advancements.BNCriterion;
import org.betterx.bclib.api.v2.levelgen.structures.BCLStructure;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.betternether.registry.NetherBlocks;
import org.betterx.betternether.registry.NetherFeatures;
import org.betterx.betternether.registry.NetherItems;
import org.betterx.betternether.registry.NetherStructures;
import org.betterx.datagen.betternether.advancements.NetherAdvancementDataProvider;
import org.betterx.datagen.betternether.recipes.NetherBlockLootTableProvider;
import org.betterx.datagen.betternether.recipes.NetherChestLootTableProvider;
import org.betterx.datagen.betternether.recipes.NetherEntityLootTableProvider;
import org.betterx.datagen.betternether.recipes.NetherRecipeDataProvider;
import org.betterx.datagen.betternether.worldgen.NetherBiomesDataProvider;
import org.betterx.bclib.api.v3.levelgen.features.blockpredicates.BlockPredicates;
import org.betterx.bclib.api.v3.levelgen.features.placement.PlacementModifiers;
import org.betterx.bclib.api.v2.PostInitAPI;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = BetterNether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterNetherDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        TagManager.ensureStaticallyLoaded();
        NetherRecipeDataProvider.buildRecipes();
        NetherFeatures.register();
        NetherItems.getItemRegistry();
        NetherBlocks.getBlockRegistry();
        PostInitAPI.postInit(false);
        NetherStructures.register();
        BNCriterion.register();
        // Load biome data after features are registered to avoid null configured features during datagen.
        NetherBiomesDataProvider.ensureStaticallyLoaded();
        // Ensure BCLib placement/block predicate types are registered in BuiltInRegistries during datagen
        PlacementModifiers.ensureStaticInitialization();
        BlockPredicates.ensureStaticInitialization();
        // Datagen needs the custom FEATURE and STRUCTURE_TYPE entries in BuiltInRegistries,
        // otherwise serialization of configured features/structures fails.
        BCLFeature.registerForDatagen();
        BCLStructure.registerForDatagen();
        BetterNether.LOGGER.info(
                "[datagen] init complete; includeServer={}, includeClient={}",
                event.includeServer(),
                event.includeClient()
        );

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            RegistrySetBuilder registryBuilder = new RegistrySetBuilder();
            NetherRegistrySupplier.INSTANCE.bootstrapRegistries(registryBuilder);

            DatapackBuiltinEntriesProvider datapackProvider = new DatapackBuiltinEntriesProvider(
                    output,
                    event.getLookupProvider(),
                    registryBuilder,
                    Set.of(BetterNether.MOD_ID)
            );
            CompletableFuture<HolderLookup.Provider> registryProvider = datapackProvider.getRegistryProvider();

            // Run sanity check against the same registry snapshot that the vanilla writer will serialize.
            generator.addProvider(true, new RegistryCodecSanityProvider(registryProvider, NetherRegistrySupplier.INSTANCE));
            generator.addProvider(true, datapackProvider);
            BetterNether.LOGGER.info("[datagen] added registry builder for {}", BetterNether.MOD_ID);

            generator.addProvider(true, new NetherBiomesDataProvider(output, registryProvider, existingFileHelper));
            generator.addProvider(true, new NetherRecipeDataProvider(output));
            generator.addProvider(
                    true,
                    new ForgeAdvancementProvider(
                            output,
                            registryProvider,
                            existingFileHelper,
                            List.of(new NetherAdvancementDataProvider())
                    )
            );
            generator.addProvider(true, new NetherBlockTagDataProvider(output, registryProvider, existingFileHelper));
            generator.addProvider(true, new NetherItemTagDataProvider(output, registryProvider, existingFileHelper));
            generator.addProvider(
                    true,
                    new NamedDataProvider(
                            new NetherChestLootTableProvider(output),
                            "BetterNether Loot Tables - Chests"
                    )
            );
            generator.addProvider(
                    true,
                    new NamedDataProvider(
                            new NetherEntityLootTableProvider(output),
                            "BetterNether Loot Tables - Entities"
                    )
            );
            generator.addProvider(
                    true,
                    new NamedDataProvider(
                            new NetherBlockLootTableProvider(output),
                            "BetterNether Loot Tables - Blocks"
                    )
            );
        }
    }

    // DataGenerator uses provider names for deduping; wrap to avoid LootTableProvider name collisions.
    private static final class NamedDataProvider implements DataProvider {
        private final DataProvider delegate;
        private final String name;

        private NamedDataProvider(DataProvider delegate, String name) {
            this.delegate = delegate;
            this.name = name;
        }

        @Override
        public CompletableFuture<?> run(CachedOutput cachedOutput) {
            return delegate.run(cachedOutput);
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
