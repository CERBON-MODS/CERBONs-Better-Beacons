package com.cerbon.better_beacons.config.custom;

import java.util.List;

public class BeaconEffects {
    public boolean isTertiaryEffectsEnabled = true;

    public List<String> levelOneEffects = List.of("minecraft:speed", "minecraft:jump_boost");
    public List<String> levelTwoEffects = List.of("minecraft:haste", "better_beacons:long_reach");
    public List<String> levelThreeEffects = List.of("minecraft:strength", "minecraft:resistance");
    public List<String> secondaryEffects = List.of("minecraft:night_vision", "minecraft:regeneration", "minecraft:fire_resistance");
    public List<String> tertiaryEffects = List.of("better_beacons:phantom_bane", "better_beacons:patrol_nullifier");
}
