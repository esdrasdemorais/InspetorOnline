package com.esdrasmorais.inspetoronline.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Company extends Default {
    private Integer operationAreaCode;
    private Integer companyReferenceCode;
    private String companyName;

    public Company() {}

    public Integer getOperationAreaCode() {
        return operationAreaCode;
    }

    public void setOperationAreaCode(Integer operationAreaCode) {
        this.operationAreaCode = operationAreaCode;
    }

    public Integer getCompanyReferenceCode() {
        return companyReferenceCode;
    }

    public void setCompanyReferenceCode(Integer companyReferenceCode) {
        this.companyReferenceCode = companyReferenceCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}