package com.ek.flooringmastery.dao;

import com.ek.flooringmastery.dto.Order;
import com.ek.flooringmastery.service.FlooringPersistenceException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderDao {

    Order createOrder(Order order) throws FlooringPersistenceException;

    Map<String, Order> getOrderList(String date);

    Order getOrder(int orderNumber, String date);

    Order updateOrder(int orderNumber, String date, String customerName, String stateAbbreviation, String productType, BigDecimal area) throws FlooringPersistenceException;

    Order deleteOrder(int orderNumber, String date) throws FlooringPersistenceException;

    Map<String, Order> readOrder() throws FlooringPersistenceException;

    void writeOrder() throws FlooringPersistenceException;
}
