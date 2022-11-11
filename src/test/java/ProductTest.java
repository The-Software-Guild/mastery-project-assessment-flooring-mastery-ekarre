import com.ek.flooringmastery.dto.Product;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    void testProductGettersAndSetters() {
        //create local variables and give them values
        String localProduct = "Carpet";
        BigDecimal localCost = new BigDecimal(2.25);
        BigDecimal localLaborCost = new BigDecimal(3.45);
        //create a new instance of our Product class
        Product testProduct = new Product();
        //set all the values we created
        testProduct.setProductType(localProduct);
        testProduct.setCostPerSquareFoot(localCost);
        testProduct.setLaborCostPerSquareFoot(localLaborCost);
        //assert that the local we created equals what we "get" with getters
        assertEquals(localProduct, testProduct.getProductType(), "Failed Product Type Comparison");
        assertEquals(localCost, testProduct.getCostPerSquareFoot(), "Failed Cost Comparison");
        assertEquals(localLaborCost, testProduct.getLaborCostPerSquareFoot(), "Failed Labor Cost Comparison");
    }
}