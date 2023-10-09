package com.cerbon.better_beacons.advancement.trigger;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class BBGenericTrigger extends SimpleCriterionTrigger<BBGenericTrigger.TriggerInstance> {
    final ResourceLocation id;

    public BBGenericTrigger(ResourceLocation resourceLocation){
        this.id = resourceLocation;
    }

    @Override
    protected @NotNull TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite predicate, DeserializationContext deserializationContext) {
        return new TriggerInstance(id, predicate);
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    public void trigger(ServerPlayer serverPlayer){
        this.trigger(serverPlayer, triggerInstance -> true);
    }


    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(ResourceLocation criterion, EntityPredicate.Composite player) {
            super(criterion, player);
        }
    }
}
