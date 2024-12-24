package com.cerbon.better_beacons.util;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.cerbons_api.api.static_utilities.RegistryUtils;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Objects;

public class BBUtils {

    public static List<List<Holder<MobEffect>>> getBeaconEffectsFromConfigFile() {
        List<Holder<MobEffect>> levelOneEffects = BetterBeacons.config.beaconEffects.levelOneEffects.stream().map(RegistryUtils::getMobEffectByKey).filter(Objects::nonNull).map(Holder::direct).toList();
        List<Holder<MobEffect>> levelTwoEffects = BetterBeacons.config.beaconEffects.levelTwoEffects.stream().map(RegistryUtils::getMobEffectByKey).filter(Objects::nonNull).map(Holder::direct).toList();
        List<Holder<MobEffect>> levelThreeEffects = BetterBeacons.config.beaconEffects.levelThreeEffects.stream().map(RegistryUtils::getMobEffectByKey).filter(Objects::nonNull).map(Holder::direct).toList();
        List<Holder<MobEffect>> secondaryEffects = BetterBeacons.config.beaconEffects.secondaryEffects.stream().map(RegistryUtils::getMobEffectByKey).filter(Objects::nonNull).map(Holder::direct).toList();
        List<Holder<MobEffect>> tertiaryEffects = BetterBeacons.config.beaconEffects.tertiaryEffects.stream().map(RegistryUtils::getMobEffectByKey).filter(Objects::nonNull).map(Holder::direct).toList();
        return List.of(levelOneEffects, levelTwoEffects, levelThreeEffects, secondaryEffects, tertiaryEffects);
    }

    public static List<ServerPlayer> getPlayersNearBeacon(Level level, int beaconX, int beaconY, int beaconZ) {
        return level.getEntitiesOfClass(ServerPlayer.class, (new AABB(beaconX, beaconY, beaconZ, beaconX, beaconY - 4, beaconZ)).inflate(10.0D, 5.0D, 10.0D));
    }

    public static String convertNumberToRoman(int number) {
        final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < VALUES.length && number > 0; i++) {
            while (VALUES[i] <= number) {
                number -= VALUES[i];
                result.append(SYMBOLS[i]);
            }
        }
        return result.toString();
    }
}
