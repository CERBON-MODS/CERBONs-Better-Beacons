package com.cerbon.better_beacons.advancement.condition;

import com.cerbon.better_beacons.config.BBCommonConfigs;
import com.cerbon.better_beacons.util.BBConstants;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class IsTertiaryEffectEnabledCondition implements ICondition {
    private static final ResourceLocation NAME = new ResourceLocation(BBConstants.MOD_ID, "is_tertiary_effect_enabled");
    private final boolean IS_TERTIARY_EFFECT_ENABLED = BBCommonConfigs.ENABLE_TERTIARY_EFFECTS.get();

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(IContext context) {
        return IS_TERTIARY_EFFECT_ENABLED;
    }

    public static class Serializer implements IConditionSerializer<IsTertiaryEffectEnabledCondition>{
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, IsTertiaryEffectEnabledCondition value) {}

        @Override
        public IsTertiaryEffectEnabledCondition read(JsonObject json) {
            return new IsTertiaryEffectEnabledCondition();
        }

        @Override
        public ResourceLocation getID() {
            return IsTertiaryEffectEnabledCondition.NAME;
        }
    }
}
