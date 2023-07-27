package com.cerbon.better_beacons.util;

public interface BBContainerData {
    String getStringData(String dataName);

    void setStringData(String dataName, String value);

    boolean getBooleanData(String dataName);

    void setBooleanData(String dataName, boolean value);
}
