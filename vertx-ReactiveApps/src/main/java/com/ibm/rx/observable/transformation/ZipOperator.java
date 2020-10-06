package com.ibm.rx.observable.transformation;

import io.reactivex.rxjava3.core.Observable;

public class ZipOperator {
    public static void main(String[] args) {
        Observable<Integer> intStream = Observable.just(1, 2, 3, 4);
        Observable<String> stringStream = Observable.just("a", "b", "c", "d", "e");

        Observable.zip(intStream, stringStream, (i, str) -> {
            System.out.println("I " + i + " String " + str);
            //emp.name + emp.department
            return i + str; // new Aggeatro(emp.name,emp.department)
        }).subscribe(System.out::println, System.out::println, () -> System.out.println("Done"));
    }
}
