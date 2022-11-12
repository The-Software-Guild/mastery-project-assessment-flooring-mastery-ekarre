package com.ek.flooringmastery.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class State {

    private String stateAbbreviation;
    private String stateName;
    private BigDecimal taxRate;

    public State(){}


    public String getStateAbbreviation() {
        return stateAbbreviation;
    }
    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getStateName(){
        return stateName;
    }
    public void setStateName(String stateName){
        this.stateName = stateName;
    }

    public BigDecimal getTaxRate(){
        return taxRate.setScale(2, RoundingMode.HALF_UP);
    }
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate.setScale(2, RoundingMode.HALF_UP);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return getStateAbbreviation().equals(state.getStateAbbreviation()) && getStateName().equals(state.getStateName()) && getTaxRate().equals(state.getTaxRate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStateAbbreviation(), getStateName(), getTaxRate());
    }
}
