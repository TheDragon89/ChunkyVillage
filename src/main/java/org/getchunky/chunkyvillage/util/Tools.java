package org.getchunky.chunkyvillage.util;

public class Tools {

    public static double parseDouble(String s) {
        try{
            return Double.parseDouble(s);
        } catch(Exception ex) {
            return -1;
        }
    }
}
