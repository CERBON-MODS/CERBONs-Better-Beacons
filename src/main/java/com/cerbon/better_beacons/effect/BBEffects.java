package com.cerbon.better_beacons.effect;

import com.cerbon.better_beacons.effect.custom.PhantomBaneEffect;
import com.cerbon.better_beacons.util.BBConstants;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BBEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BBConstants.MOD_ID);

    public static final RegistryObject<MobEffect> PHANTOM_BANE = MOB_EFFECTS.register("phantom_bane",
            () -> new PhantomBaneEffect(MobEffectCategory.BENEFICIAL, 3124687));

    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}
