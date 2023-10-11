package com.cerbon.better_beacons.advancement.condition;

import com.cerbon.better_beacons.config.BBCommonConfigs;
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
    public boolean test() {
        return switch (config) {
            case "tertiary_effect" -> BBCommonConfigs.ENABLE_TERTIARY_EFFECTS.get();
            case "beacon_beam_redirection" -> BBCommonConfigs.ENABLE_BEACON_BEAM_REDIRECTION.get();
            case "beacon_beam_transparency" -> BBCommonConfigs.ENABLE_BEACON_BEAM_TRANSPARENCY.get();
            case "base_block_amplifier" -> BBCommonConfigs.ENABLE_BASE_BLOCK_AMPLIFIER.get();
            case "payment_item_range" -> BBCommonConfigs.ENABLE_PAYMENT_ITEM_RANGE.get();
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
