package online.referity.utils;

public class  Helper {

    public static String genaralCode ( String key , int number){
        return String.format(key + "%04d" , number);
    }
}
