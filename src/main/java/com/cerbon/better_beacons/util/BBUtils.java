package com.cerbon.better_beacons.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BBUtils {
    public static String getItemNameWithCreatorModId(ItemStack itemStack){
        Item item = itemStack.getItem();
        String creatorModId = item.getCreatorModId(itemStack);

        return creatorModId + ":" + item;
    }
}
