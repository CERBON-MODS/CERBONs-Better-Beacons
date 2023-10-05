package com.cerbon.better_beacons.util.json;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.util.BBUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeaconBaseBlocksAmplifierManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
    private static final BeaconBaseBlocksAmplifierManager INSTANCE = new BeaconBaseBlocksAmplifierManager();

    private static final String DIRECTORY = "base_blocks_amplifier";

    private static final HashMap<String, Integer> blockAmplifierMap = new HashMap<>();

    public record ValuesListCodec(List<BeaconBaseBlocksAmplifierManager.BlockAmplifierCodec> values){
        public static final Codec<BeaconBaseBlocksAmplifierManager.ValuesListCodec> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                BeaconBaseBlocksAmplifierManager.BlockAmplifierCodec.CODEC.listOf().fieldOf("values").forGetter(BeaconBaseBlocksAmplifierManager.ValuesListCodec::values)
        ).apply(instance, instance.stable(BeaconBaseBlocksAmplifierManager.ValuesListCodec::new)));
    }

    public record BlockAmplifierCodec(Block block, int amplifier) {
        public static final Codec<BeaconBaseBlocksAmplifierManager.BlockAmplifierCodec> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                ForgeRegistries.BLOCKS.getCodec().fieldOf("block").forGetter(BeaconBaseBlocksAmplifierManager.BlockAmplifierCodec::block),
                Codec.INT.fieldOf("amplifier").forGetter(BeaconBaseBlocksAmplifierManager.BlockAmplifierCodec::amplifier)
        ).apply(instance, instance.stable(BeaconBaseBlocksAmplifierManager.BlockAmplifierCodec::new)));
    }

    public BeaconBaseBlocksAmplifierManager() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resources, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        blockAmplifierMap.clear();
        resources.forEach((resourceLocation, jsonElement) -> {
            try {
                DataResult<BeaconBaseBlocksAmplifierManager.ValuesListCodec> dataResult = BeaconBaseBlocksAmplifierManager.ValuesListCodec.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                dataResult.resultOrPartial(result -> {}).ifPresent(this::addToBlockAmplifierMap);
            }catch (Exception e){
                BetterBeacons.LOGGER.error("Better Beacons Error: Couldn't parse beacon base block amplifier file {}", resourceLocation, e);
            }
        });
    }

    private void addToBlockAmplifierMap(BeaconBaseBlocksAmplifierManager.ValuesListCodec valuesList){
        valuesList.values().forEach(entry -> blockAmplifierMap.put(BBUtils.getBlockKeyAsString(entry.block()), Math.max(entry.amplifier(), 1)));
    }

    public static BeaconBaseBlocksAmplifierManager getInstance() {
        return INSTANCE;
    }

    public static HashMap<String, Integer> getBlockAmplifierMap(){
        return blockAmplifierMap;
    }
}
