import com.ek.flooringmastery.dao.StateDao;
import com.ek.flooringmastery.dao.StateDaoFileImpl;
import com.ek.flooringmastery.dto.Order;
import com.ek.flooringmastery.service.FlooringPersistenceException;
import com.ek.flooringmastery.service.OrderCalculationService;
import com.ek.flooringmastery.service.OrderCalculationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;

class OrderCalculationServiceImplTest {

    public static OrderCalculationService orderService;

    public OrderCalculationServiceImplTest() {
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        orderService = ctx.getBean("service", OrderCalculationServiceImpl.class);
    }

    @BeforeEach
    void setUp() throws FlooringPersistenceException {
        String testFile = "testFiles/testStateDao.txt";
        StateDao stateDao = new StateDaoFileImpl(testFile);
        orderService = new OrderCalculationServiceImpl(stateDao);
    }

    @Test
    void updateCalculatedFields() {
        //create an order
        Order testOrder1 = new Order();
        testOrder1.setOrderNumber(2);
        testOrder1.setCustomerName("Doctor Who");
        testOrder1.setStateAbbreviation("WA");
        testOrder1.setTaxRate(new BigDecimal("9.25"));
        testOrder1.setProductType("Wood");
        testOrder1.setArea(new BigDecimal("243.00"));
        testOrder1.setCostPerSquareFoot(new BigDecimal("5.15"));
        testOrder1.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        testOrder1.setDate("11142022");

        //MaterialCost = (Area * CostPerSquareFoot)   = 243 * 5.15 = 1251.45
        //LaborCost = (Area * LaborCostPerSquareFoot) = 243 * 4.75 = 1154.25
        //Tax = (MaterialCost + LaborCost) * (TaxRate/100) = (1251.45 + 1154.25) * (9.25 / 100) = 216.51
        //Total = (MaterialCost + LaborCost + Tax) = 1251.45 + 1154.25 + 216.51 = 2622.21

        Order testOrder2 = orderService.updateCalculatedFields(testOrder1);

        //assert that the updated fields were calculated correctly
        assertEquals("Check the material cost", testOrder2.getMaterialCost(), new BigDecimal(1251.45).setScale(2, RoundingMode.HALF_UP));
        assertEquals("Check the labor cost", testOrder2.getLaborCost(), new BigDecimal(1154.25).setScale(2, RoundingMode.HALF_UP));
        assertEquals("Check the tax", testOrder2.getTax(), new BigDecimal(216.51).setScale(2, RoundingMode.HALF_UP));
        assertEquals("Check the total", testOrder2.getTotal(), new BigDecimal(2622.21).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void stateIsValid() {
        assertTrue(orderService.stateIsValid("TX"), "Checking that TX exists");
        assertFalse(orderService.stateIsValid("MO"), "Checking that MO doesn't exist");
    }
}