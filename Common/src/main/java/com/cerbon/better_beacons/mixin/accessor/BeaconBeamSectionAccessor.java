package com.cerbon.better_beacons.mixin.accessor;

import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BeaconBlockEntity.BeaconBeamSection.class)
public interface BeaconBeamSectionAccessor {

    @Accessor("height")
    void setHeight(int height);
}
