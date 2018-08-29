package com.dw.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {
    public static List<String> splitStringToList(String str, String regex){
        String[] array = str.split(regex);
        List<String> list = new ArrayList<String>();
        for(String s : array){
            list.add(s);
        }
        return list;
    }
}
