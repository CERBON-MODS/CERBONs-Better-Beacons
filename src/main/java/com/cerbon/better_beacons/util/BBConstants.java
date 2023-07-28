package com.cerbon.better_beacons.util;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class BBConstants {
    public static final String MOD_ID = "better_beacons";
    public static final String COMMON_CONFIG_NAME = MOD_ID + ".toml";

    public static final String PAYMENT_ITEM_KEY = "BBPaymentItem";

    public static final Component BEACON_RANGE_LABEL = Component.translatable("block.minecraft.beacon.better_beacons.beacon_range.label");

    public static final Tooltip CONFIRM_BUTTON_TOOLTIP = Tooltip.create(Component.translatable("block.minecraft.beacon.better_beacons.confirm_button.tooltip"));
    public static final Tooltip CANCEL_BUTTON_TOOLTIP = Tooltip.create(Component.translatable("block.minecraft.beacon.better_beacons.cancel_button.tooltip"));
}
