package com.cerbon.better_beacons.config;

import com.cerbon.better_beacons.config.custom.*;
import com.cerbon.better_beacons.util.BBConstants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

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
}
