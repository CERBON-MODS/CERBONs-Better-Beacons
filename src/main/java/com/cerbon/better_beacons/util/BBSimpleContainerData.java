package com.cerbon.better_beacons.util;

public class BBSimpleContainerData implements BBContainerData{

    public BBSimpleContainerData(int Size){
        int[] ints = new int[Size];
    }

    @Override
    public String getStringData(String dataName) {
        return null;
    }

    @Override
    public void setStringData(String dataName, String value) {

    }

    @Override
    public boolean getBooleanData(String dataName) {
        return false;
    }

    @Override
    public void setBooleanData(String dataName, boolean value) {

    }
}
