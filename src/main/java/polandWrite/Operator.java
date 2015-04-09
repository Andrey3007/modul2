package polandWrite;

import java.io.FileNotFoundException;

/**
 * Created by Андрей on 01.04.2015.
 */
public class Operator extends NotNumber {


    Operator(char operator) {
        String s = new String();
        s = s + operator;
        setType(s);
        if (operator == '+' || operator == '-') {
            setPriority(1);
        } else if (operator == '*' || operator == '/') {
            setPriority(2);
        } else {
            setPriority(0);

        }
    }

    public int getPriority() {
        return super.getPriority();
    }

    public void setPriority(int priority) {
        super.setPriority(priority);
    }

    public String getType() {
        return super.getType();
    }

    public void setType(String type) {
        try {
            super.setType(type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Operator{" +
                "type='" + getType() + '\'' +
                '}' + "Priority='" + getPriority() + '\'';
    }
}
