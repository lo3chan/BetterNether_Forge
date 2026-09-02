package org.betterx.betternether.registry;

import org.betterx.betternether.BetterNether;
import org.betterx.betternether.enchantments.ObsidianBreaker;
import org.betterx.betternether.enchantments.RubyFire;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

import java.util.LinkedHashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = BetterNether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetherEnchantments {
    private static final Map<ResourceLocation, Enchantment> ENCHANTMENTS = new LinkedHashMap<>();

    public static ObsidianBreaker OBSIDIAN_BREAKER = register("obsidian_breaker", new ObsidianBreaker());
    public static RubyFire RUBY_FIRE = register("ruby_fire", new RubyFire());

    private static <T extends Enchantment> T register(String name, T enchantment) {
        ENCHANTMENTS.putIfAbsent(BetterNether.makeID(name), enchantment);
        return enchantment;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.ENCHANTMENT)) {
            return;
        }
        event.register(Registries.ENCHANTMENT, helper -> {
            ENCHANTMENTS.forEach(helper::register);
            ENCHANTMENTS.clear();
        });
    }
}
