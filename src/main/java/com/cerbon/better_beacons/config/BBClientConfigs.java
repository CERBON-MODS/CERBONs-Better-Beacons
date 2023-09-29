package com.cerbon.better_beacons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BBClientConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {

        SPEC = BUILDER.build();
    }
}
