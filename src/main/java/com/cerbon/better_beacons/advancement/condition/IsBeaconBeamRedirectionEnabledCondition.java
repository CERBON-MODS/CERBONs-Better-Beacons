package com.cerbon.better_beacons.advancement.condition;

import com.cerbon.better_beacons.config.BBCommonConfigs;
import com.cerbon.better_beacons.util.BBConstants;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class IsBeaconBeamRedirectionEnabledCondition implements ICondition {
    private static final ResourceLocation NAME = new ResourceLocation(BBConstants.MOD_ID, "is_beacon_beam_redirection_enabled");
    private final boolean IS_BEACON_BEAM_REDIRECTION_ENABLED = BBCommonConfigs.ENABLE_BEACON_BEAM_REDIRECTION.get();

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(IContext context) {
        return IS_BEACON_BEAM_REDIRECTION_ENABLED;
    }

    public static class Serializer implements IConditionSerializer<IsBeaconBeamRedirectionEnabledCondition> {
        public static final IsBeaconBeamRedirectionEnabledCondition.Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, IsBeaconBeamRedirectionEnabledCondition value) {}

        @Override
        public IsBeaconBeamRedirectionEnabledCondition read(JsonObject json) {
            return new IsBeaconBeamRedirectionEnabledCondition();
        }

        @Override
        public ResourceLocation getID() {
            return IsBeaconBeamRedirectionEnabledCondition.NAME;
        }
    }
}
