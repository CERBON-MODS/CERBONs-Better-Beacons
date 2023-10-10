package com.cerbon.better_beacons.mixin.quark;

import com.cerbon.better_beacons.util.mixin.BeaconRedirectionAndTransparency;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.quark.content.tools.module.BeaconRedirectionModule;

@Mixin(value = BeaconRedirectionModule.class, remap = false)
public class BeaconRedirectionModuleMixin {

    @Inject(method = "tickBeacon", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private static void better_beacons_makeMyTickMethodRun(BeaconBlockEntity beacon, CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(BeaconRedirectionAndTransparency.tickBeacon(beacon));
    }
}
