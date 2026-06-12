package org.clockwork.neogeckojs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = GeckoJS.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = GeckoJS.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GeckoJSClient {
    public GeckoJSClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        GeckoJS.LOGGER.info("HELLO FROM CLIENT SETUP");
        GeckoJS.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        event.enqueueWork(() -> {
            GeckoJS.REGISTERED_SHIELDS.forEach(id -> {
                ItemProperties.register(
                        BuiltInRegistries.ITEM.get(id),
                        ResourceLocation.withDefaultNamespace("blocking"),
                        (stack, world, living, itemId) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F
                );
            });
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        GeckoJS.REGISTERED_BLOCKS.forEach((id, model) -> {
            BlockEntityType<?> type = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(id);
            if (type != null) {
                //noinspection unchecked,rawtypes
                event.registerBlockEntityRenderer((BlockEntityType) type, context -> new AnimatableBlockEntityRenderer(model));
            }
        });
    }
}
