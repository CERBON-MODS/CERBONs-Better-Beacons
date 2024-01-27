package com.cerbon.better_beacons.fabric.event;

import com.cerbon.better_beacons.advancement.BBCriteriaTriggers;
import com.cerbon.better_beacons.datapack.BaseBlocksAmplifierManager;
import com.cerbon.better_beacons.datapack.PaymentItemsRangeManager;
import com.cerbon.better_beacons.fabric.advancement.condition.BBResourceConditions;
import com.cerbon.better_beacons.util.BBConstants;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class BBEventsFabric {

    public static void register() {
        registerDatapacks();
        registerResourceConditions();
        registerCriterias();
    }

    private static void registerDatapacks() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {

            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(BBConstants.MOD_ID, "base_blocks_amplifier");
            }

            @Override
            public @NotNull CompletableFuture<Void> reload(@NotNull PreparationBarrier preparationBarrier, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller preparationsProfiler, @NotNull ProfilerFiller reloadProfiler, @NotNull Executor backgroundExecutor, @NotNull Executor gameExecutor) {
                return BaseBlocksAmplifierManager.getInstance().reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }
        });

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {

            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(BBConstants.MOD_ID, "payment_items_range");
            }

            @Override
            public @NotNull CompletableFuture<Void> reload(@NotNull PreparationBarrier preparationBarrier, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller preparationsProfiler, @NotNull ProfilerFiller reloadProfiler, @NotNull Executor backgroundExecutor, @NotNull Executor gameExecutor) {
                return PaymentItemsRangeManager.getInstance().reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }
        });
    }

    public static void registerResourceConditions() {
        ResourceConditions.register(BBResourceConditions.CONFIG_ENABLED, BBResourceConditions::isConfigEnabled);
    }

    public static void registerCriterias() {
        CriteriaTriggers.register("redirect_beacon", BBCriteriaTriggers.REDIRECT_BEACON);
        CriteriaTriggers.register("invisible_beam", BBCriteriaTriggers.INVISIBLE_BEAM);
        CriteriaTriggers.register("increase_effects_strength", BBCriteriaTriggers.INCREASE_EFFECTS_STRENGTH);
        CriteriaTriggers.register("true_full_power", BBCriteriaTriggers.TRUE_FULL_POWER);
    }
}
