import com.ek.flooringmastery.dao.OrderDao;
import com.ek.flooringmastery.dao.OrderDaoFileImpl;
import com.ek.flooringmastery.dto.Order;
import com.ek.flooringmastery.service.FlooringPersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

class OrderDaoFileImplTest {

    public static OrderDao testOrderDao;

    @BeforeEach
    void setUp() throws FlooringPersistenceException {
        String testDirectory = "orderTestFiles";
        testOrderDao = new OrderDaoFileImpl(testDirectory);
    }


    @Test
    void getOrderList() {
        //create a map to put items into
        Map<String, Order> testMap = new LinkedHashMap<>();
        Map<String, Order> fileMap;

        //create new items
        //2,Doctor Who,WA,9.25,Wood,243.00,5.15,4.75,1251.45,1154.25,216.51,2622.21
        Order testOrder1 = new Order();
        testOrder1.setOrderNumber(2);
        testOrder1.setCustomerName("Doctor Who");
        testOrder1.setStateAbbreviation("WA");
        testOrder1.setTaxRate(new BigDecimal("9.25"));
        testOrder1.setProductType("Wood");
        testOrder1.setArea(new BigDecimal("243.00"));
        testOrder1.setCostPerSquareFoot(new BigDecimal("5.15"));
        testOrder1.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        testOrder1.setMaterialCost(new BigDecimal("1251.45"));
        testOrder1.setLaborCost(new BigDecimal("1154.25"));
        testOrder1.setTax(new BigDecimal("216.51"));
        testOrder1.setTotal(new BigDecimal("2622.21"));
        testOrder1.setDate("11142022");

        //3,Albert Einstein,KY,6.00,Carpet,217.00,2.25,2.10,488.25,455.70,56.64,1000.59
        Order testOrder2 = new Order();
        testOrder2.setOrderNumber(3);
        testOrder2.setCustomerName("Albert Einstein");
        testOrder2.setStateAbbreviation("KY");
        testOrder2.setTaxRate(new BigDecimal("6.00"));
        testOrder2.setProductType("Carpet");
        testOrder2.setArea(new BigDecimal("217.00"));
        testOrder2.setCostPerSquareFoot(new BigDecimal("2.25"));
        testOrder2.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        testOrder2.setMaterialCost(new BigDecimal("488.25"));
        testOrder2.setLaborCost(new BigDecimal("455.70"));
        testOrder2.setTax(new BigDecimal("56.64"));
        testOrder2.setTotal(new BigDecimal("1000.59"));
        testOrder2.setDate("11142022");

        testMap.put(String.valueOf(2), testOrder1);
        testMap.put(String.valueOf(3), testOrder2);

        fileMap = testOrderDao.getOrderList("11142022");

        assertEquals("Checking that there are 2 items", testMap.size(), fileMap.size());

    }

    @Test
    void getOrder() throws FlooringPersistenceException {
        //create order
        Order testOrder1 = new Order();
        testOrder1.setOrderNumber(5);
        testOrder1.setCustomerName("Stacy McGee");
        testOrder1.setStateAbbreviation("MO");
        testOrder1.setTaxRate(new BigDecimal("2.45"));
        testOrder1.setProductType("Tile");
        testOrder1.setArea(new BigDecimal("100"));
        testOrder1.setCostPerSquareFoot(new BigDecimal("3.67"));
        testOrder1.setLaborCostPerSquareFoot(new BigDecimal("2.75"));
        testOrder1.setMaterialCost(new BigDecimal("367"));
        testOrder1.setLaborCost(new BigDecimal("275"));
        testOrder1.setTax(new BigDecimal("400"));
        testOrder1.setTotal(new BigDecimal("1500"));
        testOrder1.setDate("11142022");

        testOrderDao.createOrder(testOrder1.getOrderNumber(), testOrder1);

        //get the order
        testOrderDao.getOrder(5, "11142022");

        //assert that the values are equal
        assertEquals("Check for order 5", testOrder1.getOrderNumber(), testOrderDao.getOrder(5, "11142022").getOrderNumber());
        assertEquals("Check for order 5", testOrder1.getCustomerName(), testOrderDao.getOrder(5, "11142022").getCustomerName());
        assertEquals("Check for order 5", testOrder1.getStateAbbreviation(), testOrderDao.getOrder(5, "11142022").getStateAbbreviation());
        assertEquals("Check for order 5", testOrder1.getTaxRate(), testOrderDao.getOrder(5, "11142022").getTaxRate());
        assertEquals("Check for order 5", testOrder1.getProductType(), testOrderDao.getOrder(5, "11142022").getProductType());
        assertEquals("Check for order 5", testOrder1.getArea(), testOrderDao.getOrder(5, "11142022").getArea());
        assertEquals("Check for order 5", testOrder1.getCostPerSquareFoot(), testOrderDao.getOrder(5, "11142022").getCostPerSquareFoot());
        assertEquals("Check for order 5", testOrder1.getLaborCostPerSquareFoot(), testOrderDao.getOrder(5, "11142022").getLaborCostPerSquareFoot());
        assertEquals("Check for order 5", testOrder1.getMaterialCost(), testOrderDao.getOrder(5, "11142022").getMaterialCost());
        assertEquals("Check for order 5", testOrder1.getLaborCost(), testOrderDao.getOrder(5, "11142022").getLaborCost());
        assertEquals("Check for order 5", testOrder1.getTax(), testOrderDao.getOrder(5, "11142022").getTax());
        assertEquals("Check for order 5", testOrder1.getTotal(), testOrderDao.getOrder(5, "11142022").getTotal());
        assertEquals("Check for order 5", testOrder1.getDate(), testOrderDao.getOrder(5, "11142022").getDate());

        //delete to restore the file
        testOrderDao.deleteOrder(5, "11142022");
    }

