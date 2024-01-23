package com.cerbon.better_beacons.fabric.event;

import com.cerbon.better_beacons.client.screen.NewBeaconScreen;
import com.cerbon.better_beacons.menu.BBMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;

public class BBClientEventsFabric {

    public static void register() {
        attachScreensToMenus();
    }

    private static void attachScreensToMenus() {
        MenuScreens.register(BBMenuTypes.NEW_BEACON_MENU.get(), NewBeaconScreen::new);
    }
}
