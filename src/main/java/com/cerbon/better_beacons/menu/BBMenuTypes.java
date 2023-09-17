package com.cerbon.better_beacons.menu;

import com.cerbon.better_beacons.menu.custom.NewBeaconMenu;
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

    public static final RegistryObject<MenuType<NewBeaconMenu>> NEW_BEACON_MENU =
            registerMenuType(NewBeaconMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory){
        return MENU_TYPE.register("new_beacon_menu", () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus){
        MENU_TYPE.register(eventBus);
    }
}
