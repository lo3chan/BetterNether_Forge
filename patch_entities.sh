#!/bin/bash
sed -i 's/import org.betterx.bclib.entity.DespawnableAnimal;/import net.minecraft.world.entity.PathfinderMob;/g' src/main/java/org/betterx/betternether/entity/EntityFirefly.java
sed -i 's/public class EntityFirefly extends DespawnableAnimal/public class EntityFirefly extends PathfinderMob/g' src/main/java/org/betterx/betternether/entity/EntityFirefly.java
sed -i 's/public EntityFirefly(EntityType<? extends EntityFirefly> type, Level world)/public EntityFirefly(EntityType<? extends PathfinderMob> type, Level world)/g' src/main/java/org/betterx/betternether/entity/EntityFirefly.java

sed -i 's/import org.betterx.bclib.entity.DespawnableAnimal;/import net.minecraft.world.entity.PathfinderMob;/g' src/main/java/org/betterx/betternether/entity/EntityHydrogenJellyfish.java
sed -i 's/public class EntityHydrogenJellyfish extends DespawnableAnimal/public class EntityHydrogenJellyfish extends PathfinderMob/g' src/main/java/org/betterx/betternether/entity/EntityHydrogenJellyfish.java
sed -i 's/public EntityHydrogenJellyfish(EntityType<? extends EntityHydrogenJellyfish> type, Level world)/public EntityHydrogenJellyfish(EntityType<? extends PathfinderMob> type, Level world)/g' src/main/java/org/betterx/betternether/entity/EntityHydrogenJellyfish.java

sed -i 's/import org.betterx.bclib.entity.DespawnableAnimal;/import net.minecraft.world.entity.PathfinderMob;/g' src/main/java/org/betterx/betternether/entity/EntityFlyingPig.java
sed -i 's/public class EntityFlyingPig extends DespawnableAnimal/public class EntityFlyingPig extends PathfinderMob/g' src/main/java/org/betterx/betternether/entity/EntityFlyingPig.java
sed -i 's/public EntityFlyingPig(EntityType<? extends EntityFlyingPig> type, Level world)/public EntityFlyingPig(EntityType<? extends PathfinderMob> type, Level world)/g' src/main/java/org/betterx/betternether/entity/EntityFlyingPig.java