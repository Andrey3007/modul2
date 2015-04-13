import org.junit.Assert;
import org.junit.Test;
import polishNotation.*;
import polishNotation.Number;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

/**
 * Created by Андрей on 05.04.2015.
 */
public class CalculationTest {

    @Test
    public void testIsOperator() {
        Calculation calculation = new Calculation();
        boolean b = calculation.isOperator('*');
        Assert.assertTrue(b);

        b = calculation.isOperator('+');
        Assert.assertTrue(b);

        b = calculation.isOperator('-');
        Assert.assertTrue(b);

        b = calculation.isOperator('/');
        Assert.assertTrue(b);

    }

    @Test
    public void testIsNum() {
        Calculation calculation = new Calculation();
        for (int i = 48; i < 58; i++) {
            char c = (char) i;
            boolean b = calculation.isNum(c);
            Assert.assertTrue(b);
        }
    }

    @Test
    public void testIsConversion() {
        Calculation calculation = new Calculation();
        boolean b = calculation.isConversion('t');
        Assert.assertTrue(b);
        b = calculation.isConversion('o');
        Assert.assertTrue(b);
    }

    @Test
    public void testIsDot() {
        Calculation calculation = new Calculation();
        boolean b = calculation.isDot('.');
        Assert.assertTrue(b);
    }

    @Test
    public void testIsCurrency() {
        Calculation calculation = new Calculation();
        boolean b = calculation.isCurrency('$');
        Assert.assertTrue(b);
    }

    @Test
    public void testPreper() {
        Calculation calculation = new Calculation();


        LinkedList list1 = new LinkedList();
        list1.addLast(new Conversion("toEuro("));
        list1.addLast(new Conversion("toDollar("));
        Number number1 = new Number(8.0);
        number1.setCurrency(new Currency("$"));
        list1.addLast(number1);
        list1.addLast(new Operator(')'));
        list1.addLast(new Operator('+'));
        Number number2 = new Number(7.0);
        number2.setCurrency(new Currency("eur"));
        list1.addLast(number2);
        list1.addLast(new Operator(')'));
        LinkedList list2 = calculation.preper("toEuro(toDollar(8$)+7eur)");
        System.out.println(list1.toString());
        System.out.println(list2.toString());
        for(int i=0;i<list1.size();i++) {
            Assert.assertTrue(list1.get(i).equals(list2.get(i)));
        }
    }
    @Test
    public void testConvertToPoland() {
        Calculation calc = new Calculation();

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

            Number number2 = calc.calculate("toDollar(toEuro(8$)+8eur)");

            Assert.assertTrue((number1.getValue().equals(number2.getValue())));

            Assert.assertTrue(number1.getCurrency().getCurrency().equals("USD"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
