package com.cerbon.better_beacons.forge.advancement.condition;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.util.BBConstants;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class IsConfigEnabledCondition implements ICondition {
    private static final ResourceLocation NAME = new ResourceLocation(BBConstants.MOD_ID, "is_config_enabled");
    private final String config;

    public IsConfigEnabledCondition(String config) {
        this.config = config;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(IContext context) {
        return switch (config) {
            case "tertiary_effect" -> BetterBeacons.config.beaconEffects.isTertiaryEffectsEnabled;
            case "beacon_beam_redirection" -> BetterBeacons.config.beaconBeam.allowRedirecting;
            case "beacon_beam_transparency" -> BetterBeacons.config.beaconBeam.allowTransparency;
            case "base_block_amplifier" -> BetterBeacons.config.beaconRangeAndAmplifier.isBaseBlockAmplifierEnabled;
            case "payment_item_range" -> BetterBeacons.config.beaconRangeAndAmplifier.isPaymentItemRangeEnabled;
            default -> false;
        };

    }

    public static class Serializer implements IConditionSerializer<IsConfigEnabledCondition> {
        public static final IsConfigEnabledCondition.Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, IsConfigEnabledCondition value) {
            json.addProperty("config", value.config);
        }

        @Override
        public IsConfigEnabledCondition read(JsonObject json) {
            return new IsConfigEnabledCondition(GsonHelper.getAsString(json, "config"));
        }

        @Override
        public ResourceLocation getID() {
            return IsConfigEnabledCondition.NAME;
        }
    }
}
