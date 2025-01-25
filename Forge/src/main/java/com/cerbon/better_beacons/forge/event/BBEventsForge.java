package com.cerbon.better_beacons.forge.event;

import com.cerbon.better_beacons.advancement.BBCriteriaTriggers;
import com.cerbon.better_beacons.forge.advancement.condition.custom.IsConfigEnabledCondition;
import com.cerbon.better_beacons.util.BBConstants;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = BBConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BBEventsForge {

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        CraftingHelper.register(IsConfigEnabledCondition.Serializer.INSTANCE);

        CriteriaTriggers.register("better_beacons:redirect_beacon", BBCriteriaTriggers.REDIRECT_BEACON);
        CriteriaTriggers.register("better_beacons:invisible_beam", BBCriteriaTriggers.INVISIBLE_BEAM);
        CriteriaTriggers.register("better_beacons:increase_effects_strength", BBCriteriaTriggers.INCREASE_EFFECTS_STRENGTH);
        CriteriaTriggers.register("better_beacons:true_full_power", BBCriteriaTriggers.TRUE_FULL_POWER);
    }
}
