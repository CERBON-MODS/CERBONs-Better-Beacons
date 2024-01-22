package com.cerbon.better_beacons.platform;

import com.cerbon.better_beacons.util.BBConstants;

import java.util.ServiceLoader;

public class BBServices {

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        BBConstants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
