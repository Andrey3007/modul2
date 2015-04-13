package polishNotation;

/**
 * Created by Андрей on 01.04.2015.
 */
public class Number {
    private Double value;
    private Currency currency;


   public Number(String val){
        System.out.println(val+"df");
        setValue(Double.parseDouble(val));
    }
   public Number(Double d){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Number number = (Number) o;

        if (currency != null ? !currency.getCurrency().equals(number.currency.getCurrency()) : number.currency != null) return false;
        if (value != null ? !value.equals(number.value) : number.value != null) return false;

        return true;
    }


}
