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
    public static String getRequestHead(String request){
        return getHeader(request).toString();
    }

    public static boolean checkRequest(String request){
        //Check to xmlns="http://bssys.com/sbns/integration"
        boolean check = true;
        check &= request.contains("xmlns=\"http://bssys.com/sbns/integration\"");
        StringBuilder stringBuilder1 = getHeader(request);
        check &= request.contains("</"+stringBuilder1.toString()+">");
        check &= request.contains("<"+stringBuilder1.toString()+" "+"xmlns=\"http://bssys.com/sbns/integration\""+">");
        return check;
    }

    private static StringBuilder getHeader(String request) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 1 ; i < request.length() && request.charAt(i)!=' ';i ++){
            stringBuilder.append(request.charAt(i));
        }
        return stringBuilder;
    }


}
