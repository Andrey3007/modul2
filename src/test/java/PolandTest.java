import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import polandWrite.*;
import polandWrite.Number;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

/**
 * Created by Андрей on 05.04.2015.
 */
public class PolandTest extends TestCase {

    @Test
    public void testIsOperator() {
        Calculation calculation = new Calculation();
        boolean b = calculation.isOperator('*');
        if (b != true) Assert.fail();

        b = calculation.isOperator('+');
        if (b != true) Assert.fail();

        b = calculation.isOperator('-');
        if (b != true) Assert.fail();

        b = calculation.isOperator('/');
        if (b != true) Assert.fail();

    }

    @Test
    public void testIsNum() {
        Calculation calculation = new Calculation();
        for (int i = 48; i < 58; i++) {
            char c = (char) i;
            boolean b = calculation.isNum(c);
            if (b != true) Assert.fail();
        }
    }

    @Test
    public void testIsConversion() {
        Calculation calculation = new Calculation();
        boolean b = calculation.isConversion('t');
        if (b != true) Assert.fail();
        b = calculation.isConversion('o');
        if (b != true) Assert.fail();
    }

    @Test
    public void testIsDot() {
        Calculation calculation = new Calculation();
        boolean b = calculation.isDot('.');
        if (b != true) Assert.fail();
    }

    @Test
    public void testIsCurrency() {
        Calculation calculation = new Calculation();
        boolean b = calculation.isCurrency('$');
        if (b != true) Assert.fail();
    }

    @Test
    public void testPreper() {
        Calculation calculation = new Calculation();
        calculation.preper("toEuro(toDollar(8$)+7eur)");
        LinkedList list = calculation.getInPut();
        if (!(list.get(0) instanceof Conversion)) Assert.fail();
        if (!(list.get(1) instanceof Conversion)) Assert.fail();
        if (!(list.get(2) instanceof Number)) Assert.fail();
        if (!(list.get(3) instanceof Operator)) Assert.fail();
        if (!(list.get(4) instanceof Operator)) Assert.fail();
        if (!(list.get(5) instanceof Number)) Assert.fail();
        if (!(list.get(6) instanceof Operator)) Assert.fail();
    }

    @Test
    public void testConvertToPoland() {
        Calculation calc = new Calculation();
        calc.preper("toDollar(toEuro(8$)+8eur)");
        calc.convertToPoland();

        try {
            URL ur1 = new URL("http://quote.yahoo.com/d/quotes.csv?f=l1&s=USDEUR=X");
            URL ur2 = new URL("http://quote.yahoo.com/d/quotes.csv?f=l1&s=EURUSD=X");
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(ur1.openStream()));
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(ur2.openStream()));

            Double d1 = Double.parseDouble(reader1.readLine());
            Double d2 = Double.parseDouble(reader2.readLine());

            double d = (((8 * d1) + 8) * d2);
            d = d * 100;
            d = Math.round(d);
            d = d / 100;

            Number number1 = new Number(d);
            number1.setCurrency(new Currency("USD"));

            Number number2 = (Number) calc.getEndPut().getFirst();

            if (!(number1.getValue().equals(number2.getValue()))) {
                Assert.fail();
            }

            if (!number1.getCurrency().getCurrency().equals("USD")) {
                Assert.fail();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
