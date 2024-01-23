package com.cerbon.better_beacons.fabric.platform;

import com.cerbon.better_beacons.platform.services.IAttributeHelper;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AttributeHelperFabric implements IAttributeHelper {

    @Override
    public Attribute getBlockReach() {
        return ReachEntityAttributes.REACH;
    }
}
