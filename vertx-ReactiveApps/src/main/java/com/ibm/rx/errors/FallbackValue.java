package com.ibm.rx.errors;

import io.reactivex.rxjava3.core.Flowable;

/**
 * Use case 2 : how to return static fallback value:
 * .................................................
 * <p>
 * try {
 * for (int i = 1; i < 10; i++) {
 * String v1 = doSomething(i);
 * System.out.println(v1);
 * }
 * <p>
 * } catch (Throwable a) {
 * return "RECOVERED";
 * }
 */
public class FallbackValue {
    private static String doSomething(int i) {
        // System.out.println(i);
        if (i == 5) {
            throw new RuntimeException("sorry");
        }
        return Integer.toString(i);
    }

    private static String doSomethingboom(int i) {
        // System.out.println(i);
        if (i == 5) {
            throw new RuntimeException("boom10");
        }
        return Integer.toString(i);
    }

    public static void main(String[] args) {
//        try {
//            for (int i = 1; i < 10; i++) {
//                String v1 = doSomething(i);
//                System.out.println(v1);
//            }
//
//        } catch (Throwable a) {
//            return "RECOVERED";
//        }
        Flowable.range(1, 10)
                .map(v -> doSomething(v))
                .onErrorReturn(err -> "RECOVERED")
                .subscribe(v -> System.out.println(v), err -> System.out.println(err));
        Flowable.range(1, 10)
                .map(v -> doSomething(v))
                .onErrorReturn(err -> getFromCache())
                .subscribe(v -> System.out.println(v), err -> System.out.println(err));

        Flowable.just("2A")
                .map(v -> Integer.parseInt(v, 10))
                .onErrorReturnItem(0)
                .subscribe(
                        System.out::println,
                        error -> System.err.println("onError should not be printed!"));
    }

    private static String getFromCache() {
        return "Recovered from Fallback method :Result from cache";
    }
}
