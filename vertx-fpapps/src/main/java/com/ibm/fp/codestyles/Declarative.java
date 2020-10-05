package com.ibm.fp.codestyles;

import java.util.Arrays;
import java.util.List;

public class Declarative {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Dory", "Gill", "Bruce", "Nemo", "Darla", "Marlin", "Jacques");
        //java provides an api called streams;
        names.stream().filter(name -> name.equals("Nemo")).forEach(name -> {
            System.out.println(name);
        });

    }
}
