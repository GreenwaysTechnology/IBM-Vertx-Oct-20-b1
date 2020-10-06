package com.ibm.rx.broadcast;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class ColdStream {
    public static void coldStream() throws InterruptedException {
        Observable<Long> myObservable = Observable.interval(1, TimeUnit.SECONDS);

        myObservable.subscribe(item -> System.out.println("Observer 1: " + item));
        //after 3scs new subscriber joins
        Thread.sleep(3000);
        Disposable subscriber2 = myObservable
                .doOnSubscribe((r) -> System.out.println("Observer 2 Joining"))
                .doFinally(() -> System.out.println("Observer 2 left"))
                .subscribe(item -> System.out.println("Observer 2: " + item));

        Thread.sleep(5000);
        subscriber2.dispose();
        Thread.sleep(8000);
    }

    public static void main(String[] args) throws InterruptedException {
        coldStream();
    }
}
