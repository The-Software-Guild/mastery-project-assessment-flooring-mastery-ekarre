package com.ek.flooringmastery.service;

import com.ek.flooringmastery.dto.Order;

public interface OrderCalculationService {

    public Order materialCost();

    public Order laborCost();

    public Order stateTax();

    public Order orderTotal();
}
