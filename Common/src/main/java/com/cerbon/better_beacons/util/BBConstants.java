package com.cerbon.better_beacons.util;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;

public class BBConstants {
    public static final String MOD_ID = "better_beacons";
    public static final String MOD_NAME = "Better Beacons";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final String PAYMENT_ITEM_KEY = "BBPaymentItem";
    public static final String TERTIARY_EFFECT_KEY = "BBTertiary";
    public static final String PRIMARY_EFFECT_AMPLIFIER_KEY = "BBPrimaryEffectAmplifier";

    public static final TagKey<Block> BEACON_TRANSPARENT = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, "beacon_transparent"));
    public static final TagKey<Block> BEACON_TRANSPARENCY = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, "beacon_transparency"));
    public static final TagKey<Block> BEACON_REDIRECT = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, "beacon_redirect"));

    public static final String QUARK = "quark";
    public static final String BEACONS_FOR_ALL = "beaconsforall";
}
