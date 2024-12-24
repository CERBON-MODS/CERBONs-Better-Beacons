package com.cerbon.better_beacons.fabric.platform;

import com.cerbon.better_beacons.platform.services.IAttributeHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AttributeHelperFabric implements IAttributeHelper {

    @Override
    public Attribute getBlockReach() {
        return Attributes.BLOCK_INTERACTION_RANGE.value();
    }
}
