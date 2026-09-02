package org.betterx.betternether.recipes;

import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.betternether.BN;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class RecipesHelper {
    private static final String[] SHAPE_ROOF = new String[]{"# #", "###", " # "};
    private static final String[] SHAPE_STAIR = new String[]{"#  ", "## ", "###"};
    private static final String[] SHAPE_SLAB = new String[]{"###"};
    private static final String[] SHAPE_BUTTON = new String[]{"#"};
    private static final String[] SHAPE_PLATE = new String[]{"##"};
    private static final String[] SHAPE_X2 = new String[]{"##", "##"};
    private static final String[] SHAPE_3X2 = new String[]{"###", "###"};
    private static final String[] SHAPE_COLORING = new String[]{"###", "#I#", "###"};
    private static final String[] SHAPE_ROUND = new String[]{"###", "# #", "###"};
    private static final String[] SHAPE_FIRE_BOWL = new String[]{"#I#", " # ", "L L"};

    public interface RecipeProvider extends Consumer<Templates> {
    }

    private static final List<RecipeProvider> RECIPES = new LinkedList<>();

    public static void provideRecipes(Templates templates) {
        RECIPES.forEach(recipe -> recipe.accept(templates));
    }

    public static void addProvider(RecipeProvider provider) {
        RECIPES.add(provider);
    }

    public static void makeRoofRecipe(Block source, Block roof) {
        RECIPES.add(t -> t.makeRoofRecipe(source, roof));
    }

    public static void makeStairsRecipe(Block source, Block stairs) {
        RECIPES.add(t -> t.makeStairsRecipe(source, stairs));
    }


    public static void makeSlabRecipe(Block source, Block slab) {
        RECIPES.add(t -> t.makeSlabRecipe(source, slab));
    }

    public static void makeButtonRecipe(Block source, Block button) {
        RECIPES.add(t -> t.makeButtonRecipe(source, button));
    }

    public static void makePlateRecipe(Block source, Block plate) {
        RECIPES.add(t -> t.makePlateRecipe(source, plate));
    }

    public static void makeSimpleRecipe2(Block source, Block result, int count, String group, RecipeCategory category) {
        RECIPES.add(t -> t.makeSimpleRecipe2x2(source, result, count, group, category));
    }

    public static void makeWallRecipe(Block source, Block wall) {
        RECIPES.add(t -> t.makeWallRecipe(source, wall));
    }

    public static void makeColoringRecipe(Block source, Block result, Item dye, String group, RecipeCategory category) {
        RECIPES.add(t -> t.makeColoringRecipe(source, result, dye, group, category));
    }

    public static void makeRoundRecipe(Block source, Block result, String group, RecipeCategory category) {
        RECIPES.add(t -> t.makeRoundRecipe(source, result, group, category));
    }

    public static void makeFireBowlRecipe(Block material, Block inside, Item leg, Block result) {
        if (material == null || inside == null || leg == null || result == null) {
            return;
        }
        RECIPES.add(t -> t.makeFireBowlRecipe(material, inside, leg, result));
    }

    public static class Templates {
        private void makeSingleRecipe(
                String group,
                Block source,
                Block result,
                String[] shape,
                int count,
                RecipeCategory category
        ) {
            if (BuiltInRegistries.BLOCK.getKey(source) != BuiltInRegistries.BLOCK.getDefaultKey()) {
                String name = BuiltInRegistries.BLOCK.getKey(source)
                                                     .getPath() + "_" + BuiltInRegistries.BLOCK.getKey(result).getPath();

                BCLRecipeBuilder
                        .crafting(BN.id(name), result)
                        .setOutputCount(count)
                        .setGroup(group)
                        .setCategory(category)
                        .setShape(shape)
                        .addMaterial('#', source)
                        .build();
            }
        }

        public void makeRoofRecipe(Block source, Block roof) {
            makeSingleRecipe("roof_tile", source, roof, SHAPE_ROOF, 6, RecipeCategory.BUILDING_BLOCKS);
        }

        public void makeStairsRecipe(Block source, Block stairs) {
            String name = BuiltInRegistries.BLOCK.getKey(stairs)
                                                 .getPath();
            String group = BuiltInRegistries.BLOCK.getKey(stairs).getPath().contains("roof_tile")
                    ? "roof_tile_stairs"
                    : "nether_rock_stairs";
            makeSingleRecipe(group, source, stairs, SHAPE_STAIR, 4, RecipeCategory.BUILDING_BLOCKS);
            BCLRecipeBuilder.stonecutting(BN.id(name + "_stonecutting"), stairs)
                            .setOutputCount(1)
                            .setPrimaryInputAndUnlock(source)
                            .setCategory(RecipeCategory.BUILDING_BLOCKS)
                            .setGroup(group)
                            .build();
        }

        public void makeSlabRecipe(Block source, Block slab) {
            String name = BuiltInRegistries.BLOCK.getKey(slab)
                                                 .getPath();
            String group = BuiltInRegistries.BLOCK.getKey(slab).getPath().contains("roof_tile")
                    ? "roof_tile_slab"
                    : "nether_rock_slab";
            makeSingleRecipe(group, source, slab, SHAPE_SLAB, 6, RecipeCategory.BUILDING_BLOCKS);
            BCLRecipeBuilder.stonecutting(BN.id(name + "_stonecutting"), slab)
                            .setOutputCount(2)
                            .setPrimaryInputAndUnlock(source)
                            .setCategory(RecipeCategory.BUILDING_BLOCKS)
                            .setGroup(group)
                            .build();
        }

        public void makeButtonRecipe(Block source, Block button) {
            String group = "nether_rock_plate";
            makeSingleRecipe(group, source, button, SHAPE_BUTTON, 1, RecipeCategory.REDSTONE);
        }

        public void makePlateRecipe(Block source, Block plate) {
            String group = "nether_rock_plate";
            makeSingleRecipe(group, source, plate, SHAPE_PLATE, 1, RecipeCategory.REDSTONE);
        }

        public void makeSimpleRecipe2x2(Block source, Block result, int count, String group, RecipeCategory category) {
            makeSingleRecipe(group, source, result, SHAPE_X2, count, category);
        }

        public void makeWallRecipe(Block source, Block wall) {
            if (BuiltInRegistries.BLOCK.getKey(source) != BuiltInRegistries.BLOCK.getDefaultKey()) {
                String name = BuiltInRegistries.BLOCK.getKey(wall).getPath();

                BCLRecipeBuilder
                        .crafting(BN.id(name), wall)
                        .setOutputCount(6)
                        .setGroup("nether_wall")
                        .setShape(SHAPE_3X2)
                        .setCategory(RecipeCategory.DECORATIONS)
                        .addMaterial('#', source)
                        .build();

                BCLRecipeBuilder.stonecutting(BN.id(name + "_stonecutting"), wall)
                                .setOutputCount(1)
                                .setPrimaryInputAndUnlock(source)
                                .setCategory(RecipeCategory.BUILDING_BLOCKS)
                                .setGroup("nether_wall")
                                .build();
            }
        }

        public void makeColoringRecipe(Block source, Block result, Item dye, String group, RecipeCategory category) {
            if (BuiltInRegistries.BLOCK.getKey(source) != BuiltInRegistries.BLOCK.getDefaultKey()) {
                String name = BuiltInRegistries.BLOCK.getKey(result).getPath();

                BCLRecipeBuilder
                        .crafting(BN.id(name), result)
                        .setOutputCount(8)
                        .setGroup(group)
                        .setCategory(category)
                        .setShape(SHAPE_COLORING)
                        .addMaterial('#', source)
                        .addMaterial('I', dye)
                        .build();
            }
        }

        public void makeRoundRecipe(Block source, Block result, String group, RecipeCategory category) {
            if (BuiltInRegistries.BLOCK.getKey(source) != BuiltInRegistries.BLOCK.getDefaultKey()) {
                String name = BuiltInRegistries.BLOCK.getKey(result).getPath();

                BCLRecipeBuilder
                        .crafting(BN.id(name), result)
                        .setGroup(group)
                        .setCategory(category)
                        .setShape(SHAPE_ROUND)
                        .addMaterial('#', source)
                        .build();
            }
        }

        public void makeFireBowlRecipe(Block material, Block inside, Item leg, Block result) {
            if (BuiltInRegistries.BLOCK.getKey(material) != BuiltInRegistries.BLOCK.getDefaultKey()
                    && BuiltInRegistries.BLOCK.getKey(inside) != BuiltInRegistries.BLOCK.getDefaultKey()
                    && BuiltInRegistries.ITEM.getKey(leg) != BuiltInRegistries.ITEM.getDefaultKey()) {
                String name = BuiltInRegistries.BLOCK.getKey(result).getPath();

                BCLRecipeBuilder
                        .crafting(BN.id(name), result)
                        .setGroup("fire_bowl")
                        .setShape(SHAPE_FIRE_BOWL)
                        .addMaterial('#', material)
                        .addMaterial('I', inside)
                        .addMaterial('L', leg)
                        .setCategory(RecipeCategory.DECORATIONS)
                        .build();
            }
        }
    }
}
