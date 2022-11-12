package com.ek.flooringmastery.dao;

import com.ek.flooringmastery.dto.Product;
import com.ek.flooringmastery.service.FlooringPersistenceException;

import java.io.*;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class ProductDaoFileImpl implements ProductDao {

    private Map<String, Product> products;

    private static final String PRODUCT_FILE_NAME = "data/products.txt";

    private String productFileName;

    private static final String DELIMITER = ",";

    public ProductDaoFileImpl() throws FlooringPersistenceException {
        productFileName = PRODUCT_FILE_NAME;
        products = readProduct();
    }

    public ProductDaoFileImpl(String productTextFile) throws FlooringPersistenceException {
        productFileName = productTextFile;
        products = readProduct();
    }

    @Override
    public Product createProduct(String productType, Product product) throws FlooringPersistenceException {
        Product newProduct = products.put(productType, product);
        writeProduct();
        return newProduct;
    }

    @Override
    public Product getProduct(String productType) {
        return products.get(productType);
    }

    @Override
    public Product updateProduct(String updateProductType, BigDecimal updateCost, BigDecimal updateLaborCost) throws FlooringPersistenceException {
        Product currentProduct = products.get(updateProductType);
        currentProduct.setProductType(updateProductType);
        currentProduct.setCostPerSquareFoot(updateCost);
        currentProduct.setLaborCostPerSquareFoot(updateLaborCost);
        writeProduct();
        return currentProduct;
    }

    @Override
    public Product deleteProduct(String productType) throws FlooringPersistenceException {
        Product removedProduct = products.remove(productType);
        writeProduct();
        return removedProduct;
    }

    @Override
    public Map<String, Product> readProduct() throws FlooringPersistenceException {
        Scanner scanner;
        Map<String, Product> localProductMapItems = new LinkedHashMap<>();
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(productFileName)));
        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException("Can't load data", e);
        }
        String currentLine;
        Product currentProduct;
        if (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
        }
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            localProductMapItems.put(currentProduct.getProductType(), currentProduct);
        }
        scanner.close();
        return localProductMapItems;
    }

    @Override
    public void writeProduct() throws FlooringPersistenceException {
        PrintWriter out;
        try{
            out = new PrintWriter(new FileWriter(productFileName));
        } catch (IOException e) {
            throw new FlooringPersistenceException("Can't save data", e);
        }
        String productAsText;

        //create a header row for the file
        productAsText = "ProductType,CostPerSquareFoot,LaborCostPerSquareFoot";
        out.println(productAsText);
        out.flush();

        //loop through all the state in the list and write them to the file
        for (Map.Entry<String, Product> entry : products.entrySet()) {
            productAsText = marshallProduct(entry.getValue());
            out.println(productAsText);
            out.flush();
        }
        out.close();
    }

    private Product unmarshallProduct(String line) {
        String[] productTokens = line.split(DELIMITER);
        Product productFromFile = new Product();
        productFromFile.setProductType(productTokens[0]);
        productFromFile.setCostPerSquareFoot(new BigDecimal(productTokens[1]));
        productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));
        return productFromFile;
    }

    private String marshallProduct(Product product) {
        String productAsText;
        productAsText = product.getProductType() + DELIMITER;
        productAsText += product.getCostPerSquareFoot() + DELIMITER;
        productAsText += product.getLaborCostPerSquareFoot();
        return productAsText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDaoFileImpl)) return false;
        ProductDaoFileImpl that = (ProductDaoFileImpl) o;
        return Objects.equals(products, that.products) && Objects.equals(productFileName, that.productFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products, productFileName);
    }
}
