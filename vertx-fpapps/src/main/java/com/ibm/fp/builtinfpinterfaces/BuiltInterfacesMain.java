package com.ibm.fp.builtinfpinterfaces;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BuiltInterfacesMain {
    public static void main(String[] args) {
        Consumer<String> consumer = null;
        consumer = name -> System.out.println(name);
        consumer.accept("Subramanian");
        consumer = System.out::println;
        consumer.accept("Subramanian");
        ///////////////////////////////////////////////////////////////
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //functional iteratore
        list.forEach(item -> System.out.println(item));
        list.forEach(System.out::println);

        Supplier<String> stringSupplier = null;
        stringSupplier = () -> "Hello";
        System.out.println(stringSupplier.get());

        Predicate<Integer> predicate = null;
        predicate = num -> num > 10;
        System.out.println(predicate.test(2));
        System.out.println(predicate.test(20));

        //Function
        Function<Integer, Integer> function = null;
        function = num -> num * 2;
        System.out.println(function.apply(10));

    }
}
