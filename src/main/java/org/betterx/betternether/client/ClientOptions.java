package org.betterx.betternether.client;

import org.betterx.betternether.config.Configs;

public class ClientOptions {
    private static boolean initialized;
    private static boolean blendBiomeMusic;

    public static void init() {
        if (initialized) {
            return;
        }

        blendBiomeMusic = Configs.CLIENT_CONFIG.getBooleanRoot("blendBiomeMusic", true);
        Configs.CLIENT_CONFIG.saveChanges();
        initialized = true;
    }

    private static void ensureInit() {
        if (!initialized) {
            init();
        }
    }

    public static boolean blendBiomeMusic() {
        ensureInit();
        return blendBiomeMusic;
    }

    public static void setBlendBiomeMusic(boolean blendBiomeMusic) {
        ensureInit();
        ClientOptions.blendBiomeMusic = blendBiomeMusic;
    }
}
