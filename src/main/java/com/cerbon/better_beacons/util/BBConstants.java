package com.cerbon.better_beacons.util;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BBConstants {
    public static final String MOD_ID = "better_beacons";
    public static final String COMMON_CONFIG_NAME = MOD_ID + "-common.toml";

    public static final String PAYMENT_ITEM_KEY = "BBPaymentItem";

    public static final TagKey<Block> BEACON_TRANSPARENT = BlockTags.create(new ResourceLocation(MOD_ID, "beacon_transparent"));
    public static final TagKey<Block> BEACON_TRANSPARENCY = BlockTags.create(new ResourceLocation(MOD_ID, "beacon_transparency"));
    public static final TagKey<Block> BEACON_REDIRECT = BlockTags.create(new ResourceLocation(MOD_ID, "beacon_redirect"));

    public static final Component BEACON_RANGE_LABEL = Component.translatable("block.minecraft.beacon.better_beacons.beacon_range.label");
    public static final Component TERTIARY_POWER_LABEL = Component.translatable("block.minecraft.beacon.better_beacons.tertiary_power.label");

    public static final Tooltip CONFIRM_BUTTON_TOOLTIP = Tooltip.create(Component.translatable("block.minecraft.beacon.better_beacons.confirm_button.tooltip"));
    public static final Tooltip CANCEL_BUTTON_TOOLTIP = Tooltip.create(Component.translatable("block.minecraft.beacon.better_beacons.cancel_button.tooltip"));
}
