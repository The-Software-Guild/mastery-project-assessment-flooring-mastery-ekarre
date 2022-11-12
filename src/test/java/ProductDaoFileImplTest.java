import com.ek.flooringmastery.dao.ProductDao;
import com.ek.flooringmastery.dao.ProductDaoFileImpl;
import com.ek.flooringmastery.dto.Product;
import com.ek.flooringmastery.service.FlooringPersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

class ProductDaoFileImplTest {

    public static ProductDao testProductDao;

    @BeforeEach
    void setUp() throws FlooringPersistenceException {
        String testFile = "testFiles/testProductDao.txt";
        testProductDao = new ProductDaoFileImpl(testFile);
    }

    @Test
    void testGetProduct() throws FlooringPersistenceException {
        //create a new product
        Product testProduct1 = new Product();
        testProduct1.setProductType("Tile");
        testProduct1.setCostPerSquareFoot(new BigDecimal("3.50"));
        testProduct1.setLaborCostPerSquareFoot(new BigDecimal("4.15"));

        testProductDao.createProduct(testProduct1.getProductType(), testProduct1);

        //get the product
        testProductDao.getProduct("Tile");

        //assert that the products are equal
        assertEquals("Check the product type", testProduct1.getProductType(), testProductDao.getProduct("Tile").getProductType());
        assertEquals("Check the cost", testProduct1.getCostPerSquareFoot(), testProductDao.getProduct("Tile").getCostPerSquareFoot());
        assertEquals("Check the labor cost", testProduct1.getLaborCostPerSquareFoot(), testProductDao.getProduct("Tile").getLaborCostPerSquareFoot());

        //delete to restore file
        testProductDao.deleteProduct("Tile");
    }

    @Test
    void testUpdateProduct() throws FlooringPersistenceException {
        //create a new product
        Product testProduct1 = new Product();
        testProduct1.setProductType("Wood");
        testProduct1.setCostPerSquareFoot(new BigDecimal("5.15"));
        testProduct1.setLaborCostPerSquareFoot(new BigDecimal("4.75"));

        testProductDao.createProduct(testProduct1.getProductType(), testProduct1);

        //update the new product
        testProductDao.updateProduct("Wood", new BigDecimal("3.50"), new BigDecimal("3.10"));

        //get the updated product
        Product testProduct2 = testProductDao.getProduct("Wood");

        //assert that all the updates are equal
        assertEquals("Check the updated cost", testProduct2.getCostPerSquareFoot(), new BigDecimal("3.50"));
        assertEquals("Check the updated labor cost", testProduct2.getLaborCostPerSquareFoot(), new BigDecimal("3.10"));

        //delete to restore file
        testProductDao.deleteProduct("Wood");
    }

    @Test
    void testReadProduct() throws FlooringPersistenceException {
        //create a local map that will have our test data in it
        Map<String, Product> testMap = new LinkedHashMap<>();

        //this will receive whatever is in the file
        Map<String, Product> fileMap;

        //create test products and put them in the map
        Product testProduct1 = new Product();
        testProduct1.setProductType("Carpet");
        testProduct1.setCostPerSquareFoot(new BigDecimal("2.25"));
        testProduct1.setLaborCostPerSquareFoot(new BigDecimal("2.10"));

        Product testProduct2 = new Product();
        testProduct2.setProductType("Laminate");
        testProduct2.setCostPerSquareFoot(new BigDecimal("1.75"));
        testProduct2.setLaborCostPerSquareFoot(new BigDecimal("2.10"));

        testMap.put("Carpet", testProduct1);
        testMap.put("Laminate", testProduct2);

        //read the file data into a new map (fileMap)
        fileMap = testProductDao.readProduct();

        //assert that the counts are the same and the data is the sam
        assertEquals("Checking the number of items in the map", testMap.size(), fileMap.size());

        //assert that there is a "Carpet" product and that its data matches the test data
        assertEquals("Check data for Carpet", testProduct1.getProductType(), fileMap.get("Carpet").getProductType());
        assertEquals("Check data for cost", testProduct1.getCostPerSquareFoot(), fileMap.get("Carpet").getCostPerSquareFoot());
        assertEquals("Check data for labor cost", testProduct1.getLaborCostPerSquareFoot(), fileMap.get("Carpet").getLaborCostPerSquareFoot());

        //assert that there is a "Laminate" product and that its data matches the test data
        assertEquals("Check data for Laminate", testProduct2.getProductType(), fileMap.get("Laminate").getProductType());
        assertEquals("Check data for cost", testProduct2.getCostPerSquareFoot(), fileMap.get("Laminate").getCostPerSquareFoot());
        assertEquals("Check data for labor cost", testProduct2.getLaborCostPerSquareFoot(), fileMap.get("Laminate").getLaborCostPerSquareFoot());
    }

    //this unit test tests writeProduct, createProduct, and deleteProduct
    @Test
    void testWriteProductCreateProductAndDeleteProduct() throws FlooringPersistenceException {
        //create a local map that will have our test data in it
        Map<String, Product> testMap = new LinkedHashMap<>();

        //this will receive whatever is in the file
        Map<String, Product> fileMap;

        //create test products and put them in the map
        Product testProduct1 = new Product();
        testProduct1.setProductType("Tile");
        testProduct1.setCostPerSquareFoot(new BigDecimal("3.50"));
        testProduct1.setLaborCostPerSquareFoot(new BigDecimal("4.15"));

        testProductDao.createProduct(testProduct1.getProductType(), testProduct1);

        testProductDao.writeProduct();

        fileMap = testProductDao.readProduct();

        //assert that there is a "Tile" product and that its data matches the test data
        assertEquals("Check data for Tile", testProduct1.getProductType(), fileMap.get("Tile").getProductType());
        assertEquals("Check data for Tile", testProduct1.getCostPerSquareFoot(), fileMap.get("Tile").getCostPerSquareFoot());
        assertEquals("Check data for Tile", testProduct1.getLaborCostPerSquareFoot(), fileMap.get("Tile").getLaborCostPerSquareFoot());

        //delete the state we added to restore file
        testProductDao.deleteProduct("Tile");

        //make sure the state was deleted
        fileMap= testProductDao.readProduct();

        assertEquals("Check to make sure file was restored to 2 products", fileMap.size(), 2);
    }
}