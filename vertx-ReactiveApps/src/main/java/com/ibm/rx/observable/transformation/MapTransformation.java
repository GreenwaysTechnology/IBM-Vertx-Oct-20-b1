package com.ibm.rx.observable.transformation;

import io.reactivex.rxjava3.core.Observable;

public class MapTransformation {
    public static void main(String[] args) {
        transform();
    }

    public static void transform() {
//        Observable<Integer> stream = Observable.range(1, 100)
//                .map(item -> {
//                    System.out.println("map is called for " + item);
//                    return item * 2;
//                })
//                .map(newitem -> {
//                    System.out.println("new map is called for " + newitem);
//                    return newitem * 10;
//                });
//        stream.subscribe(data -> {
//            System.out.println("Subscribe" + data);
//        }, System.out::println, () -> {
//            System.out.println("Completed");
//        });
        //using builder pattern

        Observable.range(1, 100)
                .map(item -> {
                    System.out.println("map is called for " + item);
                    return item * 2;
                })
                .map(newitem -> {
                    System.out.println("new map is called for " + newitem);
                    return newitem * 10;
                })
                .subscribe(data -> {
                    System.out.println("Subscribe" + data);
                }, System.out::println, () -> {
                    System.out.println("Completed");
                });
    }
}
