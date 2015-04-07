package polandWrite;

/**
 * Created by Андрей on 02.04.2015.
 */
public class Currency {
    private String currency;

    Currency(String currency){
        setCurrency(currency);
    }

    Currency(char c){
        String s =""+ c;
        setCurrency(s);
    }
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "currency='" + currency + '\'' +
                '}';
    }
}
