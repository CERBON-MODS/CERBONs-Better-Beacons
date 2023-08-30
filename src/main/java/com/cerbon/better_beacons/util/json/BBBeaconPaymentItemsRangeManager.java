package com.cerbon.better_beacons.util.json;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.util.BBUtils;
import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BBBeaconPaymentItemsRangeManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
    private static final BBBeaconPaymentItemsRangeManager INSTANCE = new BBBeaconPaymentItemsRangeManager();

    private static final String DIRECTORY = "payment_items_range";

    private static final HashMap<String, Integer> itemRangeMap = new HashMap<>();

    public record ValuesListEntry(List<ItemRangeEntry> values){
        public static final Codec<ValuesListEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                ItemRangeEntry.CODEC.listOf().fieldOf("values").forGetter(ValuesListEntry::values)
        ).apply(instance, instance.stable(ValuesListEntry::new)));
    }

    public record ItemRangeEntry(Item item, int range) {
        public static final Codec<ItemRangeEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(ItemRangeEntry::item),
                Codec.INT.fieldOf("range").forGetter(ItemRangeEntry::range)
        ).apply(instance, instance.stable(ItemRangeEntry::new)));
    }

    public BBBeaconPaymentItemsRangeManager() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resources, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        itemRangeMap.clear();
        resources.forEach((resourceLocation, jsonElement) -> {
            try {
                DataResult<ValuesListEntry> dataResult = ValuesListEntry.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                dataResult.resultOrPartial(result -> {}).ifPresent(this::addToItemRangeMap);
            }catch (Exception e){
                BetterBeacons.LOGGER.error("Better Beacons Error: Couldn't parse beacon payment items range file {}", resourceLocation, e);
            }
        });
    }

    private void addToItemRangeMap(ValuesListEntry valuesListEntry){
        valuesListEntry.values().forEach(entry -> itemRangeMap.put(BBUtils.getItemKeyAsString(entry.item()), entry.range()));
    }

    public static BBBeaconPaymentItemsRangeManager getInstance() {
        return INSTANCE;
    }

    public static HashMap<String, Integer> getItemRangeMap(){
        return itemRangeMap;
    }
}
