package com.cerbon.better_beacons.mixin.effect;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.world.effect.HealthBoostMobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HealthBoostMobEffect.class)
public class HealthBoostMobEffectMixin {

    @WrapWithCondition(method = "removeAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"))
    private boolean better_beacons_makeHealthBoostEffectDontResetHealthIfItsTheSameAmplifier(LivingEntity livingEntity, float maxHealth, @NotNull LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        return pLivingEntity.getHealth() > 4 * (pAmplifier + 1) + pLivingEntity.getMaxHealth() || pLivingEntity.getHealth() > pLivingEntity.getMaxHealth() && !pLivingEntity.hasEffect(MobEffects.HEALTH_BOOST);
    }
}

