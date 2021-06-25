package exercise.android.nami.coolcalculator;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class CalculatorHolderTest extends TestCase {

    @Test public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    CalculatorHolder holder = new CalculatorHolder();
    CalculationDetails calc1 = new CalculationDetails(17);
    CalculationDetails calc2 = new CalculationDetails(10);

    @Test public void test_ids() {
        CalculationDetails calc1 = new CalculationDetails(42);
        CalculationDetails calc2 = new CalculationDetails(42);
        assertNotSame(calc1.id, calc2.id);
        assertEquals(calc1.number, calc2.number);
        assertEquals(calc1.status, calc2.status);
        assertNull(calc1.workId);
        assertNull(calc2.workId);
    }

    @Test public void test_adapter_on_init() {
        CalculatorAdapter adapter = new CalculatorAdapter(holder, null, null);
        assertEquals(0, adapter.getItemCount());
        holder.addCalculation(calc1);
        holder.addCalculation(calc2);
        assertEquals(2, adapter.getItemCount());
    }

    @Test public void test_order_of_calculations() {
        if (holder.calculations.size() == 0) {
            test_adapter_on_init();
        }
        CalculationDetails calculationDetails = holder.calculations.get(0);
        assertEquals(10, calculationDetails.number); // smaller goes first
        assertEquals(calculationDetails.status, "in progress");
        assertEquals(-1, calculationDetails.root1);
        assertEquals(-1, calculationDetails.root2);

        calculationDetails = holder.calculations.get(1);
        assertEquals(17, calculationDetails.number);
        assertEquals(calculationDetails.status, "in progress");
        assertEquals(-1, calculationDetails.root1);
        assertEquals(-1, calculationDetails.root2);
    }

}