package com.dw.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringUtil {
    public static List<String> splitStringToList(String str, String regex){
        if(str == null){
            return new ArrayList<>();
        }
        String[] array = str.split(regex);
        List<String> list = new ArrayList<>();
        Collections.addAll(list, array);
        return list;
    }
}
