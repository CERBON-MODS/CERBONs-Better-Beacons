package com.cerbon.better_beacons.menu;

import com.cerbon.better_beacons.menu.custom.NewBeaconMenu;
import com.cerbon.better_beacons.platform.BBServices;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.cerbons_api.api.registry.RegistryEntry;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistries;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class BBMenuTypes {
    public static final ResourcefulRegistry<MenuType<?>> MENU_TYPES = ResourcefulRegistries.create(BuiltInRegistries.MENU, BBConstants.MOD_ID);

    public static final RegistryEntry<MenuType<NewBeaconMenu>> NEW_BEACON_MENU = MENU_TYPES.register("new_beacon_menu", () ->
            create(NewBeaconMenu::new)
    );

    private static <T extends AbstractContainerMenu> MenuType<T> create(MenuSupplier<T> factory) {
        return BBServices.PLATFORM_MENU_TYPE_HELPER.registerMenuType(factory);
    }

    @FunctionalInterface
    public interface MenuSupplier<T extends AbstractContainerMenu> {
        T create(int i, Inventory inventory);
    }

    public static void register() {
        MENU_TYPES.register();
    }
}
