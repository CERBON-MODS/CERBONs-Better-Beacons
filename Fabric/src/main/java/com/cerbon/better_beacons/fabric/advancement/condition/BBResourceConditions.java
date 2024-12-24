package com.cerbon.better_beacons.fabric.advancement.condition;

import com.cerbon.better_beacons.fabric.advancement.condition.custom.ConfigEnabledResourceCondition;
import com.cerbon.better_beacons.util.BBConstants;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.resources.ResourceLocation;

public class BBResourceConditions {
    public static final ResourceConditionType<ConfigEnabledResourceCondition> CONFIG_ENABLED = createResourceConditionType("is_config_enabled", ConfigEnabledResourceCondition.CODEC);

    private static <T extends ResourceCondition> ResourceConditionType<T> createResourceConditionType(String name, MapCodec<T> codec) {
        return ResourceConditionType.create(ResourceLocation.fromNamespaceAndPath(BBConstants.MOD_ID, name), codec);
    }
}