    @Test
    void updateOrder() throws FlooringPersistenceException {
        //create a new order
        Order testOrder1 = new Order();
        testOrder1.setOrderNumber(5);
        testOrder1.setCustomerName("Stacy McGee");
        testOrder1.setStateAbbreviation("MO");
        testOrder1.setTaxRate(new BigDecimal("2.45"));
        testOrder1.setProductType("Tile");
        testOrder1.setArea(new BigDecimal("100"));
        testOrder1.setCostPerSquareFoot(new BigDecimal("3.67"));
        testOrder1.setLaborCostPerSquareFoot(new BigDecimal("2.75"));
        testOrder1.setMaterialCost(new BigDecimal("367"));
        testOrder1.setLaborCost(new BigDecimal("275"));
        testOrder1.setTax(new BigDecimal("400"));
        testOrder1.setTotal(new BigDecimal("1500"));
        testOrder1.setDate("11142022");

        testOrderDao.createOrder(testOrder1.getOrderNumber(), testOrder1);

        //update order
        testOrderDao.updateOrder(5, "11142022", "Stacy McGee", "MO", "Wood", new BigDecimal("100"));

        //get the updated order
        Order testOrder2 = testOrderDao.getOrder(5, "11142022");

        //assert that the changes were made
        assertEquals("Checking the change of product type", testOrder2.getProductType(), "Wood");

        //delete to restore file
        testOrderDao.deleteOrder(5, "11142022");

    }

    @Test
    void readOrder() throws FlooringPersistenceException {
        //create a local map that will have our test data in it
        Map<String, Order> testMap = new LinkedHashMap<>();

        //this will receive whatever is in the file
        Map<String, Order> fileMap;

        //create test products and put them in the map
        //2,Doctor Who,WA,9.25,Wood,243.00,5.15,4.75,1251.45,1154.25,216.51,2622.21
        Order testOrder1 = new Order();
        testOrder1.setOrderNumber(2);
        testOrder1.setCustomerName("Doctor Who");
        testOrder1.setStateAbbreviation("WA");
        testOrder1.setTaxRate(new BigDecimal("9.25"));
        testOrder1.setProductType("Wood");
        testOrder1.setArea(new BigDecimal("243.00"));
        testOrder1.setCostPerSquareFoot(new BigDecimal("5.15"));
        testOrder1.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        testOrder1.setMaterialCost(new BigDecimal("1251.45"));
        testOrder1.setLaborCost(new BigDecimal("1154.25"));
        testOrder1.setTax(new BigDecimal("216.51"));
        testOrder1.setTotal(new BigDecimal("2622.21"));
        testOrder1.setDate("11142022");

        //3,Albert Einstein,KY,6.00,Carpet,217.00,2.25,2.10,488.25,455.70,56.64,1000.59
        Order testOrder2 = new Order();
        testOrder2.setOrderNumber(3);
        testOrder2.setCustomerName("Albert Einstein");
        testOrder2.setStateAbbreviation("KY");
        testOrder2.setTaxRate(new BigDecimal("6.00"));
        testOrder2.setProductType("Carpet");
        testOrder2.setArea(new BigDecimal("217.00"));
        testOrder2.setCostPerSquareFoot(new BigDecimal("2.25"));
        testOrder2.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        testOrder2.setMaterialCost(new BigDecimal("488.25"));
        testOrder2.setLaborCost(new BigDecimal("455.70"));
        testOrder2.setTax(new BigDecimal("56.64"));
        testOrder2.setTotal(new BigDecimal("1000.59"));
        testOrder2.setDate("11142022");

        testMap.put(String.valueOf(2), testOrder1);
        testMap.put(String.valueOf(3), testOrder2);

        //read the file data into a new map (fileMap)
        fileMap = testOrderDao.readOrder();

        //assert that the counts are the same and the data is the same
        assertEquals("Checking the number of items in the map", testMap.size(), fileMap.size());

        //if time, check that the two orders are there along with their correct info
    }

