package org.betterx.betternether.mixin.common;

import org.betterx.betternether.recipes.BNRecipeManager;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

import net.minecraftforge.fml.ModList;

import com.google.gson.JsonObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(method = "fromJson", at = @At(value = "HEAD"), cancellable = true)
    private static void be_checkMissing(ResourceLocation id, JsonObject json, CallbackInfoReturnable<Recipe<?>> info) {
        if (id.getNamespace().equals("techreborn") && !ModList.get().isLoaded("techreborn")) {
            info.setReturnValue(BNRecipeManager.makeEmptyRecipe(id));
            info.cancel();
        }
    }
}
