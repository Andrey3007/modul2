package polishNotation;

import java.io.FileNotFoundException;

/**
 * Created by Андрей on 04.04.2015.
 */
public class NotNumber {
    private int priority;
    private String type;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) throws FileNotFoundException {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotNumber notNumber = (NotNumber) o;

        if (priority != notNumber.priority) return false;
        if (type != null ? !type.equals(notNumber.type) : notNumber.type != null) return false;

        return true;
    }


}
