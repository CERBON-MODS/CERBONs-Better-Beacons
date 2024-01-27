package com.cerbon.better_beacons.fabric;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.BetterBeaconsClient;
import com.cerbon.better_beacons.fabric.event.BBClientEventsFabric;
import com.cerbon.better_beacons.fabric.event.BBEventsFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class BetterBeaconsFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        BetterBeacons.init();

        BBEventsFabric.register();
    }

    @Override
    public void onInitializeClient() {
        BetterBeaconsClient.init();
        BBClientEventsFabric.register();
    }
}