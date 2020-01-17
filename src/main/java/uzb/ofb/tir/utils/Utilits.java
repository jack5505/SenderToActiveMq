package uzb.ofb.tir.utils;

public class Utilits {

    public static String cdd(String path){
        String cd = path;
        StringBuilder builder = new StringBuilder(cd);
        String temp =  builder.reverse().toString();
        int pos = 0;
        for(int i = 1 ; i < temp.length();i ++){
            if(temp.charAt(i) =='/'){
                pos = i;
                break;
            }
        }
        temp = temp.substring(pos);
        StringBuilder b = new StringBuilder(temp);
        return b.reverse().toString();
    }
}
