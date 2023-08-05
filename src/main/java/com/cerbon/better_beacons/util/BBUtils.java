package com.cerbon.better_beacons.util;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class BBUtils {
    public static String getItemNameWithCreatorModId(Item item){
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString();
    }
}
