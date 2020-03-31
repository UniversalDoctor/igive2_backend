package com.universaldoctor.igive2.service.dto;

import javax.validation.constraints.NotNull;

public class HypertensionDTO {
    @NotNull
    String systolic;
    @NotNull
    String dyastolic;

    public HypertensionDTO(String systolic, String dyastolic) {
        this.systolic = systolic;
        this.dyastolic = dyastolic;
    }

    public String getSystolic() {
        return systolic;
    }

    public void setSystolic(String systolic) {
        this.systolic = systolic;
    }

    public String getDyastolic() {
        return dyastolic;
    }

    public void setDyastolic(String dyastolic) {
        this.dyastolic = dyastolic;
    }
}
