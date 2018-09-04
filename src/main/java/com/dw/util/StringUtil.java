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
    public static List<Integer> splitStringToIntList(String str, String regex){
        if(str == null){
            return new ArrayList<>();
        }
        String[] array = str.split(regex);
        List<Integer> list = new ArrayList<>();
        for (String s : array){
            Integer i = Integer.parseInt(s);
            list.add(i);
        }
        return list;
    }
    public static String combineListToString(List<String> list, String splitter){
        StringBuilder sb = new StringBuilder();
        for(String s : list){
            sb.append(s);
            sb.append(splitter);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
