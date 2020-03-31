package com.universaldoctor.igive2.service.dto;

import com.universaldoctor.igive2.domain.enumeration.DataType;

import java.time.Instant;

public class DataDTO {
    DataType data;
    String vaule;
    Instant date;

    public DataDTO(DataType data, String vaule, Instant date) {
        this.data = data;
        this.vaule = vaule;
        this.date = date;
    }

    public DataType getData() {
        return data;
    }

    public void setData(DataType data) {
        this.data = data;
    }

    public String getVaule() {
        return vaule;
    }

    public void setVaule(String vaule) {
        this.vaule = vaule;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}
