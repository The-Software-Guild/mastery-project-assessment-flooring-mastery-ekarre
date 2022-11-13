package com.ek.flooringmastery.dao;

import com.ek.flooringmastery.dto.Order;
import com.ek.flooringmastery.service.FlooringPersistenceException;

import java.util.Map;

public interface OrderDao {

    Order createOrder(int orderNumber, Order order) throws FlooringPersistenceException;

    Order getOrder(int orderNumber);

    Order getOrder(int orderNumber, String date);

    Order updateOrder(int orderNumber, String date) throws FlooringPersistenceException;

    Order deleteOrder(int orderNumber, String date) throws FlooringPersistenceException;

    Map<String, Order> readOrder() throws FlooringPersistenceException;

    void writeOrder() throws FlooringPersistenceException;
}
