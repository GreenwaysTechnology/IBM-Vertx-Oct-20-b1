package com.ibm.rx.steortypes;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class SteroTypes {
    public static void observableType() {
        //0..N
        Observable.just(1, 2, 3, 4).subscribe(System.out::println, System.out::println, () -> {
            System.out.println("Completed");
        });
    }

    public static void singleType() {
        //1
        Single.create(subscriber -> {
            subscriber.onSuccess("one");
            subscriber.onSuccess("two");
        }).subscribe(System.out::println, System.out::println);
        Single.create(subscriber -> {
            subscriber.onError(new RuntimeException("error"));
        }).subscribe(System.out::println, System.out::println);

        Single.just(10).subscribe(System.out::println, System.out::println);
    }

    public static void maybeType() {
        //only item
        Maybe.just(1).subscribe(System.out::println);
        //only error
        Maybe.error(new RuntimeException("error")).subscribe(System.out::println, System.out::println);
        //only complete
        Maybe.empty().subscribe(System.out::println, System.out::println, () -> System.out.println("onComplete"));

    }
    //only signal

    public static void completeTest() {
        Completable.complete().subscribe(() -> System.out.println("Completeable"));
    }

    public static void main(String[] args) {
        //observableType();
        // singleType();
        maybeType();
        completeTest();
    }
}
