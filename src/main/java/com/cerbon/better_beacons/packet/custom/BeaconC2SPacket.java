package com.cerbon.better_beacons.packet.custom;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.menu.custom.NewBeaconMenu;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class BeaconC2SPacket {
    private final Optional<MobEffect> primary;
    private final Optional<MobEffect> secondary;
    private final Optional<MobEffect> tertiary;

    public BeaconC2SPacket(Optional<MobEffect> primary, Optional<MobEffect> secondary, Optional<MobEffect> tertiary){
        this.primary = primary;
        this.secondary = secondary;
        this.tertiary = tertiary;
    }

    public BeaconC2SPacket(FriendlyByteBuf buffer){
        this.primary = buffer.readOptional(effect -> MobEffect.byId(effect.readVarInt()));
        this.secondary = buffer.readOptional(effect -> MobEffect.byId(effect.readVarInt()));
        this.tertiary = buffer.readOptional(effect -> MobEffect.byId(effect.readVarInt()));
    }

    public void write(FriendlyByteBuf buffer){
        buffer.writeOptional(this.primary, (buffer1, effect) -> buffer1.writeVarInt(MobEffect.getId(effect)));
        buffer.writeOptional(this.secondary, (buffer1, effect) -> buffer1.writeVarInt(MobEffect.getId(effect)));
        buffer.writeOptional(this.tertiary, (buffer1, effect) -> buffer1.writeVarInt(MobEffect.getId(effect)));
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() ->{
            ServerPlayer player = ctx.getSender();
            if (player == null)return;

            AbstractContainerMenu abstractContainerMenu = player.containerMenu;

            if (abstractContainerMenu instanceof NewBeaconMenu newBeaconMenu){
                if (!player.containerMenu.stillValid(player)){
                    BetterBeacons.LOGGER.debug("Player {} interacted with invalid menu {}", player, player.containerMenu);
                    return;
                }

                if (this.getPrimary().isPresent())
                    newBeaconMenu.updateEffects(this.getPrimary(), this.getSecondary(), this.getTertiary());
                else
                    newBeaconMenu.removeActiveEffects();
            }
        });
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
}

