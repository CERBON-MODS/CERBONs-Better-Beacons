package com.cerbon.better_beacons.neoforge;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.neoforge.advancement.condition.BBConditions;
import com.cerbon.better_beacons.neoforge.advancement.trigger.BBTriggers;
import com.cerbon.better_beacons.util.BBConstants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(BBConstants.MOD_ID)
public class BetterBeaconsNeo {

    public BetterBeaconsNeo(IEventBus eventBus) {
        BetterBeacons.init();
        BBConditions.register(eventBus);
        BBTriggers.register(eventBus);
    }
}
