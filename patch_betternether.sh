#!/bin/bash
sed -i 's/net.minecraftforge.fml.common.Mod/net.neoforged.fml.common.Mod/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext/net.neoforged.fml.common.Mod/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/FMLJavaModLoadingContext.get().getModEventBus()/modBus/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/public BetterNether()/public BetterNether(IEventBus modBus)/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/IEventBus modBus = modBus;//g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent/net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/net.minecraftforge.eventbus.api.EventPriority/net.neoforged.bus.api.EventPriority/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/net.minecraftforge.eventbus.api.IEventBus/net.neoforged.bus.api.IEventBus/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/net.minecraftforge.registries.RegisterEvent/net.neoforged.neoforge.registries.RegisterEvent/g' src/main/java/org/betterx/betternether/BetterNether.java
