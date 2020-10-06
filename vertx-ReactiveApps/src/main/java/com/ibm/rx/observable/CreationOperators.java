package com.ibm.rx.observable;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreationOperators {
    public static void justOperator() {
        Observable<Integer> stream = Observable.just(1, 2, 3, 4, 5, 6, 7);
//        stream.subscribe(data -> System.out.println(data), err -> System.out.println(err), () -> {
//            System.out.println("Completed");
//        });
        stream.subscribe(System.out::println, System.out::println, () -> {
            System.out.println("Completed");
        });
    }

    public static void listOperator() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        Observable<Integer> stream = Observable.fromIterable(list);
        stream.subscribe(System.out::println, System.out::println, () -> {
            System.out.println("Completed");
        });
    }

    public static void arrayOperator() {
        Integer[] array = {1, 2, 3, 4, 5};
        Observable<Integer> stream = Observable.fromArray(array);

        stream.subscribe(System.out::println, System.out::println, () -> {
            System.out.println("Completed");
        });
    }

    public static void rangeOperator() {
        Observable<Integer> stream = Observable.range(1, 100);
        stream.subscribe(System.out::println, System.out::println, () -> {
            System.out.println("Completed");
        });
    }
    //timer

    public static void timerOperator() {
        //interval will emit data after every 10000 ms
        //....1...2...3...4...5
        Observable<Long> stream = Observable.interval(1000, TimeUnit.MILLISECONDS);
        stream.subscribe(System.out::println, System.out::println, () -> {
            System.out.println("Completed");
        });
        //pause main thread so that i can see data emission.
        try {
            Thread.sleep(10000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }


    public static void main(String[] args) {
//        justOperator();
//        listOperator();
//        arrayOperator();
//        rangeOperator();
        timerOperator();
    }
}
