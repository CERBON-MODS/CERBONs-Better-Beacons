package com.cerbon.better_beacons.event;

import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.better_beacons.util.json.BeaconBaseBlocksAmplifierManager;
import com.cerbon.better_beacons.util.json.BeaconPaymentItemsRangeManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BBConstants.MOD_ID)
public class ForgeEvents {

    @SubscribeEvent
    public static void registerDatapackListener(final AddReloadListenerEvent event) {
        event.addListener(BeaconPaymentItemsRangeManager.getInstance());
        event.addListener(BeaconBaseBlocksAmplifierManager.getInstance());
    }
}
