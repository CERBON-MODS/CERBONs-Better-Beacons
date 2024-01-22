package com.cerbon.better_beacons.fabric;

import com.cerbon.better_beacons.BetterBeacons;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class BetterBeaconsFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        BetterBeacons.init();
    }

    @Override
    public void onInitializeClient() {}
}