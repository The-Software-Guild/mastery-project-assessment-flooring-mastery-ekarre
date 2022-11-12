package com.ek.flooringmastery.dao;

import com.ek.flooringmastery.dto.State;
import com.ek.flooringmastery.service.FlooringPersistenceException;

import java.math.BigDecimal;
import java.util.Map;

public interface StateDao {

    State createState(String stateAbbreviation, State state) throws FlooringPersistenceException;

    State getState(String stateAbbreviation);

    State updateState(String inputStateAbbr, String inputStateName, BigDecimal inputTaxRate) throws FlooringPersistenceException;

    State deleteState(String stateAbbreviation) throws FlooringPersistenceException;

    Map<String, State> readState() throws FlooringPersistenceException;

    void writeState() throws FlooringPersistenceException;
}