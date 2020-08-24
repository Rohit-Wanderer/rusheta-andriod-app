package com.example.rusheta.service.model;

public class ContactDataDTO {

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

    public ContactDataDTO(int dataType, String dataValue) {
        this.dataType = dataType;
        this.dataValue = dataValue;
    }
}

