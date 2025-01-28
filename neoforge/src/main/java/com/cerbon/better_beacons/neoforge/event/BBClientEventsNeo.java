package com.cerbon.better_beacons.neoforge.event;

import com.cerbon.better_beacons.client.screen.NewBeaconScreen;
import com.cerbon.better_beacons.config.BBConfig;
import com.cerbon.better_beacons.menu.BBMenuTypes;
import com.cerbon.better_beacons.util.BBConstants;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = BBConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BBClientEventsNeo {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (modContainer, screen) -> AutoConfig.getConfigScreen(BBConfig.class, screen).get());
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(BBMenuTypes.NEW_BEACON_MENU.get(), NewBeaconScreen::new);
    }
}
