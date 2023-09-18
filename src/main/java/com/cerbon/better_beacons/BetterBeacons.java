package com.cerbon.better_beacons;

import com.cerbon.better_beacons.config.BBCommonConfigs;
import com.cerbon.better_beacons.effect.BBEffects;
import com.cerbon.better_beacons.menu.BBMenuTypes;
import com.cerbon.better_beacons.client.gui.screen.inventory.NewBeaconScreen;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.better_beacons.util.json.BeaconPaymentItemsRangeManager;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BBConstants.MOD_ID)
public class BetterBeacons {
    public static final Logger LOGGER = LogUtils.getLogger();

    public BetterBeacons() {
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.addListener(this::registerDatapackListener);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onClientSetup);

        BBMenuTypes.register(modEventBus);
        BBEffects.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BBCommonConfigs.SPEC, BBConstants.COMMON_CONFIG_NAME);
    }

    private void onClientSetup(FMLClientSetupEvent event){
        MenuScreens.register(BBMenuTypes.NEW_BEACON_MENU.get(), NewBeaconScreen::new);
    }

    private void registerDatapackListener(final AddReloadListenerEvent event){
        event.addListener(BeaconPaymentItemsRangeManager.getInstance());
    }
}
