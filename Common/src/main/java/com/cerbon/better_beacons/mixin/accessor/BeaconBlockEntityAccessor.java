package com.cerbon.better_beacons.mixin.accessor;

import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public interface BeaconBlockEntityAccessor {

    @Accessor("checkingBeamSections")
    List<BeaconBlockEntity.BeaconBeamSection> checkingBeamSections();

    @Accessor("lastCheckY")
    void setLastCheckY(int y);
}
