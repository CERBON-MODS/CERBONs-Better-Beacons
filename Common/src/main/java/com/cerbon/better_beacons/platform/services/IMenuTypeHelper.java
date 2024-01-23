package com.cerbon.better_beacons.platform.services;

import com.cerbon.better_beacons.menu.BBMenuTypes;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface IMenuTypeHelper {
    <T extends AbstractContainerMenu> MenuType<T> registerMenuType(BBMenuTypes.MenuSupplier<T> factory);
}
