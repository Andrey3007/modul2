package polandWrite;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Андрей on 01.04.2015.
 */
public class TestPolandWrite {
    public static void main(String[] args) {
        Calculation calc= new Calculation();


//        System.out.println(calc.full("toDollar(toEuro((2.5usd+3.5usd)*8/4))"));
//        System.out.println(calc.endPut.toString());


        System.out.println(calc.full("toEuro(8$)+6EUR"));
//        calc.preper("toEuro(8$)+6eur");
//        System.out.println(calc.inPut);
//        System.out.println(calc.endPut.toString());




    }
}
