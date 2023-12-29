package com.cerbon.better_beacons;

import com.cerbon.better_beacons.config.BBClientConfigs;
import com.cerbon.better_beacons.config.BBCommonConfigs;
import com.cerbon.better_beacons.effect.BBEffects;
import com.cerbon.better_beacons.menu.BBMenuTypes;
import com.cerbon.better_beacons.util.BBConstants;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BBConstants.MOD_ID)
public class BetterBeacons {
    public static final Logger LOGGER = LogUtils.getLogger();

    public BetterBeacons() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BBMenuTypes.register(modEventBus);
        BBEffects.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BBClientConfigs.SPEC, BBConstants.MOD_ID + "/" + BBConstants.CLIENT_CONFIG_NAME);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BBCommonConfigs.SPEC, BBConstants.MOD_ID + "/" + BBConstants.COMMON_CONFIG_NAME);
    }
}
