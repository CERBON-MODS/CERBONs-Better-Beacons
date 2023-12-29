package com.cerbon.better_beacons.event;

import com.cerbon.better_beacons.client.gui.screen.inventory.NewBeaconScreen;
import com.cerbon.better_beacons.menu.BBMenuTypes;
import com.cerbon.better_beacons.util.BBConstants;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BBConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BBClientEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(BBMenuTypes.NEW_BEACON_MENU.get(), NewBeaconScreen::new));
    }
}
