package polandWrite;

import jdk.nashorn.internal.runtime.Debug;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Андрей on 01.04.2015.
 */
public class Calculation {
    LinkedList inPut = new LinkedList();
    LinkedList endPut = new LinkedList();
    LinkedList<NotNumber> tmpStorage = new LinkedList();
    static final Logger LOGGER= Logger.getLogger("Calculation.class");

    public Number full(String inStr){
        preper(inStr);
        convertToPoland();
        return (Number)endPut.get(0);
    }

    public void preper(String str) {
        str = str.replace("", "");
        str = str.replace(",", "\\.");

        StringBuilder sbNum = new StringBuilder();
        StringBuilder sbCur = new StringBuilder();


        char ch[] = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {

            if (isNum(ch[i]) || isDot(ch[i])) {
                sbNum.append(ch[i]);
            } else if (isOperator(ch[i])) {
                if (sbNum.length() != 0) {
                    Double d = Double.parseDouble(sbNum.toString());
                    inPut.addLast(new Number(d));
                    sbNum.delete(0, sbNum.length());
                }
                inPut.addLast(new Operator(ch[i]));

            } else if (isConversion(ch[i]) && isConversion(ch[i + 1])) {
                if (sbNum.length() != 0) {
                    Double d = Double.parseDouble(sbNum.toString());
                    inPut.addLast(new Number(d));
                    sbNum.delete(0, sbNum.length());
                }

                StringBuilder strb = new StringBuilder();
                for (; ; i++) {
                    strb.append(ch[i]);
                    if (ch[i] == '(') {
                        break;
                    }
                }
                inPut.addLast(new Conversion(new String(strb)));
            } else if (isCurrency(ch[i])) {
                if (sbNum.length() != 0) {
                    Double d = Double.parseDouble(sbNum.toString());
                    inPut.addLast(new Number(d));
                    sbNum.delete(0, sbNum.length());
                }
                inPut.addLast(new Currency(ch[i]));
            } else {
                if (sbNum.length() != 0) {
                    Double d = Double.parseDouble(sbNum.toString());
                    inPut.addLast(new Number(d));
                    sbNum.delete(0, sbNum.length());
                }

                sbCur.append(ch[i]);
                if (sbCur.length() == 3) {
                    inPut.addLast(new Currency(sbCur.toString()));
                    sbCur.delete(0, sbCur.length());
                }
            }
        }
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

    public void convertToPoland() {
        for (int t=0;;t++) {

            g:{if (inPut.size() != 0) {
                if (inPut.getFirst() instanceof Number) {
                    endPut.addLast(inPut.removeFirst());
                } else {
                    if (tmpStorage.size() == 0 & inPut.size()!=0) {
                        tmpStorage.addLast((NotNumber) inPut.removeFirst());
                        continue ;
                    }

                    if (tmpStorage.getLast() instanceof Conversion & inPut.getFirst() instanceof NotNumber) {
                        if(!((NotNumber) inPut.getFirst()).getType().equals(")")) {
                            tmpStorage.addLast((NotNumber) inPut.removeFirst());
                            break g;
                        }
                    }
                    if(inPut.getFirst() instanceof NotNumber) {
                        if (((NotNumber) inPut.getFirst()).getPriority() > tmpStorage.getLast().getPriority()) {
                            tmpStorage.addLast((NotNumber) inPut.removeFirst());
                            break g;
                        }
                    }

                    if (((NotNumber) inPut.getFirst()).getPriority() < tmpStorage.getLast().getPriority()) {
                        endPut.addLast(tmpStorage.removeLast());
                        break g;
                    }

                    if (((NotNumber) inPut.getFirst()).getPriority() == tmpStorage.getLast().getPriority()) {

                        if ((((NotNumber) inPut.getFirst()).getType()).equals(")")) {

                            if (tmpStorage.getLast() instanceof Conversion) {
                                endPut.addLast(tmpStorage.removeLast());
                                inPut.removeFirst();
                                break g;
                            }

                            if ((tmpStorage.getLast().getType()).equals("(")) {
                                tmpStorage.removeLast();
                                inPut.removeFirst();
                                break g;
                            }
                        }
                        endPut.addLast(tmpStorage.removeLast());
                        tmpStorage.addLast((NotNumber) inPut.removeFirst());
                        break g;
                    }
                }

            } else {
                if (tmpStorage.size() != 0) {
                    endPut.addLast(tmpStorage.removeLast());
                }
            }}

            g1:{
            if (endPut.size()!=0 && endPut.getLast() instanceof Operator) {
                if (((((Operator) endPut.getLast()).getType()).equals("+"))) {
                    if (((Number) endPut.get(endPut.size() - 3)).getCurrency()
                            .equals(((Number) endPut.get(endPut.size() - 2)).getCurrency())) {
                        throw new NumberFormatException();
                    }

                    Number number = new Number(((Number) endPut.get(endPut.size() - 3)).getValue()
                            + ((Number) endPut.get(endPut.size() - 2)).getValue());
                    number.setCurrency(((Number) endPut.get(endPut.size() - 3)).getCurrency());
                    endPut.removeLast();
                    endPut.removeLast();
                    endPut.removeLast();
                    endPut.addLast(number);
                    break g1;
                }
                if (((((Operator) endPut.getLast()).getType()).equals("-"))){
                    if (((Number) endPut.get(endPut.size() - 3)).getCurrency()
                            .equals(((Number) endPut.get(endPut.size() - 2)).getCurrency())) {
                        throw new NumberFormatException();
                    }

                    Number number = new Number(((Number) endPut.get(endPut.size() - 3)).getValue()
                            - ((Number) endPut.get(endPut.size() - 2)).getValue());
                    number.setCurrency(((Number) endPut.get(endPut.size() - 3)).getCurrency());
                    endPut.removeLast();
                    endPut.removeLast();
                    endPut.removeLast();
                    endPut.addLast(number);
                    break g1;
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
                        endPut.removeLast();
                        endPut.removeLast();
                        endPut.removeLast();
                        endPut.addLast(number);
                        break g1;
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
                    endPut.removeLast();
                    endPut.removeLast();
                    endPut.removeLast();
                    endPut.addLast(number);
                    break g1;
                 }
            }}

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
    }


    public LinkedList getInPut() {
        return inPut;
    }

    public LinkedList getEndPut() {
        return endPut;
    }

    public boolean isOperator(char x) {
        if (x == '+' || x == '-' || x == '*' || x == '/' || x == '(' || x == ')') {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNum(char x) {
        if (x == '1' || x == '2' || x == '3' || x == '4' || x == '5' || x == '6' || x == '7' || x == '8' || x == '9' || x == '0') {
            return true;
        } else {
            return false;
        }
    }

    public boolean isConversion(char ch) {
        if (ch == 't' || ch == 'o') {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDot(char ch) {
        if (ch == '.') {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCurrency(char c) {
        if (c == '$') {
            return true;
        } else {
            return false;
        }
    }
}
