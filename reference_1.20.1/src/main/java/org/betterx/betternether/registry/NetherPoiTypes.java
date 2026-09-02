package org.betterx.betternether.registry;

import org.betterx.bclib.api.v2.poi.BCLPoiType;
import org.betterx.bclib.api.v2.poi.PoiManager;
import org.betterx.betternether.BetterNether;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

import net.minecraftforge.registries.RegisterEvent;

import java.util.HashSet;

public class NetherPoiTypes {
    public static BCLPoiType PIG_STATUE;

    public static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.POINT_OF_INTEREST_TYPE)) {
            register();
        }
    }

    public static void register() {
        if (PIG_STATUE != null || NetherBlocks.PIG_STATUE_RESPAWNER == null) {
            return;
        }
        var states = new HashSet<>(BCLPoiType.getBlockStates(NetherBlocks.PIG_STATUE_RESPAWNER));
        states.removeIf(PoiTypes::hasPoi);
        if (states.isEmpty()) {
            return;
        }
        try {
            PIG_STATUE = PoiManager.register(
                    BetterNether.makeID("pig_statue"),
                    states,
                    1,
                    1
            );
        } catch (IllegalStateException ex) {
            BetterNether.LOGGER.warning("Skip pig_statue POI registration: " + ex.getMessage());
        }
    }
}
