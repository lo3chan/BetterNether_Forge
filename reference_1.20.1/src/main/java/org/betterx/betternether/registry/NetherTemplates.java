package org.betterx.betternether.registry;

import org.betterx.bclib.recipes.SmithingTemplates;
import org.betterx.betternether.BetterNether;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;
import net.minecraftforge.registries.RegisterEvent;

public class NetherTemplates {
    public static final ResourceLocation EMPTY_SLOT_BOWL = BetterNether.makeID("item/empty_slot_bowl");
    public static final ResourceLocation EMPTY_SLOT_BLOCK = BetterNether.makeID("item/empty_slot_block");

    public static SmithingTemplateItem NETHER_BOWL_SMITHING_TEMPLATE;
    public static SmithingTemplateItem FLAMING_RUBY_TEMPLATE;
    public static SmithingTemplateItem CINCINNASITE_DIAMOND_TEMPLATE;

    public static void register(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.ITEM)) {
            return;
        }
        var registry = NetherItems.getItemRegistry();
        NETHER_BOWL_SMITHING_TEMPLATE = registry.registerSmithingTemplateItem(
                BetterNether.makeID("bowl_upgrade"),
                List.of(EMPTY_SLOT_BOWL),
                List.of(SmithingTemplates.EMPTY_SLOT_INGOT)
        );
        FLAMING_RUBY_TEMPLATE = registry.registerSmithingTemplateItem(
                BetterNether.makeID("flaming_ruby_upgrade"),
                SmithingTemplates.ARMOR_AND_TOOLS,
                List.of(EMPTY_SLOT_BLOCK)
        );
        CINCINNASITE_DIAMOND_TEMPLATE = registry.registerSmithingTemplateItem(
                BetterNether.makeID("cincinnasite_diamond_upgrade"),
                List.of(SmithingTemplates.EMPTY_SLOT_DIAMOND),
                List.of(SmithingTemplates.EMPTY_SLOT_INGOT)
        );
    }

    public static void ensureStaticallyLoaded() {
    }
}
