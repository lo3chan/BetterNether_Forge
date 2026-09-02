package org.betterx.datagen.betternether.worldgen;

import org.betterx.bclib.api.v3.datagen.RegistriesDataProvider;
import org.betterx.betternether.BetterNether;
import org.betterx.datagen.betternether.NetherRegistrySupplier;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class NetherRegistriesDataProvider extends RegistriesDataProvider {
    public NetherRegistriesDataProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(BetterNether.LOGGER, NetherRegistrySupplier.INSTANCE, output, registriesFuture);
    }
}
