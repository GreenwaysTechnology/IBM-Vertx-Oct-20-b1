package com.ibm.rx.observable;

import io.reactivex.rxjava3.core.Observable;

public class CreateStream {
    public static void main(String[] args) {
        //Publisher
        Observable<String> stream = Observable.create(subscriber -> {
            boolean isRightUser = true;
            try {
                if (!isRightUser) {
                    //start pushing data
                    subscriber.onNext("Govt announced that coming week end Nobody should come out");
                    subscriber.onNext("Chennai Super Kings beat DEL");
                    subscriber.onComplete();
                    subscriber.onNext("foo message"); // data will not be send to subscriber because stream already closed
                } else {
                    throw new RuntimeException("SOmething went wrong");
                }

            } catch (Exception e) {
                subscriber.onError(e);
                // data will not be send to subscriber because stream already closed
                subscriber.onNext("bar");

            }

        });

        //subscriber; listen for data,error,complete
        stream.subscribe(data -> System.out.println(data), err -> System.out.println(err), () -> {
            System.out.println("Completed");
        });
    }
}
