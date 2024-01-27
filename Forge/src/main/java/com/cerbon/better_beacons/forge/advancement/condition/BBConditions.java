package com.cerbon.better_beacons.forge.advancement.condition;

import com.cerbon.better_beacons.forge.advancement.condition.custom.IsConfigEnabledCondition;
import com.cerbon.better_beacons.util.BBConstants;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BBConditions {
    public static final DeferredRegister<Codec<? extends ICondition>> CONDITION_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.CONDITION_SERIALIZERS, BBConstants.MOD_ID);

    public static RegistryObject<Codec<? extends ICondition>> IS_CONFIG_ENABLED = CONDITION_SERIALIZERS.register("is_config_enabled", () ->
            IsConfigEnabledCondition.CODEC
    );

    public static void register(IEventBus eventBus) {
        CONDITION_SERIALIZERS.register(eventBus);
    }
}
