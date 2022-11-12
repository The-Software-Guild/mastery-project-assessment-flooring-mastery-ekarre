package com.ek.flooringmastery.dao;

import com.ek.flooringmastery.dto.State;
import com.ek.flooringmastery.service.FlooringPersistenceException;

import java.io.*;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class StateDaoFileImpl implements StateDao {

    private Map<String, State> states;
    private static final String STATE_FILE_NAME = "data/taxes.txt";

    private String fileName;

    private static final String DELIMITER = ","; //we will be working with a csv file

    public StateDaoFileImpl(String stateTextFile) throws FlooringPersistenceException {
        fileName = stateTextFile;
        states = readState();
    }
    public StateDaoFileImpl() throws FlooringPersistenceException {
        fileName = STATE_FILE_NAME;
        states = readState();
    }

    @Override
    public State createState(String stateAbbreviation, State state) throws FlooringPersistenceException {
        State newState = states.put(stateAbbreviation, state);
        writeState();
        return newState;
    }

    @Override
    public State getState(String stateAbbreviation){
        return states.get(stateAbbreviation);
    }

    @Override
    public State updateState(String inputStateAbbr, String inputStateName, BigDecimal inputTaxRate) throws FlooringPersistenceException {
        State currentState = states.get(inputStateAbbr);
        currentState.setStateAbbreviation(inputStateAbbr);
        currentState.setStateName(inputStateName);
        currentState.setTaxRate(inputTaxRate);
        writeState();
        return currentState;
    }

    @Override
    public State deleteState(String stateAbbreviation) throws FlooringPersistenceException {
        State removedState = states.remove(stateAbbreviation);
        writeState();
        return removedState;
    }

    @Override
    public Map<String, State> readState() throws FlooringPersistenceException{
        Scanner scanner;
        Map<String, State> localStateMapItems = new LinkedHashMap<>();
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(fileName)));
        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException("Can't load data", e);
        }
        String currentLine;
        State currentState;
        if (scanner.hasNextLine()) {
            currentLine = scanner.nextLine(); //skip the header row
        }
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentState = unmarshallState(currentLine);
            localStateMapItems.put(currentState.getStateAbbreviation(), currentState);
        }
        scanner.close();
        return localStateMapItems;
    }

    @Override
    public void writeState()  throws FlooringPersistenceException {
        PrintWriter out;
        try{
            out = new PrintWriter(new FileWriter(fileName));
        } catch (IOException e) {
            throw new FlooringPersistenceException("Can't save data", e);
        }
        String stateAsText;

        //create a header row for the file
        stateAsText = "State,StateName,TaxRate";
        out.println(stateAsText);
        out.flush();

        //loop through all the states in the list and write them to the file
        for (Map.Entry<String, State> entry : states.entrySet()) {
            stateAsText = marshallState(entry.getValue());
            out.println(stateAsText);
            out.flush();
        }
        out.close();
    }

    private State unmarshallState(String line) {
        String[] stateTokens = line.split(DELIMITER);
        State stateFromFile = new State();
        stateFromFile.setStateAbbreviation(stateTokens[0]);
        stateFromFile.setStateName(stateTokens[1]);
        stateFromFile.setTaxRate(new BigDecimal(stateTokens[2]));
        return stateFromFile;
    }

    private String marshallState(State state) {
        String stateAsText;
        stateAsText = state.getStateAbbreviation() + DELIMITER;
        stateAsText += state.getStateName() + DELIMITER;
        stateAsText += String.valueOf(state.getTaxRate());
        return stateAsText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateDaoFileImpl)) return false;
        StateDaoFileImpl that = (StateDaoFileImpl) o;
        return Objects.equals(states, that.states) && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(states, fileName);
    }
}
