package com.cerbon.better_beacons.packet;

import com.cerbon.better_beacons.packet.custom.BeaconC2SPacket;
import com.cerbon.cerbons_api.api.network.Network;

public class BBPackets {


    public void registerPackets() {
        Network.registerPacket(BeaconC2SPacket.type(), BeaconC2SPacket.class, BeaconC2SPacket.STREAM_CODEC, BeaconC2SPacket::handle);
    }

    public static void register() {
        new BBPackets().registerPackets();
    }
}
