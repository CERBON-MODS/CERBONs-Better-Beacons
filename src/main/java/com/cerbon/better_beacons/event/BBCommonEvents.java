package com.cerbon.better_beacons.event;

import com.cerbon.better_beacons.advancement.BBCriteriaTriggers;
import com.cerbon.better_beacons.advancement.condition.IsConfigEnabledCondition;
import com.cerbon.better_beacons.packet.BBPacketHandler;
import com.cerbon.better_beacons.util.BBConstants;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = BBConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BBCommonEvents {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BBPacketHandler.register();

            CraftingHelper.register(IsConfigEnabledCondition.Serializer.INSTANCE);

            CriteriaTriggers.register(BBCriteriaTriggers.REDIRECT_BEACON);
            CriteriaTriggers.register(BBCriteriaTriggers.INVISIBLE_BEAM);
            CriteriaTriggers.register(BBCriteriaTriggers.INCREASE_EFFECTS_STRENGTH);
            CriteriaTriggers.register(BBCriteriaTriggers.TRUE_FULL_POWER);
        });
    }
}
