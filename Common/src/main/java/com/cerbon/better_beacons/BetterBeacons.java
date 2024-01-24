package com.cerbon.better_beacons;

import com.cerbon.better_beacons.config.BBConfig;
import com.cerbon.better_beacons.effect.BBEffects;
import com.cerbon.better_beacons.menu.BBMenuTypes;
import com.cerbon.better_beacons.packet.BBPackets;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class BetterBeacons {
	public static BBConfig config;

	public static void init() {
		AutoConfig.register(BBConfig.class, JanksonConfigSerializer::new);
		AutoConfig.getConfigHolder(BBConfig.class).get().postInit();
		AutoConfig.getConfigHolder(BBConfig.class).save();
		config = AutoConfig.getConfigHolder(BBConfig.class).get();

		BBPackets.register();

		BBEffects.register();
		BBMenuTypes.register();
	}
}
