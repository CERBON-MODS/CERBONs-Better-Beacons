package com.cerbon.better_beacons.platform;

import com.cerbon.better_beacons.platform.services.IAttributeHelper;
import com.cerbon.better_beacons.platform.services.IMenuTypeHelper;
import com.cerbon.better_beacons.util.BBConstants;

import java.util.ServiceLoader;

public class BBServices {
    public static final IAttributeHelper PLATFORM_ATTRIBUTE_HELPER = load(IAttributeHelper.class);
    public static final IMenuTypeHelper PLATFORM_MENU_TYPE_HELPER = load(IMenuTypeHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        BBConstants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
