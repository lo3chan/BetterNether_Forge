package org.betterx.betternether.client;

import org.betterx.betternether.BetterNether;
import org.betterx.betternether.blocks.BNRenderLayer;
import org.betterx.betternether.config.screen.ConfigScreen;
import org.betterx.betternether.registry.NetherParticles;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = BetterNether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BetterNetherClient {
    private BetterNetherClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientOptions.init();
            registerRenderLayers();
            ModLoadingContext.get()
                             .registerExtensionPoint(
                                     ConfigScreenHandler.ConfigScreenFactory.class,
                                     () -> new ConfigScreenHandler.ConfigScreenFactory(
                                             (client, parent) -> new ConfigScreen(parent)
                                     )
                             );
        });
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        NetherParticles.registerProviders(event);
    }

    private static void registerRenderLayers() {
        RenderType cutout = RenderType.cutout();
        RenderType translucent = RenderType.translucent();
        BuiltInRegistries.BLOCK.forEach(block -> {
            if (block instanceof IRenderTypeable) {
                BNRenderLayer layer = ((IRenderTypeable) block).getRenderLayer();
                if (layer == BNRenderLayer.CUTOUT)
                    ItemBlockRenderTypes.setRenderLayer(block, cutout);
                else if (layer == BNRenderLayer.TRANSLUCENT)
                    ItemBlockRenderTypes.setRenderLayer(block, translucent);
            }
        });
    }
}
