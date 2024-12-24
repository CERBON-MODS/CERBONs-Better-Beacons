package com.cerbon.better_beacons.forge;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.forge.advancement.condition.BBConditions;
import com.cerbon.better_beacons.util.BBConstants;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

@Mod(BBConstants.MOD_ID)
public class BetterBeaconsForge {

    public BetterBeaconsForge(IEventBus modEventBus) {
        BetterBeacons.init();

        BBConditions.register(modEventBus);
    }
}