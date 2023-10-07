package com.cerbon.better_beacons.util.mixin;

import net.minecraft.world.effect.MobEffect;

public interface IBeaconBlockEntityMixin {
    String better_beacons_getPaymentItem();
    MobEffect better_beacons_getTertiaryPower();
    int better_beacons_getPrimaryEffectAmplifier();
    void better_beacons_setPrimaryEffectAmplifier(int amplifier);
}
