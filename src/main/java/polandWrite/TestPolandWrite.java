package polandWrite;

/**
 * Created by Андрей on 01.04.2015.
 */
public class TestPolandWrite {
    public static void main(String[] args) {
        Calculation calc= new Calculation();


        System.out.println(calc.full("toDollar(toEuro((2.5$+3.5$)*8/4))"));
        System.out.println(calc.endPut.toString());

    }
}
