package polandWrite;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Андрей on 04.04.2015.
 */
public class Conversion extends NotNumber {

    private double raw = 1;
    public String endCur;

    Conversion(String type) {
        try {
            setType(type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            setEndCur(type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setPriority(0);
    }

    @Override
    public int getPriority() {
        return super.getPriority();
    }

    @Override
    public void setPriority(int priority) {
        super.setPriority(priority);
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public void setType(String type) throws FileNotFoundException {
        super.setType(type);
    }

    public String toString() {
        return "Conversion" +
                "type='" + getType() + '\'' +
                '}' + "Priority='" + getPriority() + '\'';
    }

    public double getRaw() {
        return raw;
    }

    public void setRaw(double raw) {
        this.raw = raw;
    }

    public double getExRaw() {
        File file = new File("C:\\Users\\Андрей\\IdeaProjects\\moduul2\\src\\main\\resources\\CurConv");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (; ; ) {

            if (scanner.hasNextLine()) {
                StringBuilder s = new StringBuilder(scanner.nextLine());
                if (s.indexOf(getType()) != -1) {
                    s.delete(s.indexOf(getType()), s.indexOf(getType()) + getType().length());
                    return new Scanner(s.toString()).nextDouble();
                }

            } else {
                return 1;
            }

        }
    }

    public String getEndCur() {
        return endCur;
    }

    public void setEndCur(String cur) throws FileNotFoundException {
        InputStream in = new FileInputStream(
                new File("C:\\Users\\Андрей\\IdeaProjects\\moduul2\\src\\main\\resources\\AvailableCurrencies1.xls"));


        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell1;
        HSSFCell cell2;

        int rows;
        rows = sheet.getPhysicalNumberOfRows();

        for (int i = 0; i < rows; i++) {
            row = sheet.getRow(i);
            cell1 = row.getCell(2);
            if (cur.equals(cell1.toString())) {
                cell2 = row.getCell(1);
                this.endCur = (cell2.toString());
                return;
            }
        }
        throw new NumberFormatException();
    }

    public Number conversion(String inCur, String outCur, double valueIn) throws IOException {
        InputStream in = new FileInputStream(
                new File("C:\\Users\\Андрей\\IdeaProjects\\moduul2\\src\\main\\resources\\AvailableCurrencies1.xls"));
        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell1;
        HSSFCell cell2;

        int rows;
        rows = sheet.getPhysicalNumberOfRows();
        String inS = "";
        for (int i = 0; i < rows; i++) {
            row = sheet.getRow(i);
            cell1 = row.getCell(0);
            if (inCur.equals(cell1.toString())) {
                cell2 = row.getCell(1);
                inS = (cell2.toString());
            }
        }
        URL url = null;


        try {
            url = new URL("http://quote.yahoo.com/d/quotes.csv?f=l1&s=" + inS + endCur + "=X");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;


        reader = new BufferedReader(new InputStreamReader(url.openStream()));


        String line = reader.readLine();
        Double d = Double.parseDouble(line);
        Number number = new Number(valueIn * d);
        number.setCurrency(new Currency(endCur));
        return number;


    }
}
