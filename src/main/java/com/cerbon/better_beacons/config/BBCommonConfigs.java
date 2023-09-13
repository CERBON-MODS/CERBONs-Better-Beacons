package com.cerbon.better_beacons.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class BBCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> LEVEL1_EFFECTS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> LEVEL2_EFFECTS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> LEVEL3_EFFECTS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SECONDARY_EFFECTS;

    static {
        BUILDER.push("Beacon Effects");
        LEVEL1_EFFECTS = BUILDER
                .comment("This is a list that contains the effects that a beacon has when at level 1. I would recommend a maximum of three effects to prevent them from extending beyond the GUI. DEFAULT: \"minecraft:speed\", \"minecraft:luck\"")
                .defineList("Level 1 Effects", List.of("minecraft:speed", "minecraft:luck"), entry -> entry instanceof String);

        LEVEL2_EFFECTS = BUILDER
                .comment("This is a list that contains the effects that a beacon has when at level 2. I would recommend a maximum of three effects to prevent them from extending beyond the GUI. DEFAULT: \"minecraft:haste\", \"minecraft:jump_boost\"")
                .defineList("Level 2 Effects", List.of("minecraft:haste", "minecraft:jump_boost"), entry -> entry instanceof String);

        LEVEL3_EFFECTS = BUILDER
                .comment("This is a list that contains the effects that a beacon has when at level 3. I would recommend a maximum of three effects to prevent them from extending beyond the GUI. DEFAULT: \"minecraft:strength\", \"minecraft:resistance\"")
                .defineList("Level 3 Effects", List.of("minecraft:strength", "minecraft:resistance"), entry -> entry instanceof String);

        SECONDARY_EFFECTS = BUILDER
                .comment("This is a list that contains the secondary effects that a beacon has when at level 4. I would recommend a maximum of four effects to prevent them from extending beyond the GUI. DEFAULT: \"minecraft:night_vision\", \"minecraft:regeneration\", \"minecraft:fire_resistance\"")
                .defineList("Secondary Effects", List.of("minecraft:night_vision", "minecraft:regeneration", "minecraft:fire_resistance"), entry -> entry instanceof String);

        SPEC = BUILDER.build();
    }
}
