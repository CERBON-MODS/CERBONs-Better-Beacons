package com.cerbon.better_beacons.fabric.advancement.condition.custom;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.fabric.advancement.condition.BBResourceConditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;

public record ConfigEnabledResourceCondition(String config) implements ResourceCondition {
    public static final MapCodec<ConfigEnabledResourceCondition> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("config").forGetter(ConfigEnabledResourceCondition::config)
            ).apply(instance, ConfigEnabledResourceCondition::new)
    );

    @Override
    public ResourceConditionType<?> getType() {
        return BBResourceConditions.CONFIG_ENABLED;
    }

    @Override
    public boolean test(@Nullable HolderLookup.Provider registryLookup) {
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
