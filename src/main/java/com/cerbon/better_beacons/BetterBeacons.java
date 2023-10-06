package com.cerbon.better_beacons;

import com.cerbon.better_beacons.advancement.condition.IsTertiaryEffectEnabledCondition;
import com.cerbon.better_beacons.client.gui.screen.inventory.NewBeaconScreen;
import com.cerbon.better_beacons.config.BBClientConfigs;
import com.cerbon.better_beacons.config.BBCommonConfigs;
import com.cerbon.better_beacons.effect.BBEffects;
import com.cerbon.better_beacons.menu.BBMenuTypes;
import com.cerbon.better_beacons.packet.BBPacketHandler;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.better_beacons.util.json.BeaconBaseBlocksAmplifierManager;
import com.cerbon.better_beacons.util.json.BeaconPaymentItemsRangeManager;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
        modEventBus.addListener(this::onCommonSetup);

        BBMenuTypes.register(modEventBus);
        BBEffects.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BBClientConfigs.SPEC, BBConstants.MOD_ID + "/" + BBConstants.CLIENT_CONFIG_NAME);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BBCommonConfigs.SPEC, BBConstants.MOD_ID + "/" + BBConstants.COMMON_CONFIG_NAME);
    }

    private void onClientSetup(FMLClientSetupEvent event){
        event.enqueueWork(() -> MenuScreens.register(BBMenuTypes.NEW_BEACON_MENU.get(), NewBeaconScreen::new));
    }

    private void onCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(() -> CraftingHelper.register(IsTertiaryEffectEnabledCondition.Serializer.INSTANCE));
        BBPacketHandler.register();
    }

    private void registerDatapackListener(final AddReloadListenerEvent event){
        event.addListener(BeaconPaymentItemsRangeManager.getInstance());
        event.addListener(BeaconBaseBlocksAmplifierManager.getInstance());
    }
}
