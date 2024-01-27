package com.cerbon.better_beacons;

import com.cerbon.better_beacons.packet.BBPackets;

public class BetterBeaconsClient {

    public static void init() {
        BBPackets.register();
    }
}
