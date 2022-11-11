import com.ek.flooringmastery.dto.State;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    @Test
    void testStateGettersAndSetters() {
        //create local variables and give them values
        String localAbbr = "MO";
        String localName = "Missouri";
        BigDecimal localTax = new BigDecimal(1.2);
        //create a new instance of our State class
        State testState1 = new State();
        //set all the values we created
        testState1.setStateAbbreviation(localAbbr);
        testState1.setStateName(localName);
        testState1.setTaxRate(localTax);
        //assert that the local we created equals what we "get" with getters
        assertEquals(localAbbr, testState1.getStateAbbreviation(), "Failed State Abbr Comparison");
        assertEquals(localName, testState1.getStateName(), "Failed State Name Comparison");
        assertEquals(localTax, testState1.getTaxRate(), "Failed Tax Rate Comparison");
    }
}