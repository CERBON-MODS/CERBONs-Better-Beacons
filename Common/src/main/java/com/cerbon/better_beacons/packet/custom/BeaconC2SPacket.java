package com.cerbon.better_beacons.packet.custom;

import com.cerbon.better_beacons.menu.custom.NewBeaconMenu;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Optional;

public record BeaconC2SPacket(Optional<Holder<MobEffect>> primary, Optional<Holder<MobEffect>> secondary, Optional<Holder<MobEffect>> tertiary) {
    public static final ResourceLocation CHANNEL = ResourceLocation.fromNamespaceAndPath(BBConstants.MOD_ID, "beacon_c2s_packet");
    public static final StreamCodec<RegistryFriendlyByteBuf, BeaconC2SPacket> STREAM_CODEC = StreamCodec.composite(
            MobEffect.STREAM_CODEC.apply(ByteBufCodecs::optional),
            BeaconC2SPacket::primary,
            MobEffect.STREAM_CODEC.apply(ByteBufCodecs::optional),
            BeaconC2SPacket::secondary,
            MobEffect.STREAM_CODEC.apply(ByteBufCodecs::optional),
            BeaconC2SPacket::tertiary,
            BeaconC2SPacket::new
    );

    public static void handle(PacketContext<BeaconC2SPacket> ctx) {
        if (ctx.side().equals(Side.CLIENT) || ctx.sender() == null) return;

        ServerPlayer player = ctx.sender();
        BeaconC2SPacket packet = ctx.message();

        AbstractContainerMenu abstractContainerMenu = player.containerMenu;

        if (abstractContainerMenu instanceof NewBeaconMenu newBeaconMenu) {
            if (!player.containerMenu.stillValid(player)) {
                BBConstants.LOGGER.debug("Player {} interacted with invalid menu {}", player, player.containerMenu);
                return;
            }

            if (packet.getPrimary().isPresent())
                newBeaconMenu.updateEffects(packet.getPrimary(), packet.getSecondary(), packet.getTertiary());
            else
                newBeaconMenu.removeActiveEffects();

            player.closeContainer();
        }
    }

    public Optional<Holder<MobEffect>> getPrimary() {
        return this.primary;
    }

    public Optional<Holder<MobEffect>> getSecondary() {
        return this.secondary;
    }

    public Optional<Holder<MobEffect>> getTertiary() {
        return this.tertiary;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }
}
