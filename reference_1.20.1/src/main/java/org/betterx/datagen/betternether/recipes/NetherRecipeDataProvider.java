package org.betterx.datagen.betternether.recipes;

import org.betterx.bclib.api.v3.datagen.RecipeDataProvider;
import org.betterx.betternether.BetterNether;
import org.betterx.betternether.recipes.BlockRecipes;
import org.betterx.betternether.recipes.ItemRecipes;
import org.betterx.betternether.recipes.RecipesHelper;

import net.minecraft.data.PackOutput;

import java.util.List;

public class NetherRecipeDataProvider extends RecipeDataProvider {
    public NetherRecipeDataProvider(PackOutput output) {
        super(List.of(BetterNether.MOD_ID), output);
    }

    public static void buildRecipes() {
        RecipeDataProvider.defer(NetherRecipeDataProvider::registerRecipes);
    }

    private static void registerRecipes() {
        BlockRecipes.register();
        ItemRecipes.register();
        RecipesHelper.provideRecipes(new RecipesHelper.Templates());
    }
}
