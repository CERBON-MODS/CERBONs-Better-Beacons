package com.cerbon.better_beacons.forge.advancement.condition;

import com.cerbon.better_beacons.forge.advancement.condition.custom.IsConfigEnabledCondition;
import com.cerbon.better_beacons.util.BBConstants;
import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BBConditions {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.CONDITION_SERIALIZERS, BBConstants.MOD_ID);

    public static final RegistryObject<MapCodec<IsConfigEnabledCondition>> IS_CONFIG_ENABLED = CONDITION_SERIALIZERS.register("is_config_enabled", () -> IsConfigEnabledCondition.CODEC);

    public static void register(IEventBus bus) {
        CONDITION_SERIALIZERS.register(bus);
    }
}
