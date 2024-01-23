package com.cerbon.better_beacons.forge.platform;

import com.cerbon.better_beacons.platform.services.IAttributeHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeMod;

public class AttributeHelperForge implements IAttributeHelper {

    @Override
    public Attribute getBlockReach() {
        return ForgeMod.BLOCK_REACH.get();
    }
}
