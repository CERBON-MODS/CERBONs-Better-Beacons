package com.cerbon.better_beacons.datapack;

import com.cerbon.better_beacons.util.BBConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseBlocksAmplifierManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
    private static final BaseBlocksAmplifierManager INSTANCE = new BaseBlocksAmplifierManager();

    private static final String DIRECTORY = "base_blocks_amplifier";

    private static final HashMap<Block, Integer> blockAmplifierMap = new HashMap<>();

    public record ValuesListCodec(List<BlockAmplifierCodec> values) {
        public static final Codec<ValuesListCodec> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        BlockAmplifierCodec.CODEC.listOf().fieldOf("values").forGetter(ValuesListCodec::values)
                ).apply(instance, instance.stable(ValuesListCodec::new)));
    }

    public record BlockAmplifierCodec(Block block, int amplifier) {
        public static final Codec<BlockAmplifierCodec> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(BlockAmplifierCodec::block),
                        Codec.INT.fieldOf("amplifier").forGetter(BlockAmplifierCodec::amplifier)
                ).apply(instance, instance.stable(BlockAmplifierCodec::new)));
    }

    public BaseBlocksAmplifierManager() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resources, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        blockAmplifierMap.clear();

        resources.forEach((resourceLocation, jsonElement) -> {
            try {

                DataResult<ValuesListCodec> dataResult = BaseBlocksAmplifierManager.ValuesListCodec.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                dataResult.resultOrPartial(result -> {}).ifPresent(this::addToBlockAmplifierMap);

            } catch (Exception e) {
                BBConstants.LOGGER.error("Couldn't parse beacon base block amplifier file {}", resourceLocation, e);
            }
        });
    }

    private void addToBlockAmplifierMap(BaseBlocksAmplifierManager.ValuesListCodec valuesList) {
        valuesList.values().forEach(entry -> blockAmplifierMap.put(entry.block(), Math.max(Math.min(entry.amplifier(), 254), 0)));
    }

    public static BaseBlocksAmplifierManager getInstance() {
        return INSTANCE;
    }

    public static HashMap<Block, Integer> getBlockAmplifierMap() {
        return blockAmplifierMap;
    }

    public static int getHighestAmplifier() {
        return Collections.max(blockAmplifierMap.values());
    }

    public static int getLowestAmplifier() {
        return Collections.min(blockAmplifierMap.values());
    }
}
