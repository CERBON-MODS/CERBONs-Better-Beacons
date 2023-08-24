package com.cerbon.better_beacons.mixin.packet;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.menu.custom.BBNewBeaconMenu;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    @Shadow public ServerPlayer player;

    @Inject(method = "handleSetBeaconPacket", at = @At(value = "TAIL"))
    private void better_beacons_handleBeaconPacketOnNewMenu(ServerboundSetBeaconPacket pPacket, CallbackInfo ci, @Local(ordinal = 0) AbstractContainerMenu abstractContainerMenu){
        if(abstractContainerMenu instanceof BBNewBeaconMenu beaconMenu){
            if (!this.player.containerMenu.stillValid(this.player)) {
                BetterBeacons.LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.containerMenu);
                return;
            }

            if (pPacket.getPrimary().isPresent())
                beaconMenu.updateEffects(pPacket.getPrimary(), pPacket.getSecondary());
            else
                beaconMenu.removeActiveEffects();
        }
    }
}
