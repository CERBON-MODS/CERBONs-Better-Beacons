package com.cerbon.better_beacons.fabric.advancement.condition;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.util.BBConstants;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class BBResourceConditions {
    public static final ResourceLocation CONFIG_ENABLED = new ResourceLocation(BBConstants.MOD_ID, "is_config_enabled");

    public static ConditionJsonProvider configEnabled(ConditionJsonProvider value) {
        return new ConditionJsonProvider() {

            @Override
            public ResourceLocation getConditionId() {
                return CONFIG_ENABLED;
            }

            @Override
            public void writeParameters(JsonObject object) {
                object.add("config", value.toJson());
            }
        };
    }

    public static boolean isConfigEnabled(JsonObject jsonObject) {
        String config = GsonHelper.getAsString(jsonObject, "config");

        return switch (config) {
            case "tertiary_effect" -> BetterBeacons.config.beaconEffects.isTertiaryEffectsEnabled;
            case "beacon_beam_redirection" -> BetterBeacons.config.beaconBeam.allowRedirecting;
            case "beacon_beam_transparency" -> BetterBeacons.config.beaconBeam.allowTransparency;
            case "base_block_amplifier" -> BetterBeacons.config.beaconRangeAndAmplifier.isBaseBlockAmplifierEnabled;
            case "payment_item_range" -> BetterBeacons.config.beaconRangeAndAmplifier.isPaymentItemRangeEnabled;
            default -> false;
        };
    }
}
