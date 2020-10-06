package com.ibm.rx.observable.transformation;

import io.reactivex.rxjava3.core.Observable;

public class FilterOperator {
    public static void main(String[] args) {
        Observable.range(1, 100)
                //upstream
                .map(item -> {
                    System.out.println("map is called for " + item);
                    return item * 2;
                })
                //down stream, up stream
                .filter(item -> {
                    System.out.println("filtering " + item);
                    return item > 10;
                })
                //down stream, upstream
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
