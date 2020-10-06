package com.ibm.rx.backpressure;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.scene.layout.FlowPane;

public class ReactiveStreamSpec {
    static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Flowable.range(1, 999_999_999)
                .map(MyItem::new) //upstream is fast enough to send data
                .observeOn(Schedulers.io())
                .subscribe(myItem -> {
                    sleep(500); //down stream/subscriber are slow
                    System.out.println("Received MyItem " +
                            myItem.id);
                });
        sleep(Long.MAX_VALUE);
    }

    static final class MyItem {
        final int id;

        MyItem(int id) {
            this.id = id;
            System.out.println("Consuming myitem " + id);
        }
    }

}
