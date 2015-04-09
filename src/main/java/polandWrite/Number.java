package polandWrite;

/**
 * Created by Андрей on 01.04.2015.
 */
public class Number {
    private Double value;
    private Currency currency;


    Number(String val){
        System.out.println(val+"df");
        setValue(Double.parseDouble(val));
    }
    Number(Double d){
        setValue(d);
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Number{" +
                "value=" + value +
                ", currency=" + currency +
                '}';
    }


}
