package com.ek.flooringmastery.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Order {

    private int orderNumber;
    private String customerName;
    private String stateAbbreviation;
    private BigDecimal taxRate;
    private String productType;
    private BigDecimal area;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;
    private String date;

    public Order(){}

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public BigDecimal getTaxRate() {
        return taxRate.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate.setScale(2, RoundingMode.HALF_UP);
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getArea() {
        return area.setScale(2, RoundingMode.HALF_UP);
    }

    public void setArea(BigDecimal area) {
        this.area = area.setScale(2, RoundingMode.HALF_UP);
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

    public BigDecimal getMaterialCost() {
        return materialCost.setScale(2, RoundingMode.HALF_UP);
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getLaborCost() {
        return laborCost.setScale(2, RoundingMode.HALF_UP);
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTax() {
        return tax.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotal() {
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTotal(BigDecimal total) {
        this.total = total.setScale(2, RoundingMode.HALF_UP);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getOrderNumber() == order.getOrderNumber() && Objects.equals(getCustomerName(), order.getCustomerName()) && Objects.equals(getStateAbbreviation(), order.getStateAbbreviation()) && Objects.equals(getTaxRate(), order.getTaxRate()) && Objects.equals(getProductType(), order.getProductType()) && Objects.equals(getArea(), order.getArea()) && Objects.equals(getCostPerSquareFoot(), order.getCostPerSquareFoot()) && Objects.equals(getLaborCostPerSquareFoot(), order.getLaborCostPerSquareFoot()) && Objects.equals(getMaterialCost(), order.getMaterialCost()) && Objects.equals(getLaborCost(), order.getLaborCost()) && Objects.equals(getTax(), order.getTax()) && Objects.equals(getTotal(), order.getTotal()) && Objects.equals(getDate(), order.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderNumber(), getCustomerName(), getStateAbbreviation(), getTaxRate(), getProductType(), getArea(), getCostPerSquareFoot(), getLaborCostPerSquareFoot(), getMaterialCost(), getLaborCost(), getTax(), getTotal(), getDate());
    }
}
