package polandWrite;




import java.util.Scanner;

/**
 * Created by Андрей on 01.04.2015.
 */
public class TestPolandWrite {
    public static void main(String[] args) {
        Calculation calc= new Calculation();



calc.preper("toDollar(toEuro((2.5usd+3.5usd)*8/4))");
        System.out.println(calc.inPut);

//        Scanner scanner= new Scanner(System.in);
//        System.out.println("enter expr");
//        String s= scanner.nextLine();                    //        "toDollar(toEuro((2.5usd+3.5usd)*8/4))";
//
//        System.out.println(calc.full(s));



    }
}
