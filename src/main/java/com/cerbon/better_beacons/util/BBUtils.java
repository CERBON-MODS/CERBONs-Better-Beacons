package com.cerbon.better_beacons.util;

import com.cerbon.better_beacons.config.BBCommonConfigs;
import com.cerbon.cerbons_api.api.static_utilities.RegistryUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Objects;

public class BBUtils {

    public static boolean canUnlock(Player player, Component displayName){
        if (BBCommonConfigs.LOCK_BEACON.get() && !player.getPersistentData().getBoolean(BBConstants.UNLOCKED_BEACON_KEY)){
            List<? extends String> keys = BBCommonConfigs.KEYS.get();
            String mainHandItemKey = RegistryUtils.getItemKeyAsString(player.getMainHandItem().getItem());

            if (!player.isSpectator() && (!keys.contains(mainHandItemKey))){
                player.displayClientMessage(Component.translatable("beacon.isLocked", displayName, BBCommonConfigs.KEYS.get()).withStyle(ChatFormatting.RED), true);
                player.playNotifySound(SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.BLOCKS, 1.0F, 1.0F);
                return false;

            }else{
                player.getPersistentData().putBoolean(BBConstants.UNLOCKED_BEACON_KEY, true);
                player.displayClientMessage(Component.translatable("beacon.unlocked", displayName).withStyle(ChatFormatting.GREEN), true);
                player.playNotifySound(SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        }
        return true;
    }

    public static MobEffect[][] getBeaconEffectsFromConfigFile(){
        MobEffect[] level1Effects = BBCommonConfigs.LEVEL1_EFFECTS.get().stream().map(RegistryUtils::getMobEffectByKey).filter(Objects::nonNull).toArray(MobEffect[]::new);
        MobEffect[] level2Effects = BBCommonConfigs.LEVEL2_EFFECTS.get().stream().map(RegistryUtils::getMobEffectByKey).filter(Objects::nonNull).toArray(MobEffect[]::new);
        MobEffect[] level3Effects = BBCommonConfigs.LEVEL3_EFFECTS.get().stream().map(RegistryUtils::getMobEffectByKey).filter(Objects::nonNull).toArray(MobEffect[]::new);
        MobEffect[] secondaryEffects = BBCommonConfigs.SECONDARY_EFFECTS.get().stream().map(RegistryUtils::getMobEffectByKey).filter(Objects::nonNull).toArray(MobEffect[]::new);
        MobEffect[] tertiaryEffects = BBCommonConfigs.TERTIARY_EFFECTS.get().stream().map(RegistryUtils::getMobEffectByKey).filter(Objects::nonNull).toArray(MobEffect[]::new);
        return new MobEffect[][]{level1Effects, level2Effects, level3Effects, secondaryEffects, tertiaryEffects};
    }
}
