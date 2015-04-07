package polandWrite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Андрей on 04.04.2015.
 */
public class Conversion extends NotNumber{

    private double raw=2;

    Conversion(String type){
        setType(type);
        setPriority(0);
        setRaw(getExRaw());
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
    public void setType(String type) {
        super.setType(type);
    }

    public String toString(){
        return "Conversion" +
                "type='" + getType() + '\'' +
                '}'+"Priority='"+ getPriority()+'\'';
    }

    public double getRaw() {
        return raw;
    }

    public void setRaw(double raw) {
        this.raw = raw;
    }

    public double getExRaw(){
        File file= new File("C:\\Users\\Андрей\\IdeaProjects\\moduul2\\src\\main\\resources\\CurConv");
        Scanner scanner= null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(;;) {

            if(scanner.hasNextLine()){
                StringBuilder s = new StringBuilder(scanner.nextLine());
                if(s.indexOf(getType())!=-1){
                    s.delete(s.indexOf(getType()), s.indexOf(getType())+getType().length());
                    return new Scanner(s.toString()).nextDouble();
                }

            }else{
                return 1;
            }

        }
    }
}
