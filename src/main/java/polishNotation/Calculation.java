package polishNotation;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Андрей on 01.04.2015.
 */
public class Calculation {
    static final Logger LOGGER= Logger.getLogger("Calculation.class");

    public LinkedList preper(String str) {
        LinkedList list = new LinkedList();

        str = str.replace(" ", "");
        str = str.replace(",", "\\.");

        StringBuilder sbNum = new StringBuilder();
        StringBuilder sbCur = new StringBuilder();

        char ch[] = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {

            if (isNum(ch[i]) || isDot(ch[i])) {
                sbNum.append(ch[i]);
            } else if (isOperator(ch[i])) {
                parseNumberAndPut(sbNum, list);
                list.addLast(new Operator(ch[i]));
            } else if (isConversion(ch[i]) && isConversion(ch[i + 1])) {
                parseNumberAndPut(sbNum, list);

                StringBuilder strb = new StringBuilder();
                for (; ; i++) {
                    strb.append(ch[i]);
                    if (ch[i] == '(') {
                        break;
                    }
                }
                list.addLast(new Conversion(new String(strb)));
            } else if (isCurrency(ch[i])) {
                parseNumberAndPut(sbNum, list);
                list.addLast(new Currency(ch[i]));
            } else {
                parseNumberAndPut(sbNum, list);
                sbCur.append(ch[i]);
                if (sbCur.length() == 3) {
                    list.addLast(new Currency(sbCur.toString()));
                    sbCur.delete(0, sbCur.length());
                }
            }
        }
        joinCurrencyWithNumber(list);
        return list;

    }

    private void joinCurrencyWithNumber(List inPut) {
        for (int i = 0; i-1 < inPut.size() - 1; i++) {
            if (inPut.get(i) instanceof Currency) {
                if (inPut.get(i - 1) instanceof Number) {
                    ((Number) inPut.get(i - 1)).setCurrency((Currency) inPut.remove(i));
                    i=0;
                } else {
                    if (inPut.get(i + 1) instanceof Number) {
                        ((Number) inPut.get(i + 1)).setCurrency((Currency) inPut.remove(i));
                        i=0;
                    } else {
                        throw new NumberFormatException();
                    }
                }
            }
        }
    }

    private void parseNumberAndPut(StringBuilder sbNum, LinkedList inPut) {
        if (sbNum.length() != 0) {
            Double d = Double.parseDouble(sbNum.toString());
            inPut.addLast(new Number(d));
            sbNum.delete(0, sbNum.length());
        }
    }

