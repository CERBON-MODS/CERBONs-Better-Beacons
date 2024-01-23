package com.cerbon.better_beacons.datapack;

import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.cerbons_api.api.static_utilities.RegistryUtils;
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
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentItemsRangeManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
    private static final PaymentItemsRangeManager INSTANCE = new PaymentItemsRangeManager();

    private static final String DIRECTORY = "payment_items_range";

    private static final HashMap<String, Integer> itemRangeMap = new HashMap<>();

    public record ValuesListCodec(List<ItemRangeCodec> values) {
        public static final Codec<ValuesListCodec> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ItemRangeCodec.CODEC.listOf().fieldOf("values").forGetter(ValuesListCodec::values)
                ).apply(instance, instance.stable(ValuesListCodec::new)));
    }

    public record ItemRangeCodec(Item item, int range) {
        public static final Codec<ItemRangeCodec> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemRangeCodec::item),
                        Codec.INT.fieldOf("range").forGetter(ItemRangeCodec::range)
                ).apply(instance, instance.stable(ItemRangeCodec::new)));
    }

    public PaymentItemsRangeManager() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resources, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        itemRangeMap.clear();

        resources.forEach((resourceLocation, jsonElement) -> {
            try {

                DataResult<ValuesListCodec> dataResult = ValuesListCodec.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                dataResult.resultOrPartial(result -> {}).ifPresent(this::addToItemRangeMap);

            } catch (Exception e) {
                BBConstants.LOGGER.error("Couldn't parse beacon payment item range file {}", resourceLocation, e);
            }
        });
    }

    private void addToItemRangeMap(ValuesListCodec valuesList){
        valuesList.values().forEach(entry -> itemRangeMap.put(RegistryUtils.getItemKeyAsString(entry.item()), Math.max(entry.range(), 0)));
    }

    public static PaymentItemsRangeManager getInstance() {
        return INSTANCE;
    }

    public static HashMap<String, Integer> getItemRangeMap(){
        return itemRangeMap;
    }

    public static int getHighestRange() {
        return Collections.max(itemRangeMap.values());
    }
}
