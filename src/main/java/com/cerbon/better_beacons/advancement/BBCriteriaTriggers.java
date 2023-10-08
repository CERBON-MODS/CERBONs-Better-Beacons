package com.cerbon.better_beacons.advancement;

import com.cerbon.better_beacons.advancement.trigger.BBGenericTrigger;
import com.cerbon.better_beacons.util.BBConstants;
import net.minecraft.resources.ResourceLocation;

public class BBCriteriaTriggers {
    public static final BBGenericTrigger REDIRECT_BEACON = new BBGenericTrigger(new ResourceLocation(BBConstants.MOD_ID, "redirect_beacon"));
    public static final BBGenericTrigger INVISIBLE_BEAM = new BBGenericTrigger(new ResourceLocation(BBConstants.MOD_ID, "invisible_beam"));
    public static final BBGenericTrigger INCREASE_EFFECTS_STRENGTH = new BBGenericTrigger(new ResourceLocation(BBConstants.MOD_ID, "increase_effects_strength"));
}
