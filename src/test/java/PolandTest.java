import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import polandWrite.*;
import polandWrite.Number;

/**
 * Created by Андрей on 05.04.2015.
 */
public class PolandTest extends TestCase {

    @Test
    public void testIsOperator(){
        Calculation calculation= new Calculation();
        boolean b= calculation.isOperator('*');
        if(b!=true)Assert.fail();
    }
    @Test
    public void testFail(){
        Calculation calculation= new Calculation();
        boolean b= calculation.isOperator('*');
        if(b==true)Assert.fail();
    }

    @Test
    public void testAll(){
        Calculation calculation= new Calculation();
        Number number= calculation.full("toDollar(toEuro((2.5$+3.5$)*8/4))");
        if(number.getValue()!= 600 & number.getCurrency().equals("$"))Assert.fail();
    }


}
