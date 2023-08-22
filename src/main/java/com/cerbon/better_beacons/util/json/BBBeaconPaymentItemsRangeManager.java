package com.cerbon.better_beacons.util.json;

import com.cerbon.better_beacons.BetterBeacons;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BBBeaconPaymentItemsRangeManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
    private static final BBBeaconPaymentItemsRangeManager INSTANCE = new BBBeaconPaymentItemsRangeManager();

    private static final String DIRECTORY = "payment_items_range";

    // A list containing the "values" list of all the json files
    private static final List<ValuesListEntry> allValuesLists = new ArrayList<>();

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
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        allValuesLists.clear();
        resources.forEach((resourceLocation, jsonElement) -> {
            try {
                DataResult<ValuesListEntry> dataResult = ValuesListEntry.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                dataResult.resultOrPartial(result -> {}).ifPresent(allValuesLists::add);
            }catch (Exception e){
                BetterBeacons.LOGGER.error("Better Beacons Error: Couldn't parse beacons payment items range file {}", resourceLocation, e);
            }
        });
    }

    public static BBBeaconPaymentItemsRangeManager getInstance() {
        return INSTANCE;
    }

    public static List<ValuesListEntry> getAllValuesLists() {
        return allValuesLists;
    }
}
