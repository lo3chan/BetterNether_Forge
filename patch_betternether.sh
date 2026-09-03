sed -i 's/NetherEntities::onRegister/NetherEntities.ENTITIES.register(modBus)/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/NetherParticles::onRegister/NetherParticles.PARTICLES.register(modBus)/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/NetherPoiTypes::onRegister/NetherPoiTypes.POI_TYPES.register(modBus)/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/NetherFeatures::onRegister/NetherFeatures.FEATURES.register(modBus)/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/modBus.addListener((RegisterEvent event) -> BlockEntitiesRegistry.register(event));/BlockEntitiesRegistry.BLOCK_ENTITIES.register(modBus);/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/modBus.addListener(NetherTemplates::register);/NetherTemplates.register(modBus);/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/modBus.addListener(EventPriority.HIGHEST, this::ensureBlocksLoaded);/NetherBlocks.BLOCKS.register(modBus);/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/modBus.addListener(EventPriority.HIGHEST, this::ensureItemsLoaded);/NetherItems.ITEMS.register(modBus);/g' src/main/java/org/betterx/betternether/BetterNether.java
sed -i '/modBus.addListener(EventPriority.HIGHEST, this::ensureStructuresLoaded);/d' src/main/java/org/betterx/betternether/BetterNether.java
sed -i 's/NetherStructures.register();/NetherStructures.STRUCTURES.register(modBus);/g' src/main/java/org/betterx/betternether/BetterNether.java
