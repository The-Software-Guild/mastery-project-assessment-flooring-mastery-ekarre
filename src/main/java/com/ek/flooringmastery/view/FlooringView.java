package com.ek.flooringmastery.view;

import com.ek.flooringmastery.dao.*;
import com.ek.flooringmastery.dto.Order;
import com.ek.flooringmastery.dto.Product;
import com.ek.flooringmastery.dto.State;
import com.ek.flooringmastery.service.FlooringPersistenceException;
import com.ek.flooringmastery.service.OrderCalculationService;
import com.ek.flooringmastery.service.OrderCalculationServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FlooringView {

    private UserIO io;
    private StateDao stateDao = new StateDaoFileImpl();
    private OrderDao orderDao = new OrderDaoFileImpl();
    private OrderCalculationService orderService = new OrderCalculationServiceImpl(stateDao);
    private ProductDao productDao = new ProductDaoFileImpl();

    public FlooringView(UserIO io) throws FlooringPersistenceException {
        this.io = io;
    }

    // main menu
    public void welcomeBanner(){io.print("* * * * * Welcome to TSG Corp * * * * *");}
    public int mainMenu(){
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export all Data");
        io.print("6. Exit");

        return io.readInt("Please select from the above choices ", 1, 6);
    }

    // 1. method to display orders
    public void displayAllBanner(){io.print("* * * * * Display all Orders * * * * *");}
    public void displayOrders() {
        String getDate = "";
        getDate = io.readString("Please enter the date you would like to look up ");
        Map<String, Order> orderList = orderDao.getOrderList(getDate);
        if(orderList.size() > 0) {
            for (Map.Entry<String, Order> entry : orderList.entrySet()) {
                String orderInfo = String.format("%s %s %s %s %s %s %s %s %s %s %s %s",
                        entry.getValue().getOrderNumber(),
                        entry.getValue().getCustomerName(),
                        entry.getValue().getStateAbbreviation(),
                        entry.getValue().getTaxRate(),
                        entry.getValue().getProductType(),
                        entry.getValue().getArea(),
                        entry.getValue().getCostPerSquareFoot(),
                        entry.getValue().getLaborCostPerSquareFoot(),
                        entry.getValue().getMaterialCost(),
                        entry.getValue().getLaborCost(),
                        entry.getValue().getTax(),
                        entry.getValue().getTotal());
                io.print(orderInfo);
            }
        } else {
            io.print("No orders found with that date");
        }
        io.readString("Please hit enter to continue ");
    }

    // 2. method to add an order
    public void addOrderBanner(){io.print("* * * * * Add an Order * * * * *");}
    public void addOrder() throws FlooringPersistenceException {

        //enter date
        String orderDate = "";
        LocalDate todayDate;
        LocalDate enteredDate;
        do {
            orderDate = io.readString("Please enter a date that you would like this ordered ");
            todayDate = LocalDate.now();
            enteredDate = LocalDate.parse(orderDate, DateTimeFormatter.ofPattern("MMddyyyy"));
        } while (enteredDate.isBefore(todayDate));

        //enter customer name
        String customerName = "";
        do {
            customerName = io.readString("Please enter your name ");
        } while(customerName.length() == 0);

        //enter state abbreviation
        String stateAbbr = "";
        do {
            stateAbbr = io.readString("Please enter your state abbreviation ");
        } while (!orderService.stateIsValid(stateAbbr));

        //enter product type
        //print out all products
        io.print("List of Products");
        List<Product> productList = productDao.getAllProducts();
        for(Product currentProduct : productList){
            String productInfo = String.format("%s %s %s",
                    currentProduct.getProductType(),
                    currentProduct.getCostPerSquareFoot(),
                    currentProduct.getLaborCostPerSquareFoot());
            io.print(productInfo);
        }
        String productType = io.readString("Please choose a product ");

        //enter order
        BigDecimal orderArea = io.readBigDecimal("Please enter your area. Minimum is 100 sq ft ");

        //create a new order
        Order currentOrder = new Order();
        currentOrder.setDate(orderDate);
        currentOrder.setCustomerName(customerName);
        currentOrder.setStateAbbreviation(stateAbbr);
        currentOrder.setProductType(productType);

        //now that we know which product, let's fetch it, so we can get its data
        Product chosenProduct = productDao.getProduct(productType);
        State chosenState = stateDao.getState(stateAbbr);

        currentOrder.setTaxRate(chosenState.getTaxRate());
        currentOrder.setArea(new BigDecimal(String.valueOf(orderArea)));
        currentOrder.setCostPerSquareFoot(new BigDecimal(String.valueOf(chosenProduct.getCostPerSquareFoot())));
        currentOrder.setLaborCostPerSquareFoot(new BigDecimal(String.valueOf(chosenProduct.getLaborCostPerSquareFoot())));

        //calculate the rest of the fields
        orderService.updateCalculatedFields(currentOrder);

        //print out all the fields
        io.print("Date: " + orderDate);
        io.print("Customer Name: " + customerName);
        io.print("State: " + stateAbbr);
        io.print("Product: " + productType);
        io.print("Tax Rate: " + chosenState.getTaxRate());
        io.print("Area: " + orderArea);
        io.print("Cost Per Square Foot: " + chosenProduct.getCostPerSquareFoot());
        io.print("Labor Cost Per Square Foot: " + chosenProduct.getLaborCostPerSquareFoot());
        io.print("Material Cost: " + currentOrder.getMaterialCost());
        io.print("Labor Cost: " + currentOrder.getLaborCost());
        io.print("Total Tax: " + currentOrder.getTax());
        io.print("Total: " + currentOrder.getTotal());

        //print a blank line to separate and give space
        io.print("");

        //ask user if they are sure they want to create this order
        String yesNo = "";
        do {
            yesNo = io.readString("Do you want to create this order? Y/N ");
        } while (!(Objects.equals(yesNo, "y") || Objects.equals(yesNo, "Y") || Objects.equals(yesNo, "n") || Objects.equals(yesNo, "N")));
        if(yesNo.equals("y") || yesNo.equals("Y")) {
            orderDao.createOrder(currentOrder);
            io.print("Thank you for your order");
        } else {
            io.print("Order cancelled");
        }
    }

    // 3. method to edit an order
    public void editOrderBanner(){io.print("* * * * * Edit Your Order * * * * *");}
    public void updateOrder() throws FlooringPersistenceException {
        //put creation of new order at top so fields can be set as we go
        Order updatedOrder = new Order();

        //ask the user for the date and order number
        String getDate = "";
        int getOrderNum;
        Order orderRecord;
        getOrderNum = io.readInt("Please enter your order number ");
        getDate = io.readString("Please enter the date of the order ");
        io.print("");

        //attempt to get that order
        orderRecord = orderDao.getOrder(getOrderNum, getDate);
        if(orderRecord != null){
            //print out current order
            io.print("Date: " + orderRecord.getDate());
            io.print("Customer Name: " + orderRecord.getCustomerName());
            io.print("State: " + orderRecord.getStateAbbreviation());
            io.print("Product: " + orderRecord.getProductType());
            io.print("Tax Rate: " + orderRecord.getTaxRate());
            io.print("Area: " + orderRecord.getArea());
            io.print("Cost Per Square Foot: " + orderRecord.getCostPerSquareFoot());
            io.print("Labor Cost Per Square Foot: " + orderRecord.getLaborCostPerSquareFoot());
            io.print("Material Cost: " + orderRecord.getMaterialCost());
            io.print("Labor Cost: " + orderRecord.getLaborCost());
            io.print("Total Tax: " + orderRecord.getTax());
            io.print("Total: " + orderRecord.getTotal());

            //print a blank line to separate and give space
            io.print("");

            //update customer name
            String currentName = orderRecord.getCustomerName();
            io.print("Current Name: " + currentName);
            String newCustomerName = io.readString("Please enter customer name. If unchanged, hit enter ");
            if(newCustomerName.equals("")){
                updatedOrder.setCustomerName(currentName);
            } else {
                updatedOrder.setCustomerName(newCustomerName);
            }

            //update state abbreviation
            String currentStateAbbr = orderRecord.getStateAbbreviation();
            io.print("Current State: " + currentStateAbbr);
            String newStateAbbr = "";
            do {
                newStateAbbr = io.readString("Please enter your state abbreviation. If unchanged, hit enter ");
                //if the state stays the same, break out of the loop
                if(newStateAbbr.equals("")){
                    break;
                }
            } while (!orderService.stateIsValid(newStateAbbr));
            if(newStateAbbr.equals("")){
                updatedOrder.setStateAbbreviation(currentStateAbbr);
                State chosenState = stateDao.getState(currentStateAbbr);
                updatedOrder.setTaxRate(chosenState.getTaxRate());
            } else{
                updatedOrder.setStateAbbreviation(newStateAbbr);
                State chosenState = stateDao.getState(newStateAbbr);
                updatedOrder.setTaxRate(chosenState.getTaxRate());
            }

            //update product type
            //print out all products
            io.print("List of Products");
            List<Product> productList = productDao.getAllProducts();
            for(Product currentProduct : productList){
                String productInfo = String.format("%s %s %s",
                        currentProduct.getProductType(),
                        currentProduct.getCostPerSquareFoot(),
                        currentProduct.getLaborCostPerSquareFoot());
                io.print(productInfo);
            }
            String currentProduct = orderRecord.getProductType();
            io.print("Current Product Type: " + currentProduct);
            String newProductType = io.readString("Please enter your product type. If unchanged, hit enter ");
            if(newProductType.equals("")){
                updatedOrder.setProductType(currentProduct);
                Product chosenProduct = productDao.getProduct(currentProduct);
                updatedOrder.setCostPerSquareFoot(new BigDecimal(String.valueOf(chosenProduct.getCostPerSquareFoot())));
                updatedOrder.setLaborCostPerSquareFoot(new BigDecimal(String.valueOf(chosenProduct.getLaborCostPerSquareFoot())));
            } else {
                updatedOrder.setProductType(newProductType);
                Product chosenProduct = productDao.getProduct(newProductType);
                updatedOrder.setCostPerSquareFoot(new BigDecimal(String.valueOf(chosenProduct.getCostPerSquareFoot())));
                updatedOrder.setLaborCostPerSquareFoot(new BigDecimal(String.valueOf(chosenProduct.getLaborCostPerSquareFoot())));
            }

            //update area
            BigDecimal currentArea = orderRecord.getArea();
            io.print("Current Area: " + currentArea);
            String newArea = io.readString("Please enter your area. Minimum is 100 sq ft. If unchanged, hit enter  ");
            if(newArea.equals("")){
                updatedOrder.setArea(new BigDecimal(String.valueOf(currentArea)));
            } else{
                updatedOrder.setArea(new BigDecimal((newArea)));
            }

            //set these, so they are part of the update even though they aren't changed
            updatedOrder.setDate(orderRecord.getDate());
            updatedOrder.setOrderNumber(orderRecord.getOrderNumber());

            //update all the calculated fields
            orderService.updateCalculatedFields(updatedOrder);

            //print out all the fields
            io.print("Date: " + updatedOrder.getDate());
            io.print("Customer Name: " + updatedOrder.getCustomerName());
            io.print("State: " + updatedOrder.getStateAbbreviation());
            io.print("Product: " + updatedOrder.getProductType());
            io.print("Tax Rate: " + updatedOrder.getTaxRate());
            io.print("Area: " + updatedOrder.getArea());
            io.print("Cost Per Square Foot: " + updatedOrder.getCostPerSquareFoot());
            io.print("Labor Cost Per Square Foot: " + updatedOrder.getLaborCostPerSquareFoot());
            io.print("Material Cost: " + updatedOrder.getMaterialCost());
            io.print("Labor Cost: " + updatedOrder.getLaborCost());
            io.print("Total Tax: " + updatedOrder.getTax());
            io.print("Total: " + updatedOrder.getTotal());

            //print a blank line to separate and give space
            io.print("");

            //ask person if they are sure they want to create this order
            String yesNo = "";
            do {
                yesNo = io.readString("Do you want to update this order? Y/N ");
            } while (!(Objects.equals(yesNo, "y") || Objects.equals(yesNo, "Y") || Objects.equals(yesNo, "n") || Objects.equals(yesNo, "N")));
            if(yesNo.equals("y") || yesNo.equals("Y")) {
                orderDao.updateOrder(updatedOrder.getOrderNumber(), updatedOrder.getDate(), updatedOrder.getCustomerName(), updatedOrder.getStateAbbreviation(), updatedOrder.getProductType(), updatedOrder.getArea());
                io.print("Your order has been updated");
            } else {
                io.print("Order not updated");
            }
        } else {
            io.print("That order is not in our system. Please hit enter to go back to the main menu ");
        }
    }

    // 4. method to remove an order
    public void removeOrderBanner(){io.print("* * * * * Remove an Order * * * * *");}
    public void removeOrder() throws FlooringPersistenceException {
        String yesNo = "";
        String getDate = "";
        int getOrderNum;
        Order orderRecord;

        //ask the user for the date and order number
        getOrderNum = io.readInt("Please enter your order number ");
        getDate = io.readString("Please enter the date of the order ");
        io.print("");

        //attempt to get that order
        orderRecord = orderDao.getOrder(getOrderNum, getDate);
        if (orderRecord != null) {
            //print out all the fields
            io.print("Date: " + orderRecord.getDate());
            io.print("Customer Name: " + orderRecord.getCustomerName());
            io.print("State: " + orderRecord.getStateAbbreviation());
            io.print("Product: " + orderRecord.getProductType());
            io.print("Tax Rate: " + orderRecord.getTaxRate());
            io.print("Area: " + orderRecord.getArea());
            io.print("Cost Per Square Foot: " + orderRecord.getCostPerSquareFoot());
            io.print("Labor Cost Per Square Foot: " + orderRecord.getLaborCostPerSquareFoot());
            io.print("Material Cost: " + orderRecord.getMaterialCost());
            io.print("Labor Cost: " + orderRecord.getLaborCost());
            io.print("Total Tax: " + orderRecord.getTax());
            io.print("Total: " + orderRecord.getTotal());
            io.print("");

            //ask user if they want to delete this order
            do {
                yesNo = io.readString("Do you want to delete this order? Y/N ");
            } while (!(Objects.equals(yesNo, "y") || Objects.equals(yesNo, "Y") || Objects.equals(yesNo, "n") || Objects.equals(yesNo, "N")));
            if(yesNo.equals("y") || yesNo.equals("Y")) {
                orderDao.deleteOrder(getOrderNum, getDate);
                io.print("Order was successfully removed. ");
            }
        } else {
            io.print("That order number is not in our system. ");
        }
        io.readString("Please hit enter to continue. ");
    }

    // 5. method to export all data

    // 6. exit the program
    public void exitBanner(){io.print("Thank you for ordering, bye!");}
}
