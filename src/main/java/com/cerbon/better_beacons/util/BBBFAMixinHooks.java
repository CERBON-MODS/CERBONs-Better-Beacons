package com.cerbon.better_beacons.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.beaconsforall.common.BeaconsForAllConfig;

import java.util.function.Predicate;

public class BBBFAMixinHooks {
    public static final Predicate<LivingEntity> VALID_CREATURE = living ->
            !(living instanceof Player) && isValidCreature(living);

    private static boolean isValidCreature(LivingEntity livingEntity) {
        boolean validType = switch (BeaconsForAllConfig.creatureType) {
            case TAMED -> isTamed(livingEntity);
            case PASSIVE -> (livingEntity instanceof Animal || livingEntity instanceof Npc) &&
                    !(livingEntity instanceof Enemy);
            case ALL -> true;
            default -> false;
        };

        boolean validConfig = BeaconsForAllConfig.additionalCreatures.contains(livingEntity.getType());
        return validType || validConfig;
    }

    private static boolean isTamed(LivingEntity livingEntity) {
        boolean flag =
                livingEntity instanceof TamableAnimal && ((TamableAnimal) livingEntity).isTame();

        if (!flag) {
            flag = livingEntity instanceof AbstractHorse && ((AbstractHorse) livingEntity).isTamed();
        }

        if (!flag) {
            flag = livingEntity instanceof Saddleable && ((Saddleable) livingEntity).isSaddled();
        }
        return flag;
    }
}
