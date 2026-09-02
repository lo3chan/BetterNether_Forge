#!/bin/bash
find src/main/java -name "*.java" -exec sed -i 's/net.minecraftforge/net.neoforged.neoforge/g' {} +
