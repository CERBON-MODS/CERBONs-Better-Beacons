package com.cerbon.better_beacons.packet.custom;

import com.cerbon.better_beacons.menu.custom.NewBeaconMenu;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.cerbons_api.api.network.data.PacketContext;
import com.cerbon.cerbons_api.api.network.data.Side;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class BeaconC2SPacket {
    public static final ResourceLocation CHANNEL = ResourceLocation.fromNamespaceAndPath(BBConstants.MOD_ID, "beacon_c2s_packet");
    public static final StreamCodec<FriendlyByteBuf, BeaconC2SPacket> STREAM_CODEC = StreamCodec.ofMember(BeaconC2SPacket::write, BeaconC2SPacket::new);

    private final Optional<MobEffect> primary;
    private final Optional<MobEffect> secondary;
    private final Optional<MobEffect> tertiary;

    public BeaconC2SPacket(Optional<MobEffect> primary, Optional<MobEffect> secondary, Optional<MobEffect> tertiary) {
        this.primary = primary;
        this.secondary = secondary;
        this.tertiary = tertiary;
    }

    public BeaconC2SPacket(FriendlyByteBuf buffer) {
        this.primary = buffer.readOptional(effect -> effect.readById(BuiltInRegistries.MOB_EFFECT::byId));
        this.secondary = buffer.readOptional(effect -> effect.readById(BuiltInRegistries.MOB_EFFECT::byId));
        this.tertiary = buffer.readOptional(effect -> effect.readById(BuiltInRegistries.MOB_EFFECT::byId));
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeOptional(this.primary, (buffer1, effect) -> buffer1.writeById(BuiltInRegistries.MOB_EFFECT::getId, effect));
        buffer.writeOptional(this.secondary, (buffer1, effect) -> buffer1.writeById(BuiltInRegistries.MOB_EFFECT::getId, effect));
        buffer.writeOptional(this.tertiary, (buffer1, effect) -> buffer1.writeById(BuiltInRegistries.MOB_EFFECT::getId, effect));
    }

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
        }
    }

    public Optional<MobEffect> getPrimary() {
        return this.primary;
    }

    public Optional<MobEffect> getSecondary() {
        return this.secondary;
    }

    public Optional<MobEffect> getTertiary() {
        return this.tertiary;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }
}
