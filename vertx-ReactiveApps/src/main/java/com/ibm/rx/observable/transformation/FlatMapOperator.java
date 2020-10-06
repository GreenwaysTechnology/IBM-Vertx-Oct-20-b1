package com.ibm.rx.observable.transformation;

import io.reactivex.rxjava3.core.Observable;

public class FlatMapOperator {
    public static void main(String[] args) {
//        Observable.just("A", "B", "C").map(String::toLowerCase).subscribe(
//                System.out::println, System.out::println, () -> {
//                    System.out.println("done");
//                }
//        );
        //looks like nested array
        Observable.just("A", "B", "C").flatMap(item -> {
            System.out.println(item);
            return Observable.just(1, 2, 3);
        }).subscribe(
                System.out::println, System.out::println, () -> {
                    System.out.println("done");
                }
        );
    }
}
