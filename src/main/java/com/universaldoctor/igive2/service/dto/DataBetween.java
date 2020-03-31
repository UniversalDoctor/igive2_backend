package com.universaldoctor.igive2.service.dto;

import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.domain.enumeration.DataType;

import java.time.LocalDate;

public class DataBetween {
    MobileUser mobileUser;
    DataType data;
    LocalDate dateGT;
    LocalDate dateLT;

    public DataBetween(MobileUser mobileUser, DataType data, LocalDate dateGT, LocalDate dateLT) {
        this.mobileUser = mobileUser;
        this.data = data;
        this.dateGT = dateGT;
        this.dateLT = dateLT;
    }

    public MobileUser getMobileUser() {
        return mobileUser;
    }

    public void setMobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
    }

    public DataType getData() {
        return data;
    }

    public void setData(DataType data) {
        this.data = data;
    }

    public LocalDate getDateGT() {
        return dateGT;
    }

    public void setDateGT(LocalDate dateGT) {
        this.dateGT = dateGT;
    }

    public LocalDate getDateLT() {
        return dateLT;
    }

    public void setDateLT(LocalDate dateLT) {
        this.dateLT = dateLT;
    }
}
