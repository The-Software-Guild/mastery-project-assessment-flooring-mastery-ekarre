package com.ek.flooringmastery.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Product {

    private String productType;

    private BigDecimal costPerSquareFoot;

    private BigDecimal laborCostPerSquareFoot;

    public Product(){}

    public String getProductType(){
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
    }
    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
    }
    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        this.laborCostPerSquareFoot = laborCostPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getProductType().equals(product.getProductType()) && getCostPerSquareFoot().equals(product.getCostPerSquareFoot()) && getLaborCostPerSquareFoot().equals(product.getLaborCostPerSquareFoot());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductType(), getCostPerSquareFoot(), getLaborCostPerSquareFoot());
    }
}
