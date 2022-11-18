package com.ek.flooringmastery.service;

import com.ek.flooringmastery.dao.StateDao;
import com.ek.flooringmastery.dto.Order;
import com.ek.flooringmastery.dto.State;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrderCalculationServiceImpl implements OrderCalculationService{

    private StateDao stateDao;

    public OrderCalculationServiceImpl(StateDao state){
        this.stateDao = state;
    }

    @Override
    public Order updateCalculatedFields(Order order) {
        //MaterialCost = (Area * CostPerSquareFoot)
        order.setMaterialCost(order.getArea().multiply(order.getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP));

        //LaborCost = (Area * LaborCostPerSquareFoot)
        order.setLaborCost(order.getArea().multiply(order.getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP));

        //Tax = (MaterialCost + LaborCost) * (TaxRate/100)
        BigDecimal totalCost = order.getMaterialCost().add(order.getLaborCost()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal scaledTaxRate = order.getTaxRate().divide(new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
        order.setTax(totalCost.multiply(scaledTaxRate).setScale(2, RoundingMode.HALF_UP));

        //Total = (MaterialCost + LaborCost + Tax)
        order.setTotal(order.getMaterialCost().add(order.getLaborCost().add(order.getTax())).setScale(2, RoundingMode.HALF_UP));

        return order;
    }

    @Override
    public Boolean stateIsValid(String stateAbbr){
        //try to fetch a state by this abbreviation
        State testState = stateDao.getState(stateAbbr);
        return (testState != null);
    }
}
