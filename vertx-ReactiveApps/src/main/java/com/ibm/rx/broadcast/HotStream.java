package com.ibm.rx.broadcast;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

public class HotStream {
    public static void hotStream() throws InterruptedException {
        //source stream.
        Observable<Long> myObservable = Observable.interval(1, TimeUnit.SECONDS);
        //convert cold into hot
        ConnectableObservable<Long> connectableObservable = myObservable.publish();
        //try to connect all subscribers under one single unit.
        connectableObservable.connect();

        connectableObservable
                .doOnSubscribe((r) -> System.out.println("Observer 1 Joining"))
                .subscribe(item -> System.out.println("Observer 1: " + item));

        Thread.sleep(3000);

        Disposable subscriber2 = connectableObservable
                .doOnSubscribe((r) -> System.out.println("Observer 2 Joining"))
                .doOnDispose(() -> System.out.println("Subscriber left session"))
                .subscribe(item -> System.out.println("Observer 2: " + item));

        Thread.sleep(3500);

        subscriber2.dispose();

        connectableObservable
                .doOnSubscribe((r) -> System.out.println("Observer 3 Joining"))
                .subscribe(item -> System.out.println("Observer 3: " + item));

        Thread.sleep(8000);

    }

    public static void main(String[] args) throws InterruptedException {
        hotStream();
    }


}
