package com.cerbon.better_beacons.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class BBCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> LEVEL1_EFFECTS, LEVEL2_EFFECTS, LEVEL3_EFFECTS, SECONDARY_EFFECTS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_BEACON_REDIRECTION_AND_TRANSPARENCY;
    public static final ForgeConfigSpec.ConfigValue<Integer> HORIZONTAL_MOVE_LIMIT;

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
        BUILDER.pop();

        BUILDER.push("Redirection && Transparency");
        ENABLE_BEACON_REDIRECTION_AND_TRANSPARENCY = BUILDER
                .comment("Sets if the beacon beam can be redirected and become transparent when conditions are met. DEFAULT: True")
                .define("Enable Redirection && Transparency", true);

        HORIZONTAL_MOVE_LIMIT = BUILDER
                .comment("Sets the maximum amount of blocks that the beacon beam can extend while horizontal. DEFAULT: 64")
                .define("Horizontal Move Limit", 64);

        SPEC = BUILDER.build();
    }
}
