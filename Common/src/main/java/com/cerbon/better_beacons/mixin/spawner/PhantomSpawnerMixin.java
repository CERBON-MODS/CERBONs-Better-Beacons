package com.cerbon.better_beacons.mixin.spawner;

import com.cerbon.better_beacons.effect.BBEffects;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"), cancellable = true)
    private void bb_preventPhantomsSpawn(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies, CallbackInfoReturnable<Integer> cir, @Local(ordinal = 0) ServerPlayer player) {
        if (player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(BBEffects.PHANTOM_BANE.get())))
            cir.setReturnValue(0);
    }
}