    public Number calculate(String str) {
        LinkedList inPut = preper(str);

        LinkedList endPut = new LinkedList();
        LinkedList<NotNumber> tmpStorage = new LinkedList();
        for (;;) {

            unaryMovement(inPut, endPut, tmpStorage);

            oneCalculation(endPut);

            if (endPut.size()!=0 && endPut.getLast() instanceof Conversion){
                Number number=(Number) endPut.get(endPut.size() - 2);
                Conversion conv= (Conversion)endPut.getLast();
                Number number1= null;
                try {
                    number1 = conv.conversion(number.getCurrency().getCurrency(), conv.getEndCur(), number.getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                endPut.removeLast();
                endPut.removeLast();
                endPut.addLast(number1);
            }
            LOGGER.info(endPut.toString());
            if(endPut.size()==1 & inPut.size()==0 & tmpStorage.size()==0){
                double d= ((Number)endPut.getFirst()).getValue();
                d=d*100;
                int i =(int)Math.round(d);
                d=(double)i/100;
                ((Number)endPut.getFirst()).setValue(d);
                break;
            }
        }
        return (Number)endPut.getFirst();
    }

    private void oneCalculation(LinkedList endPut) {
        if (endPut.size()!=0 && endPut.getLast() instanceof Operator) {
            if (((((Operator) endPut.getLast()).getType()).equals("+"))) {
                if (((Number) endPut.get(endPut.size() - 3)).getCurrency()
                        .equals(((Number) endPut.get(endPut.size() - 2)).getCurrency())) {
                    throw new NumberFormatException();
                }

                Number number = new Number(((Number) endPut.get(endPut.size() - 3)).getValue()
                        + ((Number) endPut.get(endPut.size() - 2)).getValue());
                number.setCurrency(((Number) endPut.get(endPut.size() - 3)).getCurrency());
                EntryList(number, endPut);
                return;
            }
            if (((((Operator) endPut.getLast()).getType()).equals("-"))){
                if (((Number) endPut.get(endPut.size() - 3)).getCurrency()
                        .equals(((Number) endPut.get(endPut.size() - 2)).getCurrency())) {
                    throw new NumberFormatException();
                }

                Number number = new Number(((Number) endPut.get(endPut.size() - 3)).getValue()
                        - ((Number) endPut.get(endPut.size() - 2)).getValue());
                number.setCurrency(((Number) endPut.get(endPut.size() - 3)).getCurrency());
                EntryList(number, endPut);
                return;
            }
            if ((((Operator) endPut.getLast()).getType()).equals("*")){
                if(((Number) endPut.get(endPut.size() - 3)).getCurrency()==null
                        || ((Number) endPut.get(endPut.size() - 2)).getCurrency()==null){
                    Number number = new Number(((Number) endPut.get(endPut.size() - 3)).getValue()
                            * ((Number) endPut.get(endPut.size() - 2)).getValue());
                    if(((Number) endPut.get(endPut.size() - 3)).getCurrency()!=null){
                        number.setCurrency(((Number) endPut.get(endPut.size() - 3)).getCurrency());
                    }
                    if(((Number) endPut.get(endPut.size() - 2)).getCurrency()!=null){
                        number.setCurrency(((Number) endPut.get(endPut.size() - 2)).getCurrency());
                    }
                    EntryList(number, endPut);
                    return;
                 }else{
                    throw new NumberFormatException();
                }
            }

            if((((Operator) endPut.getLast()).getType()).equals("/")){
                if(((Number) endPut.get(endPut.size() - 2)).getCurrency()!=null){
                    throw new NumberFormatException();
                }
                Number number= new Number(((Number) endPut.get(endPut.size() - 3)).getValue()
                        / ((Number) endPut.get(endPut.size() - 2)).getValue());
                number.setCurrency((((Number) endPut.get(endPut.size() - 3)).getCurrency()));
               EntryList(number, endPut);
                return;
             }
        }
    }

    private void EntryList(Number number, LinkedList endPut){
        for(int i=0;i<3;i++ )
            endPut.removeLast();
        endPut.addLast(number);

    }

    private void unaryMovement(LinkedList inPut, LinkedList endPut, LinkedList<NotNumber> tmpStorage) {
        if (inPut.size() != 0) {
            if (inPut.getFirst() instanceof Number) {
                endPut.addLast(inPut.removeFirst());
            } else {
                if (tmpStorage.size() == 0 & inPut.size()!=0) {
                    tmpStorage.addLast((NotNumber) inPut.removeFirst());
                    return;
                }
                if (tmpStorage.getLast() instanceof Conversion & inPut.getFirst() instanceof NotNumber) {
                    if(!((NotNumber) inPut.getFirst()).getType().equals(")")) {
                        tmpStorage.addLast((NotNumber) inPut.removeFirst());
                        return;
                    }
                }
                if(inPut.getFirst() instanceof NotNumber) {
                    if (((NotNumber) inPut.getFirst()).getPriority() > tmpStorage.getLast().getPriority()) {
                        tmpStorage.addLast((NotNumber) inPut.removeFirst());
                        return;
                    }
                }
                if (((NotNumber) inPut.getFirst()).getPriority() < tmpStorage.getLast().getPriority()) {
                    endPut.addLast(tmpStorage.removeLast());
                    return;
                }
                if (((NotNumber) inPut.getFirst()).getPriority() == tmpStorage.getLast().getPriority()) {

                    if ((((NotNumber) inPut.getFirst()).getType()).equals(")")) {

                        if (tmpStorage.getLast() instanceof Conversion) {
                            endPut.addLast(tmpStorage.removeLast());
                            inPut.removeFirst();
                            return;
                        }

                        if ((tmpStorage.getLast().getType()).equals("(")) {
                            tmpStorage.removeLast();
                            inPut.removeFirst();
                            return;
                        }
                    }
                    endPut.addLast(tmpStorage.removeLast());
                    tmpStorage.addLast((NotNumber) inPut.removeFirst());
                    return;
                }
            }

        } else {
            if (tmpStorage.size() != 0) {
                endPut.addLast(tmpStorage.removeLast());
            }
        }
    }

    public boolean isOperator(char x) {
        return (x == '+' || x == '-' || x == '*' || x == '/' || x == '(' || x == ')');
    }

    public boolean isNum(char x) {
        return  (x == '1' || x == '2' || x == '3' || x == '4' || x == '5' || x == '6' || x == '7'
                || x == '8' || x == '9' || x == '0');
    }

    public boolean isConversion(char ch) {
        return  (ch == 't' || ch == 'o');
    }

    public boolean isDot(char ch) {
        return (ch == '.');
    }

    public boolean isCurrency(char c) {
        return (c == '$');
    }
}
