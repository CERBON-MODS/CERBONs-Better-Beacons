package com.cerbon.better_beacons.effect;

import com.cerbon.better_beacons.effect.custom.LongReachEffect;
import com.cerbon.better_beacons.effect.custom.PatrolNullifierEffect;
import com.cerbon.better_beacons.effect.custom.PhantomBaneEffect;
import com.cerbon.better_beacons.platform.BBServices;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.cerbons_api.api.registry.RegistryEntry;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistries;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class BBEffects {
    public static final ResourcefulRegistry<MobEffect> MOB_EFFECTS = ResourcefulRegistries.create(BuiltInRegistries.MOB_EFFECT, BBConstants.MOD_ID);

    public static final RegistryEntry<MobEffect> PHANTOM_BANE = MOB_EFFECTS.register("phantom_bane", () ->
            new PhantomBaneEffect(MobEffectCategory.BENEFICIAL, 3124687)
    );

    public static final RegistryEntry<MobEffect> PATROL_BANE = MOB_EFFECTS.register("patrol_bane", () ->
            new PatrolNullifierEffect(MobEffectCategory.BENEFICIAL, 9120331)
    );

    public static final RegistryEntry<MobEffect> LONG_REACH = MOB_EFFECTS.register("long_reach", () ->
            new LongReachEffect(MobEffectCategory.BENEFICIAL, 0xDEF58F).addAttributeModifier(BBServices.PLATFORM_ATTRIBUTE_HELPER.getBlockReach(), "C7F45B68-A090-4AD7-B75B-376BD2991CFD", 3d, AttributeModifier.Operation.ADDITION)
    );

    public static void register() {
        MOB_EFFECTS.register();
    }
}
