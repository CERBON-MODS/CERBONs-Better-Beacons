package com.cerbon.better_beacons;

import com.cerbon.better_beacons.screen.BBMenuTypes;
import com.cerbon.better_beacons.screen.BBNewBeaconScreen;
import com.cerbon.better_beacons.util.BBConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BBConstants.MOD_ID)
public class BetterBeacons {
    public static final Logger LOGGER = LogUtils.getLogger();

    public BetterBeacons() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        BBMenuTypes.register(modEventBus);

        modEventBus.addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event){
        MenuScreens.register(BBMenuTypes.NEW_BEACON_MENU.get(), BBNewBeaconScreen::new);
    }
}
