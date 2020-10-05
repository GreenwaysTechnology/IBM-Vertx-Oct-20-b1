package com.ibm.fp.codestyles;

import java.util.Arrays;
import java.util.List;

public class Imperative {
    public static void main(String[] args) {
        //Imperative style coding

        List<String> names = Arrays.asList("Dory", "Gill", "Bruce", "Nemo", "Darla", "Marlin", "Jacques");
        //find a Nemo in the list;
        // imperative
        boolean found = false;
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals("Nemo")) {
                found = true;
                break;
            }
        }
        if (found)
            System.out.println("Nemo found");
        else
            System.out.println("Nemo not found");

    }
}
