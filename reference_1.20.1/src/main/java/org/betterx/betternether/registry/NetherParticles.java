package org.betterx.betternether.registry;

import org.betterx.bclib.particles.BCLParticleType;
import org.betterx.betternether.BetterNether;
import org.betterx.betternether.config.Configs;
import org.betterx.betternether.particles.BNParticleProvider;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.registries.RegisterEvent;

public class NetherParticles {
    public static SimpleParticleType BLUE_DRIPPING_OBSIDIAN_TEAR;
    public static SimpleParticleType BLUE_FALLING_OBSIDIAN_TEAR;
    public static SimpleParticleType BLUE_LANDING_OBSIDIAN_TEAR;

    public static SimpleParticleType BLUE_DRIPPING_OBSIDIAN_WEEP;
    public static SimpleParticleType BLUE_FALLING_OBSIDIAN_WEEP;
    public static SimpleParticleType BLUE_LANDING_OBSIDIAN_WEEP;

    public static SimpleParticleType DRIPPING_OBSIDIAN_WEEP;
    public static SimpleParticleType FALLING_OBSIDIAN_WEEP;
    public static SimpleParticleType LANDING_OBSIDIAN_WEEP;

    private static boolean registered = false;

    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.PARTICLE_TYPE)) {
            return;
        }
        registerTypes();
    }

    private static void registerTypes() {
        if (registered) {
            return;
        }
        registered = true;

        BLUE_DRIPPING_OBSIDIAN_TEAR = BCLParticleType.register(
                BetterNether.makeID("blue_dripping_obsidian_tear")
        );

        BLUE_FALLING_OBSIDIAN_TEAR = BCLParticleType.register(
                BetterNether.makeID("blue_falling_obsidian_tear")
        );

        BLUE_LANDING_OBSIDIAN_TEAR = BCLParticleType.register(
                BetterNether.makeID("blue_landing_obsidian_tear")
        );

        if (Configs.MAIN.getBoolean("particles", "weeping", true)) {
            BLUE_DRIPPING_OBSIDIAN_WEEP = BCLParticleType.register(
                    BetterNether.makeID("blue_dripping_obsidian_weep")
            );

            BLUE_FALLING_OBSIDIAN_WEEP = BCLParticleType.register(
                    BetterNether.makeID("blue_falling_obsidian_weep")
            );

            BLUE_LANDING_OBSIDIAN_WEEP = BCLParticleType.register(
                    BetterNether.makeID("blue_landing_obsidian_weep")
            );

            DRIPPING_OBSIDIAN_WEEP = BCLParticleType.register(
                    BetterNether.makeID("dripping_obsidian_weep")
            );

            FALLING_OBSIDIAN_WEEP = BCLParticleType.register(
                    BetterNether.makeID("falling_obsidian_weep")
            );

            LANDING_OBSIDIAN_WEEP = BCLParticleType.register(
                    BetterNether.makeID("landing_obsidian_weep")
            );
        } else {
            BLUE_DRIPPING_OBSIDIAN_WEEP = BLUE_DRIPPING_OBSIDIAN_TEAR;
            DRIPPING_OBSIDIAN_WEEP = BLUE_DRIPPING_OBSIDIAN_TEAR;

            BLUE_FALLING_OBSIDIAN_WEEP = BLUE_FALLING_OBSIDIAN_TEAR;
            FALLING_OBSIDIAN_WEEP = BLUE_FALLING_OBSIDIAN_TEAR;

            BLUE_LANDING_OBSIDIAN_WEEP = BLUE_LANDING_OBSIDIAN_TEAR;
            LANDING_OBSIDIAN_WEEP = BLUE_LANDING_OBSIDIAN_TEAR;
        }
    }

    public static void registerProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BLUE_DRIPPING_OBSIDIAN_TEAR, BNParticleProvider.ObsidianTearHangProvider::new);
        event.registerSpriteSet(BLUE_FALLING_OBSIDIAN_TEAR, BNParticleProvider.ObsidianTearFallProvider::new);
        event.registerSpriteSet(BLUE_LANDING_OBSIDIAN_TEAR, BNParticleProvider.ObsidianTearLandProvider::new);

        if (Configs.MAIN.getBoolean("particles", "weeping", true)) {
            event.registerSpriteSet(BLUE_DRIPPING_OBSIDIAN_WEEP, BNParticleProvider.ObsidianWeepHangProvider::new);
            event.registerSpriteSet(BLUE_FALLING_OBSIDIAN_WEEP, BNParticleProvider.ObsidianWeepFallProvider::new);
            event.registerSpriteSet(BLUE_LANDING_OBSIDIAN_WEEP, BNParticleProvider.ObsidianWeepLandProvider::new);

            event.registerSpriteSet(DRIPPING_OBSIDIAN_WEEP, BNParticleProvider.ObsidianVanillaWeepHangProvider::new);
            event.registerSpriteSet(FALLING_OBSIDIAN_WEEP, BNParticleProvider.ObsidianVanillaWeepFallProvider::new);
            event.registerSpriteSet(LANDING_OBSIDIAN_WEEP, BNParticleProvider.ObsidianVanillaWeepLandProvider::new);
        }
    }
}
