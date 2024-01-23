package com.cerbon.better_beacons.forge.event;

import com.cerbon.better_beacons.datapack.BaseBlocksAmplifierManager;
import com.cerbon.better_beacons.datapack.PaymentItemsRangeManager;
import com.cerbon.better_beacons.util.BBConstants;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BBConstants.MOD_ID)
public class ForgeEvents {

    @SubscribeEvent
    public static void registerDatapacks(AddReloadListenerEvent event) {
        event.addListener(BaseBlocksAmplifierManager.getInstance());
        event.addListener(PaymentItemsRangeManager.getInstance());
    }
}
