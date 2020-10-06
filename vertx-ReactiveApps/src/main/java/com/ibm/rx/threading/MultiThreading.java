package com.ibm.rx.threading;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MultiThreading {
    public static void runINmain() {
        Observable.range(1, 10)
                .map(i -> {
                    System.out.println(Thread.currentThread().getName());
                    return i * 2;
                })
                .doOnSubscribe(c -> System.out.println("on Subscribe"))
                .doOnNext(System.out::println)
                .subscribe(r -> {
                    System.out.println("Subscribe methods " + Thread.currentThread().getName());
                });
    }

    public static void main(String[] args) {
        multiThreading();
    }

    public static void multiThreading() {
        Observable.range(1, 5)
                .map(i -> {
                    System.out.println("map 1 " + Thread.currentThread().getName());
                    return i * 2;
                })
                .subscribeOn(Schedulers.computation())
                .map(i -> {
                    System.out.println("map 2 " + Thread.currentThread().getName());
                    return i * 2;
                })
                .observeOn(Schedulers.newThread())
                .map(i -> {
                    System.out.println("map 3 " + Thread.currentThread().getName());
                    return i * 2;
                })
                .observeOn(Schedulers.io())
                .map(i -> {
                    System.out.println("map 4 " + Thread.currentThread().getName());
                    return i * 2;
                })
                .blockingLast();


    }
}
