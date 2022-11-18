package com.ek.flooringmastery.service;

import com.ek.flooringmastery.dto.Order;

import java.math.BigDecimal;

public interface OrderCalculationService {

    public Order updateCalculatedFields(Order order);

    public Boolean stateIsValid(String stateAbbr);
}
