package com.universaldoctor.igive2.service.dto;

import com.universaldoctor.igive2.domain.enumeration.DataType;

public class DataMeanDTO {

    String dataType;
    String meanValue;

    public DataMeanDTO(String dataType, String meanValue) {
        this.dataType = dataType;
        this.meanValue = meanValue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getMeanValue() {
        return meanValue;
    }

    public void setMeanValue(String meanValue) {
        this.meanValue = meanValue;
    }
}
