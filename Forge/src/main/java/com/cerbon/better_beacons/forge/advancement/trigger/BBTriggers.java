package com.cerbon.better_beacons.forge.advancement.trigger;

import com.cerbon.better_beacons.advancement.BBCriteriaTriggers;
import com.cerbon.better_beacons.advancement.trigger.BBGenericTrigger;
import com.cerbon.better_beacons.util.BBConstants;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BBTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES = DeferredRegister.create(Registries.TRIGGER_TYPE, BBConstants.MOD_ID);

    public static final RegistryObject<BBGenericTrigger> REDIRECT_BEACON = TRIGGER_TYPES.register("redirect_beacon", () -> BBCriteriaTriggers.REDIRECT_BEACON);
    public static final RegistryObject<BBGenericTrigger> INVISIBLE_BEAM = TRIGGER_TYPES.register("invisible_beam", () -> BBCriteriaTriggers.INVISIBLE_BEAM);
    public static final RegistryObject<BBGenericTrigger> INCREASE_EFFECTS_STRENGTH = TRIGGER_TYPES.register("increase_effects_strength", () -> BBCriteriaTriggers.INCREASE_EFFECTS_STRENGTH);
    public static final RegistryObject<BBGenericTrigger> TRUE_FULL_POWER = TRIGGER_TYPES.register("true_full_power", () -> BBCriteriaTriggers.TRUE_FULL_POWER);

    public static void register(IEventBus bus) {
        TRIGGER_TYPES.register(bus);
    }
}
