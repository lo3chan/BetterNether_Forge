#!/bin/bash
file="src/main/java/org/betterx/betternether/registry/NetherEntities.java"

# Insert ENTITIES definition before onRegister
sed -i 's/public static void onRegister(RegisterEvent event)/public static final net.neoforged.neoforge.registries.DeferredRegister<EntityType<?>> ENTITIES = net.neoforged.neoforge.registries.DeferredRegister.create(Registries.ENTITY_TYPE, BetterNether.MOD_ID);\n\n    public static void register(net.neoforged.bus.api.IEventBus modBus) {\n        ENTITIES.register(modBus);\n    }\n\n    public static void onRegister(RegisterEvent event)/g' $file

# Replace entity registrations in onRegister
sed -i 's/helper.register(BetterNether.makeID("naga_projectile"), NAGA_PROJECTILE);/ENTITIES.register("naga_projectile", () -> NAGA_PROJECTILE);/g' $file
sed -i 's/helper.register(BetterNether.makeID("firefly"), FIREFLY.type());/ENTITIES.register("firefly", () -> FIREFLY.type());/g' $file
sed -i 's/helper.register(BetterNether.makeID("hydrogen_jellyfish"), HYDROGEN_JELLYFISH.type());/ENTITIES.register("hydrogen_jellyfish", () -> HYDROGEN_JELLYFISH.type());/g' $file
sed -i 's/helper.register(BetterNether.makeID("naga"), NAGA.type());/ENTITIES.register("naga", () -> NAGA.type());/g' $file
sed -i 's/helper.register(BetterNether.makeID("flying_pig"), FLYING_PIG.type());/ENTITIES.register("flying_pig", () -> FLYING_PIG.type());/g' $file
sed -i 's/helper.register(BetterNether.makeID("jungle_skeleton"), JUNGLE_SKELETON.type());/ENTITIES.register("jungle_skeleton", () -> JUNGLE_SKELETON.type());/g' $file
sed -i 's/helper.register(BetterNether.makeID("skull"), SKULL.type());/ENTITIES.register("skull", () -> SKULL.type());/g' $file

