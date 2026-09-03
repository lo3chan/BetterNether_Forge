package org.betterx.betternether.registry;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.entity.BCLEntityWrapper;
import org.betterx.bclib.interfaces.SpawnRule;
import org.betterx.betternether.BetterNether;
import org.betterx.betternether.config.Configs;
import org.betterx.betternether.entity.*;
import org.betterx.betternether.world.NetherBiomeConfig;
import org.betterx.ui.ColorUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.AABB;

import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent.Operation;
import net.neoforged.neoforge.eventbus.api.SubscribeEvent;
import net.neoforged.neoforge.fml.common.Mod;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = BetterNether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetherEntities {
    public enum KnownSpawnTypes {
        GHAST(50, 4, 4, EntityType.GHAST),
        ZOMBIFIED_PIGLIN(100, 4, 4, EntityType.ZOMBIFIED_PIGLIN),
        MAGMA_CUBE(2, 4, 4, EntityType.MAGMA_CUBE),
        SKULL(2, 2, 4, NetherEntities.SKULL),
        ENDERMAN(1, 4, 4, EntityType.ENDERMAN),
        PIGLIN(15, 4, 4, EntityType.PIGLIN),
        STRIDER(60, 1, 2, EntityType.STRIDER),
        HOGLIN(9, 1, 2, EntityType.HOGLIN),
        FIREFLY(5, 1, 3, NetherEntities.FIREFLY),
        HYDROGEN_JELLYFISH(5, 2, 6, NetherEntities.HYDROGEN_JELLYFISH),
        NAGA(8, 3, 5, NetherEntities.NAGA),
        FLYING_PIG(20, 2, 4, NetherEntities.FLYING_PIG),
        JUNGLE_SKELETON(40, 2, 4, NetherEntities.JUNGLE_SKELETON),
        PIGLIN_BRUTE(0, 1, 1, EntityType.PIGLIN_BRUTE);

        public final int weight;
        public final int minGroupSize;
        public final int maxGroupSize;
        public EntityType type;
        public BCLEntityWrapper wrapper;

        public boolean isVanilla() {
            return wrapper == null;
        }

        public void addSpawn(BCLBiomeBuilder builder, NetherBiomeConfig data) {
            final String category = data.configGroup() + ".spawn." + this.type.getCategory()
                                                                              .getName() + "." + this.type
                    .getDescriptionId()
                    .replace(
                            "entity.",
                            ""
                    );
            int weight = Configs.BIOMES.getInt(category, "weight", data.spawnWeight(this));
            int min = Configs.BIOMES.getInt(category, "minGroupSize", minGroupSize);
            int max = Configs.BIOMES.getInt(category, "maxGroupSize", maxGroupSize);

            if (wrapper == null) {
                builder.spawn(this.type, weight, min, max);
            } else {
                builder.spawn(this.wrapper, weight, min, max);
            }
        }

        public void addSpawn(ResourceLocation ID, Holder<Biome> biome, float multiplier) {
            final String category = ID.getNamespace() + "." + ID.getPath() + ".spawn." + this.type.getCategory()
                                                                                                  .getName() + "." + this.type
                    .getDescriptionId()
                    .replace(
                            "entity.",
                            ""
                    );
            int dweight = Configs.BIOMES.getInt(category, "weight", (int) (weight * multiplier));
            int min = Configs.BIOMES.getInt(category, "minGroupSize", minGroupSize);
            int max = Configs.BIOMES.getInt(category, "maxGroupSize", maxGroupSize);

            BiomeAPI.addBiomeMobSpawn(biome, this.type, dweight, min, max);
        }

        public void addSpawn(ResourceLocation ID, Holder<Biome> biome) {
            addSpawn(ID, biome, 1);
        }

        KnownSpawnTypes(int w, int min, int max, EntityType type) {
            weight = w;
            minGroupSize = min;
            maxGroupSize = max;
            this.type = type;
            this.wrapper = null;
        }

        <M extends Mob> KnownSpawnTypes(int w, int min, int max, BCLEntityWrapper type) {
            weight = w;
            minGroupSize = min;
            maxGroupSize = max;
            this.type = type == null ? null : type.type();
            this.wrapper = type;
        }

        void setWrapper(BCLEntityWrapper wrapper) {
            this.wrapper = wrapper;
            if (wrapper != null) {
                this.type = wrapper.type();
            }
        }
    }

    public static final Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> ATTR_BUILDERS = Maps.newHashMap();
    private static final List<BCLEntityWrapper<?>> NETHER_ENTITIES = Lists.newArrayList();

    private static boolean spawnEggsRegistered;
    public static EntityType<EntityNagaProjectile> NAGA_PROJECTILE;

    public static BCLEntityWrapper<EntityFirefly> FIREFLY;

    public static BCLEntityWrapper<EntityHydrogenJellyfish> HYDROGEN_JELLYFISH;

    public static BCLEntityWrapper<EntityNaga> NAGA;

    public static BCLEntityWrapper<EntityFlyingPig> FLYING_PIG;

    public static BCLEntityWrapper<EntityJungleSkeleton> JUNGLE_SKELETON;

    public static BCLEntityWrapper<EntitySkull> SKULL;


    private static <T extends Mob> BCLEntityWrapper<T> register(
            String name,
            MobCategory group,
            float width,
            float height,
            EntityFactory<T> entity,
            Builder attributes
    ) {
        ResourceLocation id = BetterNether.makeID(name);
        EntityType<T> type = EntityType.Builder.of(entity, group)
                                               .sized(width, height)
                                               .fireImmune() //Nether Entities are by default immune to fire
                                               .build(id.toString());

        ATTR_BUILDERS.put(type, attributes);

        var wrapper = new BCLEntityWrapper<>(type, Configs.MOBS.getBooleanRoot(id.getPath(), true));
        if (!wrapper.canSpawn()) {
            NETHER_ENTITIES.add(wrapper);
        }
        return wrapper;
    }

    public static void registerSpawnEggs() {
        if (spawnEggsRegistered) {
            return;
        }
        spawnEggsRegistered = true;

        NetherItems.makeEgg(
                "spawn_egg_firefly",
                mobTypeSupplier("firefly"),
                ColorUtil.color(255, 223, 168),
                ColorUtil.color(233, 182, 95)
        );
        NetherItems.makeEgg(
                "spawn_egg_hydrogen_jellyfish",
                mobTypeSupplier("hydrogen_jellyfish"),
                ColorUtil.color(253, 164, 24),
                ColorUtil.color(88, 21, 4)
        );
        NetherItems.makeEgg(
                "spawn_egg_naga",
                mobTypeSupplier("naga"),
                ColorUtil.color(12, 12, 12),
                ColorUtil.color(210, 90, 26)
        );
        NetherItems.makeEgg(
                "spawn_egg_flying_pig",
                mobTypeSupplier("flying_pig"),
                ColorUtil.color(241, 140, 93),
                ColorUtil.color(176, 58, 47)
        );
        NetherItems.makeEgg(
                "spawn_egg_jungle_skeleton",
                mobTypeSupplier("jungle_skeleton"),
                ColorUtil.color(134, 162, 149),
                ColorUtil.color(6, 111, 79)
        );
        NetherItems.makeEgg(
                "spawn_egg_skull",
                mobTypeSupplier("skull"),
                ColorUtil.color(24, 19, 19),
                ColorUtil.color(255, 28, 18)
        );
    }

    private static Supplier<EntityType<? extends Mob>> mobTypeSupplier(String name) {
        return () -> (EntityType<? extends Mob>) Objects.requireNonNull(
                ForgeRegistries.ENTITY_TYPES.getValue(BetterNether.makeID(name)),
                "Missing entity type for spawn egg: " + name
        );
    }

    private static boolean testSpawnAboveLava(LevelAccessor world, BlockPos pos, boolean allow) {
        int h = org.betterx.bclib.util.BlocksHelper.downRay(world, pos, MAX_FLOAT_HEIGHT + 2);
        if (h > MAX_FLOAT_HEIGHT) return false;

        for (int i = 1; i <= h + 1; i++)
            if (org.betterx.bclib.util.BlocksHelper.isLava(world.getBlockState(pos.below(i))))
                return allow;

        return !allow;
    }

    public static final int MAX_FLOAT_HEIGHT = 7;
    public static final SpawnRule RULE_FLOAT_NOT_ABOVE_LAVA = (type, world, spawnReason, pos, random) -> testSpawnAboveLava(
            world,
            pos,
            false
    );
    public static final SpawnRule RULE_FLOAT_ABOVE_LAVA = (type, world, spawnReason, pos, random) -> testSpawnAboveLava(
            world,
            pos,
            true
    );

    private static boolean belowMaxHeight(LevelAccessor world, BlockPos pos) {
        return pos.getY() < world.dimensionType().logicalHeight();
    }

    private static boolean maxNearby(
            EntityType<?> entityType,
            LevelAccessor world,
            BlockPos pos,
            int count,
        int radius
    ) {
        AABB box = new AABB(pos).inflate(radius, world.getHeight(), radius);
        return world.getEntitiesOfClass(
                Entity.class,
                box,
                entity -> entity.getType() == entityType
        ).size() < count;
    }

    @SubscribeEvent
    public static void onRegisterSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                FIREFLY.type(),
                SpawnPlacements.Type.NO_RESTRICTIONS,
                Types.MOTION_BLOCKING_NO_LEAVES,
                (type, world, reason, pos, random) -> belowMaxHeight(world, pos)
                        && RULE_FLOAT_NOT_ABOVE_LAVA.canSpawn(type, world, reason, pos, random)
                        && maxNearby(type, world, pos, 32, 64),
                Operation.REPLACE
        );
        event.register(
                HYDROGEN_JELLYFISH.type(),
                SpawnPlacements.Type.NO_RESTRICTIONS,
                Types.MOTION_BLOCKING,
                (type, world, reason, pos, random) -> belowMaxHeight(world, pos)
                        && maxNearby(type, world, pos, 24, 64),
                Operation.REPLACE
        );
        event.register(
                NAGA.type(),
                SpawnPlacements.Type.ON_GROUND,
                Types.MOTION_BLOCKING_NO_LEAVES,
                (type, world, reason, pos, random) -> world.getDifficulty() != Difficulty.PEACEFUL
                        && world.getMaxLocalRawBrightness(pos) <= 8
                        && maxNearby(type, world, pos, 32, 64),
                Operation.REPLACE
        );
        event.register(
                FLYING_PIG.type(),
                SpawnPlacements.Type.NO_RESTRICTIONS,
                Types.MOTION_BLOCKING,
                (type, world, reason, pos, random) -> belowMaxHeight(world, pos)
                        && RULE_FLOAT_NOT_ABOVE_LAVA.canSpawn(type, world, reason, pos, random)
                        && maxNearby(type, world, pos, 16, 64),
                Operation.REPLACE
        );
        event.register(
                JUNGLE_SKELETON.type(),
                SpawnPlacements.Type.ON_GROUND,
                Types.MOTION_BLOCKING_NO_LEAVES,
                (type, world, reason, pos, random) -> world.getDifficulty() != Difficulty.PEACEFUL
                        && maxNearby(type, world, pos, 16, 64),
                Operation.REPLACE
        );
        event.register(
                SKULL.type(),
                SpawnPlacements.Type.NO_RESTRICTIONS,
                Types.MOTION_BLOCKING,
                (type, world, reason, pos, random) -> belowMaxHeight(world, pos)
                        && world.getDifficulty() != Difficulty.PEACEFUL
                        && world.getMaxLocalRawBrightness(pos) <= 7
                        && maxNearby(type, world, pos, 16, 64),
                Operation.REPLACE
        );
    }

    public static void registerEntity(String name, EntityType<? extends LivingEntity> entity) {
        registerEntity(name, entity, Mob.createMobAttributes());
    }

    public static void registerEntity(
            String name,
            EntityType<? extends LivingEntity> entity,
            AttributeSupplier.Builder builder
    ) {
        ATTR_BUILDERS.put(entity, builder);
    }

    public static boolean isNetherEntity(Entity entity) {
        return NETHER_ENTITIES.contains(entity.getType());
    }

    @SubscribeEvent
    public static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        ATTR_BUILDERS.forEach((type, builder) -> event.put(type, builder.build()));
    }

    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.ENTITY_TYPE)) return;
        event.register(Registries.ENTITY_TYPE, helper -> {
            NAGA_PROJECTILE = EntityType.Builder
                    .of(EntityNagaProjectile::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .noSummon()
                    .build(BetterNether.makeID("naga_projectile").toString());
            helper.register(BetterNether.makeID("naga_projectile"), NAGA_PROJECTILE);
            ATTR_BUILDERS.put(NAGA_PROJECTILE, Mob.createMobAttributes());

            FIREFLY = register(
                    "firefly",
                    MobCategory.AMBIENT,
                    0.5f,
                    0.5f,
                    EntityFirefly::new,
                    EntityFirefly.createMobAttributes()
            );
            helper.register(BetterNether.makeID("firefly"), FIREFLY.type());

            HYDROGEN_JELLYFISH = register(
                    "hydrogen_jellyfish",
                    MobCategory.AMBIENT,
                    2.0f,
                    5.0f,
                    EntityHydrogenJellyfish::new,
                    EntityHydrogenJellyfish.createMobAttributes()
            );
            helper.register(BetterNether.makeID("hydrogen_jellyfish"), HYDROGEN_JELLYFISH.type());

            NAGA = register(
                    "naga",
                    MobCategory.MONSTER,
                    0.625f,
                    2.75f,
                    EntityNaga::new,
                    EntityNaga.createMobAttributes()
            );
            helper.register(BetterNether.makeID("naga"), NAGA.type());

            FLYING_PIG = register(
                    "flying_pig",
                    MobCategory.AMBIENT,
                    1.0f,
                    1.25f,
                    EntityFlyingPig::new,
                    EntityFlyingPig.createMobAttributes()
            );
            helper.register(BetterNether.makeID("flying_pig"), FLYING_PIG.type());

            JUNGLE_SKELETON = register(
                    "jungle_skeleton",
                    MobCategory.MONSTER,
                    0.6F,
                    1.99F,
                    EntityJungleSkeleton::new,
                    EntityJungleSkeleton.createMonsterAttributes()
            );
            helper.register(BetterNether.makeID("jungle_skeleton"), JUNGLE_SKELETON.type());

            SKULL = register(
                    "skull",
                    MobCategory.MONSTER,
                    0.625F,
                    0.625F,
                    EntitySkull::new,
                    EntitySkull.createMobAttributes()
            );
            helper.register(BetterNether.makeID("skull"), SKULL.type());

            KnownSpawnTypes.SKULL.setWrapper(SKULL);
            KnownSpawnTypes.FIREFLY.setWrapper(FIREFLY);
            KnownSpawnTypes.HYDROGEN_JELLYFISH.setWrapper(HYDROGEN_JELLYFISH);
            KnownSpawnTypes.NAGA.setWrapper(NAGA);
            KnownSpawnTypes.FLYING_PIG.setWrapper(FLYING_PIG);
            KnownSpawnTypes.JUNGLE_SKELETON.setWrapper(JUNGLE_SKELETON);

        });
    }

    static void modifyNonBNBiome(ResourceLocation biomeID, Holder<Biome> biome) {
        final boolean isCrimson = biomeID.equals(Biomes.CRIMSON_FOREST.location());

        KnownSpawnTypes.FIREFLY.addSpawn(biomeID, biome, isCrimson ? 3 : 1);
        KnownSpawnTypes.HYDROGEN_JELLYFISH.addSpawn(biomeID, biome);
        KnownSpawnTypes.NAGA.addSpawn(biomeID, biome, isCrimson ? 0 : 1);

        synchronized (Configs.BIOMES) {
            Configs.BIOMES.saveChanges();
        }
    }
}
