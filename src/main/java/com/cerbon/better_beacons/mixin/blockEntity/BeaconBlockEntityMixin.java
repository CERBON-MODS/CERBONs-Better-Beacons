package com.cerbon.better_beacons.mixin.blockEntity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    @Shadow @Final public static final MobEffect[][] BEACON_EFFECTS = new MobEffect[][]{{MobEffects.MOVEMENT_SPEED, MobEffects.LUCK}, {MobEffects.DIG_SPEED, MobEffects.JUMP}, {MobEffects.DAMAGE_BOOST, MobEffects.DAMAGE_RESISTANCE}, {MobEffects.NIGHT_VISION, MobEffects.REGENERATION, MobEffects.HEALTH_BOOST}};
    @Shadow @Final private static final Set<MobEffect> VALID_EFFECTS = Arrays.stream(BEACON_EFFECTS).flatMap(Arrays::stream).collect(Collectors.toSet());

    @Inject(method = "applyEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z", ordinal = 1))
    private static void better_beacons_setHealthBoostEffectAmplifierTo1(Level pLevel, BlockPos pPos, int pLevels, MobEffect pPrimary, @NotNull MobEffect pSecondary, @NotNull CallbackInfo ci, @Local(ordinal = 2) int j, @Local(ordinal = 0) @NotNull Player player1){
        if (pSecondary.equals(MobEffects.HEALTH_BOOST)){
            player1.addEffect(new MobEffectInstance(pSecondary, j, 1 , true, true));
        }
    }
}
