package com.cerbon.better_beacons.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class BBCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> LEVEL1_EFFECTS, LEVEL2_EFFECTS, LEVEL3_EFFECTS, SECONDARY_EFFECTS, TERTIARY_EFFECTS, KEYS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_BEACON_BEAM_REDIRECTION, ENABLE_BEACON_BEAM_TRANSPARENCY, ENABLE_WATERLOGGING, ENABLE_CONDUCT_REDSTONE, LOCK_BEACON;
    public static final ForgeConfigSpec.ConfigValue<Integer> HORIZONTAL_MOVE_LIMIT;

    static {
        BUILDER.push("Beacon Effects");
        LEVEL1_EFFECTS = BUILDER
                .comment("This is a list that contains the effects that a beacon has when at level 1. I would recommend a maximum of three effects to prevent them from extending beyond the GUI. DEFAULT: \"minecraft:speed\", \"minecraft:jump_boost\"")
                .defineList("Level 1 Effects", List.of("minecraft:speed", "minecraft:jump_boost"), entry -> entry instanceof String);

        LEVEL2_EFFECTS = BUILDER
                .comment("This is a list that contains the effects that a beacon has when at level 2. I would recommend a maximum of three effects to prevent them from extending beyond the GUI. DEFAULT: \"minecraft:haste\", \"better_beacons:long_reach\"")
                .defineList("Level 2 Effects", List.of("minecraft:haste", "better_beacons:long_reach"), entry -> entry instanceof String);

        LEVEL3_EFFECTS = BUILDER
                .comment("This is a list that contains the effects that a beacon has when at level 3. I would recommend a maximum of three effects to prevent them from extending beyond the GUI. DEFAULT: \"minecraft:strength\", \"minecraft:resistance\"")
                .defineList("Level 3 Effects", List.of("minecraft:strength", "minecraft:resistance"), entry -> entry instanceof String);

        SECONDARY_EFFECTS = BUILDER
                .comment("This is a list that contains the secondary effects that a beacon has when at level 4. I would recommend a maximum of four effects to prevent them from extending beyond the GUI. DEFAULT: \"minecraft:night_vision\", \"minecraft:regeneration\", \"minecraft:fire_resistance\"")
                .defineList("Secondary Effects", List.of("minecraft:night_vision", "minecraft:regeneration", "minecraft:fire_resistance"), entry -> entry instanceof String);

        TERTIARY_EFFECTS = BUILDER
                .comment("This is a list that contains the tertiary effects that a beacon has when at level 5. You can insert here only two effects. DEFAULT: \"better_beacons:phantom_bane\", \"better_beacons:patrol_nullifier\"")
                .defineList("Tertiary Effects", List.of("better_beacons:phantom_bane", "better_beacons:patrol_nullifier"), entry -> entry instanceof String);
        BUILDER.pop();

        BUILDER.push("Redirection && Transparency");
        ENABLE_BEACON_BEAM_REDIRECTION = BUILDER
                .comment("Sets if the beacon beam can be redirected. DEFAULT: True")
                .define("Enable Beacon Beam Redirection", true);

        ENABLE_BEACON_BEAM_TRANSPARENCY = BUILDER
                .comment("Sets if the beacon beam can become transparent. DEFAULT: True")
                .define("Enable Beacon Beam Transparency", true);

        HORIZONTAL_MOVE_LIMIT = BUILDER
                .comment("Sets the maximum amount of blocks that the beacon beam can extend while horizontal. DEFAULT: 64")
                .define("Horizontal Move Limit", 64);
        BUILDER.pop();

        BUILDER.push("Waterlogging && Redstone");
        ENABLE_WATERLOGGING = BUILDER
                .comment("Sets if the beacon can be waterlogged. DEFAULT: True")
                .define("Enable Waterlogging", true);

        ENABLE_CONDUCT_REDSTONE = BUILDER
                .comment("Sets if the beacon can conduct redstone. DEFAULT: True")
                .define("Enable Conduct Redstone", true);
        BUILDER.pop();

        BUILDER.push("Lock Beacon");
        LOCK_BEACON = BUILDER
                .comment("Sets if the beacon needs a special item (Key) to open it for the first time. DEFAULT: False")
                .define("Lock Beacon", false);
        KEYS = BUILDER
                .comment("You can put in this list all items that can be used to unlock the beacon. Example: [\"minecraft:stick\", \"minecraft:bone\"]. DEFAULT: Nothing")
                .defineList("Keys", List.of(), entry -> entry instanceof String);

        SPEC = BUILDER.build();
    }
}
