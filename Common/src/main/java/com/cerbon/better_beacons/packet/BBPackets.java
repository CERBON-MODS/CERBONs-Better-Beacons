package com.cerbon.better_beacons.packet;

import com.cerbon.better_beacons.packet.custom.BeaconC2SPacket;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.cerbons_api.api.network.Network;
import net.minecraft.resources.ResourceLocation;

public class BBPackets {
    private final ResourceLocation CHANNEL = new ResourceLocation(BBConstants.MOD_ID, "packets");

    public void registerPackets() {
        Network.registerPacket(CHANNEL, BeaconC2SPacket.class,
                BeaconC2SPacket::new,
                BeaconC2SPacket::write,
                BeaconC2SPacket::handle
        );
    }

    public static void register() {
        new BBPackets().registerPackets();
    }
}
