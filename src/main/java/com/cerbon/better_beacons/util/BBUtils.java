package com.cerbon.better_beacons.util;

import com.cerbon.better_beacons.config.BBCommonConfigs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class BBUtils {
    public static String getItemKeyAsString(Item item){
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString();
    }

    public static MobEffect[][] getBeaconEffectsFromConfigFile(){
        MobEffect[] level1Effects = BBCommonConfigs.LEVEL1_EFFECTS.get().stream().map(BBUtils::getMobEffectByKey).toArray(MobEffect[]::new);
        MobEffect[] level2Effects = BBCommonConfigs.LEVEL2_EFFECTS.get().stream().map(BBUtils::getMobEffectByKey).toArray(MobEffect[]::new);
        MobEffect[] level3Effects = BBCommonConfigs.LEVEL3_EFFECTS.get().stream().map(BBUtils::getMobEffectByKey).toArray(MobEffect[]::new);
        MobEffect[] secondaryEffects = BBCommonConfigs.SECONDARY_EFFECTS.get().stream().map(BBUtils::getMobEffectByKey).toArray(MobEffect[]::new);
        return new MobEffect[][]{level1Effects, level2Effects, level3Effects, secondaryEffects};
    }

    public static MobEffect getMobEffectByKey(String key){
        return ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(key));
    }
}
