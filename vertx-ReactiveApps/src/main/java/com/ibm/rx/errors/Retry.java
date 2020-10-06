package com.ibm.rx.errors;

import io.reactivex.rxjava3.core.Observable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Retry {
    public static void main(String[] args) {
        Observable<Long> source = Observable.interval(0, 1, TimeUnit.SECONDS)
                .flatMap(x -> {
                    if (x >= 2) return Observable.error(new IOException("Something went wrong!"));
                    else return Observable.just(x);
                });
        source.retry((retryCount, error) -> {
            System.out.println("retring " + retryCount + " " + error);
            return retryCount < 5;
        })
                .blockingSubscribe(
                        x -> System.out.println("onNext: " + x),
                        error -> System.err.println("onError: " + error.getMessage()));
    }
}
