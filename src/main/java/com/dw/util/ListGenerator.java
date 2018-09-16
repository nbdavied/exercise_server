package com.dw.util;

import java.util.ArrayList;
import java.util.List;

public class ListGenerator {
    public static List<String> of(String... str){
        List<String> list = new ArrayList<>();
        for (String s : str){
            list.add(s);
        }
        return list;
    }
}
