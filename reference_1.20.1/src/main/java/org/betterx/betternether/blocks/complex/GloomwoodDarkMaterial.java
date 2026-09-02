package org.betterx.betternether.blocks.complex;

import org.betterx.bclib.complexmaterials.WoodenComplexMaterial;
import org.betterx.bclib.complexmaterials.entry.SlotMap;
import org.betterx.bclib.complexmaterials.set.wood.WoodSlots;

import net.minecraft.world.level.material.MapColor;

public class GloomwoodDarkMaterial extends NetherWoodenMaterial<GloomwoodDarkMaterial> {
    public GloomwoodDarkMaterial() {
        super("gloomwood_dark", MapColor.COLOR_BLUE, MapColor.COLOR_BLUE);
    }

    @Override
    protected SlotMap<WoodenComplexMaterial> createMaterialSlots() {
        return super.createMaterialSlots()
                    .remove(WoodSlots.BOAT)
                    .remove(WoodSlots.CHEST_BOAT);
    }
}
