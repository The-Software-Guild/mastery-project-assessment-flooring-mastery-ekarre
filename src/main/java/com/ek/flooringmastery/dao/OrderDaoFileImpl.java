package com.ek.flooringmastery.dao;

import com.ek.flooringmastery.dto.Order;
import com.ek.flooringmastery.service.FlooringPersistenceException;

import java.util.Map;

public class OrderDaoFileImpl implements OrderDao{

    private Map<String, Order> orders;

    private Map<Integer, Order> numberOrders;

    private static final String ORDER_FILE_NAME = "orders/order.txt";

    private String orderFileName;

    private static final String DELIMITER = ",";

    public OrderDaoFileImpl() throws FlooringPersistenceException {
        orderFileName = ORDER_FILE_NAME;
        orders = readOrder();

    }

    public OrderDaoFileImpl(String orderTextFile) throws FlooringPersistenceException {
        orderFileName = orderTextFile;
        orders = readOrder();

    }

    @Override
    public Order createOrder(int orderNumber, Order order) throws FlooringPersistenceException {
        Order newOrder = numberOrders.put(orderNumber, order);
        writeOrder();
        return newOrder;
    }

    @Override
    public Order getOrder(int orderNumber) {
        return orders.get(orderNumber);
    }

    @Override
    public Order getOrder(int orderNumber, String date) {
        return orders.get(orderNumber);
    }

    @Override
    public Order updateOrder(int orderNumber, String date) throws FlooringPersistenceException {
        return null;
    }

    @Override
    public Order deleteOrder(int orderNumber, String date) throws FlooringPersistenceException {
        Order deletedOrder = orders.remove(orderNumber);
        writeOrder();
        return deletedOrder;
    }

    @Override
    public Map<String, Order> readOrder() throws FlooringPersistenceException {
        return null;
    }

    @Override
    public void writeOrder() throws FlooringPersistenceException {

    }

    //private Order unmarshallOrder(String line){}

    //private String marshallOrder(Order order){}
}
