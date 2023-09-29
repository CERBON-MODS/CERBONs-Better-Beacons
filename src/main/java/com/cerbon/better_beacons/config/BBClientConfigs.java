package com.cerbon.better_beacons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BBClientConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue CANCEL_BUTTON_REMOVE_EFFECTS, ENABLE_CANCEL_BUTTON_TOOLTIP, ENABLE_CONFIRM_BUTTON_TOOLTIP;

    static {
        BUILDER.push("Buttons");
        CANCEL_BUTTON_REMOVE_EFFECTS = BUILDER
                .comment("If false the cancel button will only close the gui and will not remove the active effects. DEFAULT: True")
                .define("Cancel Button Remove Effects", true);

        ENABLE_CANCEL_BUTTON_TOOLTIP = BUILDER
                .comment("If false the cancel button will not have a tooltip. DEFAULT: True")
                .define("Enable Cancel Button Tooltip", true);

        ENABLE_CONFIRM_BUTTON_TOOLTIP = BUILDER
                .comment("If false the confirm button will not have a tooltip. DEFAULT: True")
                .define("Enable Confirm Button Tooltip", true);

        SPEC = BUILDER.build();
    }
}
