package com.cerbon.better_beacons.forge.event;

import com.cerbon.better_beacons.client.screen.NewBeaconScreen;
import com.cerbon.better_beacons.config.BBConfig;
import com.cerbon.better_beacons.menu.BBMenuTypes;
import com.cerbon.better_beacons.util.BBConstants;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BBConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BBClientEventsForge {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> AutoConfig.getConfigScreen(BBConfig.class, parent).get()));
        MenuScreens.register(BBMenuTypes.NEW_BEACON_MENU.get(), NewBeaconScreen::new);
    }
}