package com.cerbon.better_beacons.neoforge.advancement.condition;

import com.cerbon.better_beacons.neoforge.advancement.condition.custom.IsConfigEnabledCondition;
import com.cerbon.better_beacons.util.BBConstants;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class BBConditions {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, BBConstants.MOD_ID);

    public static final Supplier<MapCodec<IsConfigEnabledCondition>> IS_CONFIG_ENABLED = CONDITION_CODECS.register("is_config_enabled", () -> IsConfigEnabledCondition.CODEC);

    public static void register(IEventBus bus) {
        CONDITION_CODECS.register(bus);
    }
}