    @Test
    void testWriteOrderCreateOrderAndDeleteOrder() throws FlooringPersistenceException {
        //create a local map that will have our test data in it
        Map<String, Order> testMap = new LinkedHashMap<>();

        //this will receive whatever is in the file
        Map<String, Order> fileMap;

        //create test products and put them in the map
        //2,Doctor Who,WA,9.25,Wood,243.00,5.15,4.75,1251.45,1154.25,216.51,2622.21
        Order testOrder1 = new Order();
        testOrder1.setOrderNumber(5);
        testOrder1.setCustomerName("Stacy McGee");
        testOrder1.setStateAbbreviation("MO");
        testOrder1.setTaxRate(new BigDecimal("2.45"));
        testOrder1.setProductType("Tile");
        testOrder1.setArea(new BigDecimal("100"));
        testOrder1.setCostPerSquareFoot(new BigDecimal("3.67"));
        testOrder1.setLaborCostPerSquareFoot(new BigDecimal("2.75"));
        testOrder1.setMaterialCost(new BigDecimal("367"));
        testOrder1.setLaborCost(new BigDecimal("275"));
        testOrder1.setTax(new BigDecimal("400"));
        testOrder1.setTotal(new BigDecimal("1500"));
        testOrder1.setDate("06182022");

        testOrderDao.createOrder(testOrder1.getOrderNumber(), testOrder1);

        //testOrderDao.writeOrder();

        //fileMap = testOrderDao.readOrder();

        //assert that there is an order and that it's data matches
        assertEquals("Check for order 5", testOrder1.getOrderNumber(), testOrderDao.getOrder(5, "06182022").getOrderNumber());
        assertEquals("Check for order 5", testOrder1.getCustomerName(), testOrderDao.getOrder(5, "06182022").getCustomerName());
        assertEquals("Check for order 5", testOrder1.getStateAbbreviation(), testOrderDao.getOrder(5, "06182022").getStateAbbreviation());
        assertEquals("Check for order 5", testOrder1.getTaxRate(), testOrderDao.getOrder(5, "06182022").getTaxRate());
        assertEquals("Check for order 5", testOrder1.getProductType(), testOrderDao.getOrder(5, "06182022").getProductType());
        assertEquals("Check for order 5", testOrder1.getArea(), testOrderDao.getOrder(5, "06182022").getArea());
        assertEquals("Check for order 5", testOrder1.getCostPerSquareFoot(), testOrderDao.getOrder(5, "06182022").getCostPerSquareFoot());
        assertEquals("Check for order 5", testOrder1.getLaborCostPerSquareFoot(), testOrderDao.getOrder(5, "06182022").getLaborCostPerSquareFoot());
        assertEquals("Check for order 5", testOrder1.getMaterialCost(), testOrderDao.getOrder(5, "06182022").getMaterialCost());
        assertEquals("Check for order 5", testOrder1.getLaborCost(), testOrderDao.getOrder(5, "06182022").getLaborCost());
        assertEquals("Check for order 5", testOrder1.getTax(), testOrderDao.getOrder(5, "06182022").getTax());
        assertEquals("Check for order 5", testOrder1.getTotal(), testOrderDao.getOrder(5, "06182022").getTotal());
        assertEquals("Check for order 5", testOrder1.getDate(), testOrderDao.getOrder(5, "06182022").getDate());

        //delete the order we added to restore file
        testOrderDao.deleteOrder(5, "06182022");

        //make sure it was deleted
        //fileMap = testOrderDao.readOrder();

        //assertEquals("Check to make sure the file was restored to 2", fileMap.size(), 2);
    }
}