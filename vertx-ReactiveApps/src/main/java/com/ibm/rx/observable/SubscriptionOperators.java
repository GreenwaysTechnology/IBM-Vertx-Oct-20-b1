package com.ibm.rx.observable;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class SubscriptionOperators {
    public static void main(String[] args) {
        Observable
                .range(1, 10)
                .subscribe(System.out::println, System.out::println, () -> System.out.println("done"));
        Observable
                .range(1, 10)
                .map(i -> i * 2)
                .doOnNext(data -> System.out.println(data))
                .doOnError(err -> System.out.println(err))
                .doOnComplete(() -> System.out.println("done"))
                .doOnSubscribe((s) -> System.out.println("subscription"))
                .subscribe();

        Observable
                .interval(1000, TimeUnit.MILLISECONDS)
                .map(i -> i * 2)
                .filter(i -> i % 2 == 0)
                .flatMap(i -> Observable.just(i + "even numbers"))
                .doOnNext(data -> System.out.println(data))
                .doOnError(err -> System.out.println(err))
                .doOnComplete(() -> System.out.println("done"))
                .doOnSubscribe((s) -> System.out.println("subscription"))
                .blockingLast(); // block main thread indefintely

    }
}
