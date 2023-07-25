package com.cerbon.better_beacons.menu;

import com.cerbon.better_beacons.menu.custom.BBNewBeaconMenu;
import com.cerbon.better_beacons.util.BBConstants;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BBMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPE =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, BBConstants.MOD_ID);

    public static final RegistryObject<MenuType<BBNewBeaconMenu>> NEW_BEACON_MENU =
            registerMenuType(BBNewBeaconMenu::new, "new_beacon_menu");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name){
        return MENU_TYPE.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus){
        MENU_TYPE.register(eventBus);
    }
}
