package com.cerbon.better_beacons.packet;

import com.cerbon.better_beacons.packet.custom.BeaconC2SPacket;
import com.cerbon.better_beacons.util.BBConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class BBPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id(){
        return packetId++;
    }

    public static void register(){
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(BBConstants.MOD_ID, "packets"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(BeaconC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(BeaconC2SPacket::new)
                .encoder(BeaconC2SPacket::write)
                .consumerMainThread(BeaconC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }
}
