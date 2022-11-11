import com.ek.flooringmastery.dto.Order;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderGettersAndSetters() {
        //create local variables and give them values
        int localOrderNumber = 1;
        String localCustomerName = "Bob McBob";
        String localStateAbbr = "CA";
        BigDecimal localTaxRate = new BigDecimal(25.00);
        String localProductType = "Tile";
        BigDecimal localArea = new BigDecimal(249.00);
        BigDecimal localCostPer = new BigDecimal(3.50);
        BigDecimal localLaborCostPer = new BigDecimal(4.15);
        BigDecimal localMaterialCost = new BigDecimal(871.50);
        BigDecimal localLaborCost = new BigDecimal(1033.35);
        BigDecimal localTax = new BigDecimal(476.21);
        BigDecimal localTotal = new BigDecimal(2381.06);
        String localDate = "07182022";
        //create an instance of the Order class
        Order testOrder = new Order();
        //set all the values we created
        testOrder.setOrderNumber(localOrderNumber);
        testOrder.setCustomerName(localCustomerName);
        testOrder.setStateAbbreviation(localStateAbbr);
        testOrder.setTaxRate(localTaxRate);
        testOrder.setProductType(localProductType);
        testOrder.setArea(localArea);
        testOrder.setCostPerSquareFoot(localCostPer);
        testOrder.setLaborCostPerSquareFoot(localLaborCostPer);
        testOrder.setMaterialCost(localMaterialCost);
        testOrder.setLaborCost(localLaborCost);
        testOrder.setTax(localTax);
        testOrder.setTotal(localTotal);
        testOrder.setDate(localDate);
        //assert that the local we created equals what we "get" with getters
        assertEquals(localOrderNumber, testOrder.getOrderNumber(), "Failed Order Number Comparison");
        assertEquals(localCustomerName, testOrder.getCustomerName(), "Failed Customer Name Comparison");
        assertEquals(localStateAbbr, testOrder.getStateAbbreviation(), "Failed State Abbr Comparison");
        assertEquals(localTaxRate, testOrder.getTaxRate(), "Failed Tax Rate Comparison");
        assertEquals(localProductType, testOrder.getProductType(), "Failed Product Type Comparison");
        assertEquals(localArea, testOrder.getArea(), "Failed Area Comparison");
        assertEquals(localCostPer, testOrder.getCostPerSquareFoot(), "Failed Cost Per Comparison");
        assertEquals(localLaborCostPer, testOrder.getLaborCostPerSquareFoot(), "Failed Labor Cost Per Comparison");
        assertEquals(localMaterialCost, testOrder.getMaterialCost(), "Failed Material Cost Comparison");
        assertEquals(localLaborCost, testOrder.getLaborCost(), "Failed Labor Cost Comparison");
        assertEquals(localTax, testOrder.getTax(), "Failed Tax Comparison");
        assertEquals(localTotal, testOrder.getTotal(), "Failed Total Comparison");
        assertEquals(localDate, testOrder.getDate(), "Failed Date Comparison");
    }
}