package com.cerbon.better_beacons.neoforge.advancement.condition.custom;

import com.cerbon.better_beacons.BetterBeacons;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public record IsConfigEnabledCondition(String config) implements ICondition {
    public static final MapCodec<IsConfigEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.fieldOf("config").forGetter(IsConfigEnabledCondition::config)
            ).apply(instance, IsConfigEnabledCondition::new)
    );

    @Override
    public boolean test(@NotNull IContext iContext) {
        return switch (config) {
            case "tertiary_effect" -> BetterBeacons.config.beaconEffects.isTertiaryEffectsEnabled;
            case "beacon_beam_redirection" -> BetterBeacons.config.beaconBeam.allowRedirecting;
            case "beacon_beam_transparency" -> BetterBeacons.config.beaconBeam.allowTransparency;
            case "base_block_amplifier" -> BetterBeacons.config.beaconRangeAndAmplifier.isBaseBlockAmplifierEnabled;
            case "payment_item_range" -> BetterBeacons.config.beaconRangeAndAmplifier.isPaymentItemRangeEnabled;
            default -> false;
        };
    }

    @Override
    public @NotNull MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}