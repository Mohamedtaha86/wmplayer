package model;

public class IDGenerator {
    private static int id=0;

    public static long getNextID(){
        if(id==9999){
            throw new IDOverFlowException();
        }else {
            id++;
            return id-1;
        }
    }


}
