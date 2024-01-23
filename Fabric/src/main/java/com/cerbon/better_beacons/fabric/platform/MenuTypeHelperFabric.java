package com.cerbon.better_beacons.fabric.platform;

import com.cerbon.better_beacons.menu.BBMenuTypes;
import com.cerbon.better_beacons.platform.services.IMenuTypeHelper;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class MenuTypeHelperFabric implements IMenuTypeHelper {

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> registerMenuType(BBMenuTypes.MenuSupplier<T> factory) {
        return new MenuType<>(factory::create, FeatureFlags.DEFAULT_FLAGS);
    }
}
