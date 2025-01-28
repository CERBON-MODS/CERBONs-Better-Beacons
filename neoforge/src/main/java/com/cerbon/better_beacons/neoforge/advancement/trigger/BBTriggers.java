package com.cerbon.better_beacons.neoforge.advancement.trigger;

import com.cerbon.better_beacons.advancement.BBCriteriaTriggers;
import com.cerbon.better_beacons.advancement.trigger.BBGenericTrigger;
import com.cerbon.better_beacons.util.BBConstants;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BBTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES = DeferredRegister.create(Registries.TRIGGER_TYPE, BBConstants.MOD_ID);

    public static final Supplier<BBGenericTrigger> REDIRECT_BEACON = TRIGGER_TYPES.register("redirect_beacon", () -> BBCriteriaTriggers.REDIRECT_BEACON);
    public static final Supplier<BBGenericTrigger> INVISIBLE_BEAM = TRIGGER_TYPES.register("invisible_beam", () -> BBCriteriaTriggers.INVISIBLE_BEAM);
    public static final Supplier<BBGenericTrigger> INCREASE_EFFECTS_STRENGTH = TRIGGER_TYPES.register("increase_effects_strength", () -> BBCriteriaTriggers.INCREASE_EFFECTS_STRENGTH);
    public static final Supplier<BBGenericTrigger> TRUE_FULL_POWER = TRIGGER_TYPES.register("true_full_power", () -> BBCriteriaTriggers.TRUE_FULL_POWER);

    public static void register(IEventBus bus) {
        TRIGGER_TYPES.register(bus);
    }
}
