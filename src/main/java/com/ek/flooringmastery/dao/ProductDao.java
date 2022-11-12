package com.ek.flooringmastery.dao;

import com.ek.flooringmastery.dto.Product;
import com.ek.flooringmastery.service.FlooringPersistenceException;

import java.math.BigDecimal;
import java.util.Map;

public interface ProductDao {

    Product createProduct(String productType, Product product) throws FlooringPersistenceException;

    Product getProduct(String productType);

    Product updateProduct(String updateProductType, BigDecimal updateCost, BigDecimal updateLaborCost) throws FlooringPersistenceException;

    Product deleteProduct(String productType) throws FlooringPersistenceException;

    Map<String, Product> readProduct() throws FlooringPersistenceException;

    void writeProduct() throws FlooringPersistenceException;
}