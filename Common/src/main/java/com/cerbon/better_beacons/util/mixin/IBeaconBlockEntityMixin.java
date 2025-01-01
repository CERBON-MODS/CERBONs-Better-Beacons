package com.cerbon.better_beacons.util.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

public interface IBeaconBlockEntityMixin {
    String bb_getPaymentItem();
    Holder<MobEffect> bb_getTertiaryEffect();
    int bb_getPrimaryEffectAmplifier();
    void bb_setPrimaryEffectAmplifier(int amplifier);
}
