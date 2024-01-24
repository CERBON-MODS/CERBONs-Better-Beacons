package com.cerbon.better_beacons.config.custom;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class BeaconPaymentItemsUI {

    @ConfigEntry.Gui.CollapsibleObject
    public First first = new First();

    @ConfigEntry.Gui.CollapsibleObject
    public Second second = new Second();

    @ConfigEntry.Gui.CollapsibleObject
    public Third third = new Third();

    @ConfigEntry.Gui.CollapsibleObject
    public Fourth fourth = new Fourth();

    @ConfigEntry.Gui.CollapsibleObject
    public Fifth fifth = new Fifth();

    @ConfigEntry.Gui.CollapsibleObject
    public Sixth sixth = new Sixth();

    public static class First {
        public String item = "minecraft:netherite_ingot";
        public int tertiaryPosX = 12;
        public int posX = 14;
    }

    public static class Second {
        public String item = "minecraft:diamond";
        public int tertiaryPosX = 33;
        public int posX = 35;
    }

    public static class Third {
        public String item = "minecraft:emerald";
        public int tertiaryPosX = 53;
        public int posX = 55;
    }

    public static class Fourth {
        public String item = "minecraft:gold_ingot";
        public int tertiaryPosX = 75;
        public int posX = 77;
    }

    public static class Fifth {
        public String item = "minecraft:iron_ingot";
        public int tertiaryPosX = 97;
        public int posX = 100;
    }

    public static class Sixth {
        public String item = "minecraft:copper_ingot";
        public int tertiaryPosX = 119;
        public int posX = 122;
    }
}
