package com.cerbon.better_beacons.forge.advancement.condition.custom;

import com.cerbon.better_beacons.BetterBeacons;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraftforge.common.crafting.conditions.ICondition;

public record IsConfigEnabledCondition(String config) implements ICondition {
    public static final Codec<IsConfigEnabledCondition> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("config").forGetter(IsConfigEnabledCondition::config)
            ).apply(instance, IsConfigEnabledCondition::new)
    );

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

    @Override
    public Codec<? extends ICondition> codec() {
        return CODEC;
    }
}
