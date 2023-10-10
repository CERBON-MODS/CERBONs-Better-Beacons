package com.cerbon.better_beacons.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BBConstants {
    public static final String MOD_ID = "better_beacons";
    public static final String COMMON_CONFIG_NAME = MOD_ID + "-common.toml";
    public static final String CLIENT_CONFIG_NAME = MOD_ID + "-client.toml";

    public static final String PAYMENT_ITEM_KEY = "BBPaymentItem";
    public static final String TERTIARY_POWER_KEY = "BBTertiary";
    public static final String PRIMARY_EFFECT_AMPLIFIER_KEY = "BBPrimaryEffectAmplifier";
    public static final String UNLOCKED_BEACON_KEY = "BBUnlockedBeacon";

    public static final TagKey<Block> BEACON_TRANSPARENT = BlockTags.create(new ResourceLocation(MOD_ID, "beacon_transparent"));
    public static final TagKey<Block> BEACON_TRANSPARENCY = BlockTags.create(new ResourceLocation(MOD_ID, "beacon_transparency"));
    public static final TagKey<Block> BEACON_REDIRECT = BlockTags.create(new ResourceLocation(MOD_ID, "beacon_redirect"));

    public static final Component BEACON_RANGE_LABEL = Component.translatable("block.minecraft.beacon.better_beacons.beacon_range.label");
    public static final Component PAYMENT_ITEM_LABEL = Component.translatable("block.minecraft.beacon.better_beacons.payment_item.label");
    public static final Component TERTIARY_POWER_LABEL = Component.translatable("block.minecraft.beacon.better_beacons.tertiary_power.label");
    public static final Component CURRENT_PAYMENT_LABEL = Component.translatable("block.minecraft.beacon.better_beacons.current_payment.label");

    public static final Component CONFIRM_BUTTON_TOOLTIP = Component.translatable("block.minecraft.beacon.better_beacons.confirm_button.tooltip");
    public static final Component CANCEL_BUTTON_REMOVE_EFFECTS_TOOLTIP = Component.translatable("block.minecraft.beacon.better_beacons.cancel_button_remove_effects.tooltip");
    public static final Component CANCEL_BUTTON_CLOSE_CONTAINER_TOOLTIP = Component.translatable("block.minecraft.beacon.better_beacons.cancel_button_close_container.tooltip");

    public static final String BEACONS_FOR_ALL = "beaconsforall";
    public static final String QUARK = "quark";
}
