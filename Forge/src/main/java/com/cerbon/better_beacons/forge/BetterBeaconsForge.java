package com.cerbon.better_beacons.forge;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.forge.advancement.condition.BBConditions;
import com.cerbon.better_beacons.forge.advancement.trigger.BBTriggers;
import com.cerbon.better_beacons.util.BBConstants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BBConstants.MOD_ID)
public class BetterBeaconsForge {

    public BetterBeaconsForge() {
        BetterBeacons.init();
        BBConditions.register(FMLJavaModLoadingContext.get().getModEventBus());
        BBTriggers.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}