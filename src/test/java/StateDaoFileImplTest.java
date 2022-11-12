import com.ek.flooringmastery.dao.StateDao;
import com.ek.flooringmastery.dao.StateDaoFileImpl;
import com.ek.flooringmastery.dto.State;
import com.ek.flooringmastery.service.FlooringPersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class StateDaoFileImplTest {

    public static StateDao testStateDao;

    @BeforeEach
    void setUp() throws FlooringPersistenceException {
        String testFile = "testFiles/testStateDao.txt";
        testStateDao = new StateDaoFileImpl(testFile);
    }

    @Test
    void testGetState() throws FlooringPersistenceException {
        //create a new state
        State testState1 = new State();
        testState1.setStateAbbreviation("CO");
        testState1.setStateName("Colorado");
        testState1.setTaxRate(new BigDecimal(12.45));

        testStateDao.createState(testState1.getStateAbbreviation(), testState1);

        //get the state
        testStateDao.getState("CO");

        //assert that the states are equal
        assertEquals("Check the state abbreviation", testState1.getStateAbbreviation(), testStateDao.getState("CO").getStateAbbreviation());
        assertEquals("Check the state name", testState1.getStateName(), testStateDao.getState("CO").getStateName());
        assertEquals("Check the state tax rate", testState1.getTaxRate(), testStateDao.getState("CO").getTaxRate());

        //delete to restore file
        testStateDao.deleteState("CO");
    }

    @Test
    void testUpdateState() throws FlooringPersistenceException {
        //create a new state
        State testState1 = new State();
        testState1.setStateAbbreviation("IA");
        testState1.setStateName("Iowa");
        testState1.setTaxRate(new BigDecimal(6.34));

        testStateDao.createState(testState1.getStateAbbreviation(), testState1);

        //update the new state
        testStateDao.updateState("IA", "Iowa2", new BigDecimal(10.34));

        //get the state we just updated
        State testState2 = testStateDao.getState("IA");

        //assert that all the updates are equal
        assertNotEquals("Check that the names are different", testState1.getStateName(), testState2.getStateName());
        assertEquals("Check the updated state name", testState2.getStateName(), "Iowa2");
        //assertNotEquals("Check that the tax rates are different", testState1.getTaxRate(), testState2.getTaxRate());
        assertEquals("Check the updated tax rate", testState2.getTaxRate(), new BigDecimal(10.34).setScale(2, RoundingMode.HALF_UP));

        //delete to restore the file
        testStateDao.deleteState("IA");
    }

    @Test
    void testReadState() throws FlooringPersistenceException {
        //creating a local map that will have our test data in it
        Map<String, State> testMap = new LinkedHashMap<>();
        //this will receive whatever is in the file
        Map<String, State> fileMap;

        //create test states and put them in the map
        //TX,Texas,4.45
        //WA,Washington,9.25
        State testState1 = new State();
        testState1.setStateAbbreviation("TX");
        testState1.setStateName("Texas");
        testState1.setTaxRate(new BigDecimal(4.45));

        State testState2 = new State();
        testState2.setStateAbbreviation("WA");
        testState2.setStateName("Washington");
        testState2.setTaxRate(new BigDecimal(9.25));

        testMap.put("TX", testState1);
        testMap.put("WA", testState2);

        //now we read the file data into a new map (fileMap)
        fileMap = testStateDao.readState();

        //now the states in the testMap should be the same as the states in the fileMap
        //assert that the counts are the same and the data is the same
        assertEquals("Checking the number of items in the map", testMap.size(), fileMap.size());

        //assert that there is a "Texas" state and that its data matches the test data
        assertEquals("Check data for state abbr", testState1.getStateAbbreviation(), fileMap.get("TX").getStateAbbreviation());
        assertEquals("Check data for state name", testState1.getStateName(), fileMap.get("TX").getStateName());
        assertEquals("Check data for tax rate", testState1.getTaxRate(), fileMap.get("TX").getTaxRate());

        //assert that there is a "Washington" state and that its data matches the test data
        assertEquals("Check data for state abbr", testState2.getStateAbbreviation(), fileMap.get("WA").getStateAbbreviation());
        assertEquals("Check data for state name", testState2.getStateName(), fileMap.get("WA").getStateName());
        assertEquals("Check data for tax rate", testState2.getTaxRate(), fileMap.get("WA").getTaxRate());
    }

    //this unit test tests writeState, createState, and deleteState
    @Test
    void testWriteStateCreateStateAndDeleteState() throws FlooringPersistenceException {
        //creating a local map that will have our test data in it
        Map<String, State> testMap = new LinkedHashMap<>();
        //this will receive whatever is in the file
        Map<String, State> fileMap;

        //create test states and put them in the map
        State testState1 = new State();
        testState1.setStateAbbreviation("IA");
        testState1.setStateName("Iowa");
        testState1.setTaxRate(new BigDecimal(6.34));

        testStateDao.createState(testState1.getStateAbbreviation(), testState1);

        testStateDao.writeState();

        fileMap = testStateDao.readState();

        //assert that there is an "Iowa" state and that its data matches the test data
        assertEquals("Check data for Iowa", testState1.getStateAbbreviation(), fileMap.get("IA").getStateAbbreviation());
        assertEquals("Check data for Iowa", testState1.getStateName(), fileMap.get("IA").getStateName());
        assertEquals("Check data for Iowa", testState1.getTaxRate(), fileMap.get("IA").getTaxRate());

        //now delete the state we just added to fix our file back up
        testStateDao.deleteState(testState1.getStateAbbreviation());

        //make sure the state was deleted
        fileMap = testStateDao.readState();

        assertEquals("Check to make sure file map has 2 states in it", fileMap.size(), 2);
    }
}