package com.ek.flooringmastery.dao;

import com.ek.flooringmastery.dto.Order;
import com.ek.flooringmastery.service.FlooringPersistenceException;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderDaoFileImpl implements OrderDao{

    private Map<String, Order> orders;
    private Map<String, Integer> orderCounts; //this tracks the number of orders by date
    private int maxID = 0;
    private static final String ORDER_FILE_DIRECTORY_NAME = "orders";
    private String orderFileDirectory;
    private static final String DELIMITER = ",";

    public OrderDaoFileImpl() throws FlooringPersistenceException {
        orderFileDirectory = ORDER_FILE_DIRECTORY_NAME;
        orders = readOrder();

    }

    public OrderDaoFileImpl(String directoryName) throws FlooringPersistenceException {
        orderFileDirectory = directoryName;
        orders = readOrder();

    }

    @Override
    public Order createOrder(Order order) throws FlooringPersistenceException {
        String dateCopy = order.getDate();
        order.setOrderNumber(maxID + 1); //give it the next order number
        Order prevOrder = orders.put(String.valueOf(order.getOrderNumber()), order);
        //increment the maxID to reflect the next value
        maxID += 1;

        //check if this is a new date, if it is put the date into the map
        if(!orderCounts.containsKey(dateCopy)){
            orderCounts.put(dateCopy, 0);
        }
        //increment the counter for this date
        orderCounts.put(dateCopy, orderCounts.get(dateCopy) + 1);
        writeOrder();
        return order;
    }

    @Override
    public Map<String, Order> getOrderList(String date) {
        Map<String, Order> localMap = new LinkedHashMap<>();
        //iterate through the orders map to find entries with the matching date
        //if the date matches, then put it into the local map
        for(Map.Entry<String, Order> entry : orders.entrySet()) {
            if(entry.getValue().getDate().equals(date)) {
                localMap.put(entry.getKey(), entry.getValue());
            }
        }
        return localMap;
    }

    @Override
    public Order getOrder(int orderNumber, String date) {
        return orders.get(String.valueOf(orderNumber));
    }

    @Override
    public Order updateOrder(int orderNumber, String date, String customerName, String stateAbbreviation, String productType, BigDecimal area) throws FlooringPersistenceException {
        Order currentOrder = orders.get(String.valueOf(orderNumber));
        currentOrder.setCustomerName(customerName);
        currentOrder.setStateAbbreviation(stateAbbreviation);
        currentOrder.setProductType(productType);
        currentOrder.setArea(area);
        writeOrder();
        return currentOrder;
    }

    @Override
    public Order deleteOrder(int orderNumber, String date) throws FlooringPersistenceException {
        Order deletedOrder = orders.remove(String.valueOf(orderNumber));
        //decrement the count for the order we just deleted
        //if the count is now 0, then delete the file because we don't need it anymore
        orderCounts.put(deletedOrder.getDate(), orderCounts.get(deletedOrder.getDate()) - 1);
        if(orderCounts.get(deletedOrder.getDate()).equals(0)){
            //delete its file
            File localFile = new File(orderFileDirectory + "/Orders_" + deletedOrder.getDate() + ".txt");
            localFile.delete();
        }
        writeOrder();
        return deletedOrder;
    }

    @Override
    public Map<String, Order> readOrder() throws FlooringPersistenceException {
        orderCounts = new LinkedHashMap<>();
        Scanner scanner;
        Map<String, Order> localOrderMapItems = new LinkedHashMap<>();
        Set<String> orderFileList = getFileList(orderFileDirectory);
        Iterator<String> fileListIterator = orderFileList.iterator();
        while(fileListIterator.hasNext()) {
            String localFileName = fileListIterator.next();
            try{
                scanner = new Scanner(new BufferedReader(new FileReader(orderFileDirectory + "/" + localFileName)));
            } catch (FileNotFoundException e) {
                throw new FlooringPersistenceException("Can't load data", e);
            }

            String currentLine;
            Order currentOrder;
            if(scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                currentOrder = unmarshallOrder(currentLine);
                currentOrder.setDate(localFileName.substring(7,15)); //grab the date portion of the file name
                localOrderMapItems.put(String.valueOf(currentOrder.getOrderNumber()), currentOrder);
                //check if this is a new date, if it is put the date into the map
                if(!orderCounts.containsKey(currentOrder.getDate())){
                    orderCounts.put(currentOrder.getDate(), 0);
                }
                //increment the counter for this date
                orderCounts.put(currentOrder.getDate(), orderCounts.get(currentOrder.getDate()) + 1);
                if(currentOrder.getOrderNumber() > maxID){
                    maxID = currentOrder.getOrderNumber();
                }
            }
            scanner.close();
        }
        return localOrderMapItems;
    }

    @Override
    public void writeOrder() throws FlooringPersistenceException {
        String orderAsText;
        PrintWriter out;
        String[] dates = new String[0];
        //loop through all the orders in the list and write them to the file
        //it needs to have a name of "Orders_MMDDYYYY.txt" and date is taken from what user enters
        for (Map.Entry<String, Order> entry : orders.entrySet()) {
            //check if we have encountered this date before
            if(Arrays.asList(dates).contains(entry.getValue().getDate())) {
                try{
                    out = new PrintWriter(new FileWriter(orderFileDirectory + "/Orders_" + entry.getValue().getDate() + ".txt", true));
                } catch (IOException e) {
                    throw new FlooringPersistenceException("Can't save data", e);
                }
            }
            else {
                try{
                    out = new PrintWriter(new FileWriter(orderFileDirectory + "/Orders_" + entry.getValue().getDate() + ".txt", false));
                } catch (IOException e) {
                    throw new FlooringPersistenceException("Can't save data", e);
                }
                //create a header row for the file
                orderAsText = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";
                out.println(orderAsText);
                String[] newArray = new String[ dates.length + 1 ];
                for (int i=0; i <dates.length; i++) {
                    newArray[i] = dates [i];
                }
                newArray[newArray.length- 1] = entry.getValue().getDate();
                dates = newArray; //adds date to the list of dates
            }
            orderAsText = marshallOrder(entry.getValue());
            out.println(orderAsText);
            out.flush();
            out.close();
        }
    }

    private Order unmarshallOrder(String line){
        String[] orderTokens = line.split(DELIMITER);
        Order orderFromFile = new Order();
        orderFromFile.setOrderNumber(Integer.parseInt(orderTokens[0]));
        orderFromFile.setCustomerName(orderTokens[1]);
        orderFromFile.setStateAbbreviation(orderTokens[2]);
        orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]));
        orderFromFile.setProductType(orderTokens[4]);
        orderFromFile.setArea(new BigDecimal(orderTokens[5]));
        orderFromFile.setCostPerSquareFoot(new BigDecimal(orderTokens[6]));
        orderFromFile.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]));
        orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));
        orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));
        orderFromFile.setTax(new BigDecimal(orderTokens[10]));
        orderFromFile.setTotal(new BigDecimal(orderTokens[11]));
        return orderFromFile;
    }

    private String marshallOrder(Order order){
        String orderAsText;
        orderAsText = order.getOrderNumber() + DELIMITER;
        orderAsText += order.getCustomerName() + DELIMITER;
        orderAsText += order.getStateAbbreviation() + DELIMITER;
        orderAsText += order.getTaxRate() + DELIMITER;
        orderAsText += order.getProductType() + DELIMITER;
        orderAsText += order.getArea() + DELIMITER;
        orderAsText += order.getCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getLaborCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getMaterialCost() + DELIMITER;
        orderAsText += order.getLaborCost() + DELIMITER;
        orderAsText += order.getTax() + DELIMITER;
        orderAsText += order.getTotal();
        return orderAsText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDaoFileImpl)) return false;
        OrderDaoFileImpl that = (OrderDaoFileImpl) o;
        return Objects.equals(orders, that.orders) && Objects.equals(orderFileDirectory, that.orderFileDirectory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders, orderFileDirectory);
    }

    public Set<String> getFileList(String dir) {
        return Stream.of(new File(dir).listFiles())
                       .filter(file -> !file.isDirectory())
                       .map(File::getName)
                       .collect(Collectors.toSet());
    }
}


