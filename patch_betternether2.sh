#!/bin/bash
sed -i '/import net.neoforged.fml.common.Mod;/d' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/import net.minecraft.core.registries.Registries;/import net.minecraft.core.registries.Registries;\nimport net.neoforged.fml.common.Mod;/g' src/main/java/org/betterx/betternether/BetterNether.java
