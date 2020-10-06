package com.ibm.rx.backpressure;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class BackPressureOperators {
    public static void sampleOpertor() {
        Observable<Integer> observable = Observable
                .range(1, 5000)
                .sample(1, TimeUnit.NANOSECONDS) //back pressure operator
                .map(i -> i); //down stream dont get values from 1 rather
        observable.subscribe(s -> {
            System.out.println("value after every 1 nano secs " + s);
        });
    }
    public static void buffer() {
        Observable.range(1, 2000)
                .buffer(5) //back pressure operator , buffering/storing values and send to down stream
                .map(i -> i) //down stream dont get values from 1 rather than get buffer of 5
                .subscribe((i) -> System.out.println("Item Got :" + i), System.out::println);
    }
    public static void lastN() {
        Observable.range(1, 100)
                .takeLast(50) //back pressure drop all items , expect last 50 values
                .map(i -> i) //down stream dont get values from 1 rather than get buffer of 5
                .subscribe((i) -> System.out.println("Item Got :" + i), System.out::println);
    }

    public static void main(String[] args) {
          // sampleOpertor();
        // buffer();
         lastN();
    }
}
