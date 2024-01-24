package com.cerbon.better_beacons.config;

import com.cerbon.better_beacons.config.custom.*;
import com.cerbon.better_beacons.util.BBConstants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Config(name = BBConstants.MOD_ID)
public class BBConfig implements ConfigData {

    @ConfigEntry.Category("Beacon Effects")
    @ConfigEntry.Gui.TransitiveObject
    public BeaconEffects beaconEffects = new BeaconEffects();

    @ConfigEntry.Category("Range && Amplifier")
    @ConfigEntry.Gui.TransitiveObject
    public RangeAndAmplifier beaconRangeAndAmplifier = new RangeAndAmplifier();

    @ConfigEntry.Category("Beam")
    @ConfigEntry.Gui.TransitiveObject
    public BeaconBeam beaconBeam = new BeaconBeam();

    @ConfigEntry.Category("Buttons")
    @ConfigEntry.Gui.TransitiveObject
    public BeaconButtons beaconButtons = new BeaconButtons();

    @ConfigEntry.Category("Payment Items UI")
    @ConfigEntry.Gui.TransitiveObject
    public BeaconPaymentItemsUI beaconPaymentItemsUI = new BeaconPaymentItemsUI();

    // The lists need to be initialized this way otherwise the config file will not save correctly
    public void postInit() {
        List<String> levelOneEffects = beaconEffects.levelOneEffects;
        List<String> levelTwoEffects = beaconEffects.levelTwoEffects;
        List<String> levelThreeEffects = beaconEffects.levelThreeEffects;
        List<String> secondaryEffects = beaconEffects.secondaryEffects;
        List<String> tertiaryEffects = beaconEffects.tertiaryEffects;

        if (levelOneEffects == null) {
            List<String> defaultEffects = Arrays.asList("minecraft:speed", "minecraft:jump_boost");
            beaconEffects.levelOneEffects = new ArrayList<>(defaultEffects);
        } else
            beaconEffects.levelOneEffects = new ArrayList<>(new HashSet<>(levelOneEffects));

        if (levelTwoEffects == null) {
            List<String> defaultEffects = Arrays.asList("minecraft:haste", "better_beacons:long_reach");
            beaconEffects.levelTwoEffects = new ArrayList<>(defaultEffects);
        } else
            beaconEffects.levelTwoEffects = new ArrayList<>(new HashSet<>(levelTwoEffects));

        if (levelThreeEffects == null) {
            List<String> defaultEffects = Arrays.asList("minecraft:strength", "minecraft:resistance");
            beaconEffects.levelThreeEffects = new ArrayList<>(defaultEffects);
        } else
            beaconEffects.levelThreeEffects = new ArrayList<>(new HashSet<>(levelThreeEffects));

        if (secondaryEffects == null) {
            List<String> defaultEffects = Arrays.asList("minecraft:night_vision", "minecraft:regeneration", "minecraft:fire_resistance");
            beaconEffects.secondaryEffects = new ArrayList<>(defaultEffects);
        } else
            beaconEffects.secondaryEffects = new ArrayList<>(new HashSet<>(secondaryEffects));

        if (tertiaryEffects == null) {
            List<String> defaultEffects = Arrays.asList("better_beacons:phantom_bane", "better_beacons:patrol_bane");
            beaconEffects.tertiaryEffects = new ArrayList<>(defaultEffects);
        } else
            beaconEffects.tertiaryEffects = new ArrayList<>(new HashSet<>(tertiaryEffects));
    }
}
