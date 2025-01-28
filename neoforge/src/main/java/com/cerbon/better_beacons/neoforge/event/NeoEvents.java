package com.cerbon.better_beacons.neoforge.event;

import com.cerbon.better_beacons.datapack.BaseBlocksAmplifierManager;
import com.cerbon.better_beacons.datapack.PaymentItemsRangeManager;
import com.cerbon.better_beacons.util.BBConstants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@EventBusSubscriber(modid = BBConstants.MOD_ID)
public class NeoEvents {

    @SubscribeEvent
    public static void registerDatapacks(AddReloadListenerEvent event) {
        event.addListener(BaseBlocksAmplifierManager.getInstance());
        event.addListener(PaymentItemsRangeManager.getInstance());
    }
}
