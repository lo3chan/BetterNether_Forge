package org.betterx.betternether;

import org.betterx.betternether.advancements.BNCriterion;
import org.betterx.betternether.commands.CommandRegistry;
import org.betterx.betternether.config.Config;
import org.betterx.betternether.config.Configs;
import org.betterx.betternether.enchantments.ObsidianBreaker;
import org.betterx.betternether.loot.BNLoot;
import org.betterx.betternether.recipes.IntegrationRecipes;
import org.betterx.betternether.registry.*;
import org.betterx.betternether.tab.CreativeTabs;
import org.betterx.betternether.world.BNWorldGenerator;
import org.betterx.worlds.together.util.Logger;
import org.betterx.worlds.together.world.WorldConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.neoforged.fml.common.Mod;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(BetterNether.MOD_ID)
public class BetterNether {
    public static final String MOD_ID = "betternether";
    public static final Logger LOGGER = new Logger(MOD_ID);
    private static boolean thinArmor = true;
    private static boolean lavafallParticles = true;

    public BetterNether(IEventBus modBus) {

        // Avoid eager block/item class loading here; it can trip registry freeze in datagen.
        modBus.addListener(EventPriority.HIGHEST, this::ensureStructuresLoaded);
        modBus.addListener(NetherEntities::onRegister);
        modBus.addListener(NetherParticles::onRegister);
        modBus.addListener(NetherPoiTypes::onRegister);
        modBus.addListener(NetherFeatures::onRegister);
        modBus.addListener((RegisterEvent event) -> BlockEntitiesRegistry.register(event));
        modBus.addListener(NetherTemplates::register);
        modBus.addListener(EventPriority.HIGHEST, this::ensureBlocksLoaded);
        modBus.addListener(EventPriority.HIGHEST, this::ensureItemsLoaded);
        modBus.addListener(this::onCommonSetup);
    }

    public void onInitialize() {
        LOGGER.info("=^..^=    BetterNether for 1.21.1    =^..^=");
        initOptions();
        SoundsRegistry.ensureStaticallyLoaded();
        NetherGameRules.ensureStaticallyLoaded();
        BNWorldGenerator.onModInit();
        NetherStructures.register();
        NetherBiomes.register();
        BrewingRegistry.register();
        CommandRegistry.register();
        ObsidianBreaker.register();
        Config.save();

        BNLoot.register();
        BNCriterion.register();

        Configs.saveConfigs();
        WorldConfig.registerModCache(MOD_ID);
        Patcher.register();
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        onInitialize();
    }

    private void ensureBlocksLoaded(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.BLOCK)) {
            return;
        }
        try {
            Class.forName("org.betterx.betternether.registry.NetherBlocks");
        } catch (ClassNotFoundException ignored) {
        }
        NetherTags.register();
        // Ensure features/structure types are queued before registry events fire.
        NetherStructures.ensureStaticLoad();
        NetherFeatures.register();
    }

    private void ensureStructuresLoaded(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.STRUCTURE_TYPE)) {
            return;
        }
        NetherStructures.ensureStaticLoad();
    }

    private void ensureItemsLoaded(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.ITEM)) {
            return;
        }
        try {
            Class.forName("org.betterx.betternether.registry.NetherItems");
        } catch (ClassNotFoundException ignored) {
        }
        // Spawn eggs must be queued during the item registry event (entities register later).
        NetherEntities.registerSpawnEggs();
        IntegrationRecipes.register();
        CreativeTabs.register();
    }

    private void initOptions() {
        thinArmor = Configs.MAIN.getBoolean("improvement", "smaller_armor_offset", true);
        lavafallParticles = Configs.MAIN.getBoolean("improvement", "lavafall_particles", true);
    }

    public static boolean hasThinArmor() {
        return thinArmor;
    }

    public static void setThinArmor(boolean value) {
        thinArmor = value;
    }

    public static boolean hasLavafallParticles() {
        return lavafallParticles;
    }

    public static ResourceLocation makeID(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
