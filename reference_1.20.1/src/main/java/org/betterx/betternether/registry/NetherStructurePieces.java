package org.betterx.betternether.registry;

import org.betterx.betternether.BetterNether;
import org.betterx.betternether.world.structures.piece.CavePiece;
import org.betterx.betternether.world.structures.piece.CityPiece;
import org.betterx.betternether.world.structures.piece.DestructionPiece;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

import java.util.LinkedHashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = BetterNether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetherStructurePieces {
    private static final Map<ResourceLocation, StructurePieceType> TYPES = new LinkedHashMap<>();

    public static final StructurePieceType NETHER_CITY_PIECE = register("bncity", CityPiece::new);
    public static final StructurePieceType CAVE_PIECE = register("bncave", CavePiece::new);
    public static final StructurePieceType DESTRUCTION_PIECE = register("bndestr", DestructionPiece::new);
    public static final StructurePieceType ANCHOR_TREE_PIECE = register("anchor_tree", DestructionPiece::new);

    private static StructurePieceType register(String id, StructurePieceType pieceType) {
        ResourceLocation key = BetterNether.makeID(id);
        StructurePieceType existing = TYPES.get(key);
        if (existing != null) {
            return existing;
        }
        TYPES.put(key, pieceType);
        return pieceType;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.STRUCTURE_PIECE)) {
            return;
        }
        event.register(Registries.STRUCTURE_PIECE, helper -> {
            TYPES.forEach(helper::register);
            TYPES.clear();
        });
    }

    public static void ensureStaticLoad() {

    }
}
