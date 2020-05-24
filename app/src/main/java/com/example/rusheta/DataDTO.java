package com.example.rusheta;

public class DataDTO {

    private int dataType;

    private String dataValue;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public DataDTO(int dataType, String dataValue) {
        this.dataType = dataType;
        this.dataValue = dataValue;
    }
}

