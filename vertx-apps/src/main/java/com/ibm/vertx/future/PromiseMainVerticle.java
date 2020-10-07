package com.ibm.vertx.future;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.example.util.Runner;

class PromiseVerticle extends AbstractVerticle {

  //  public Promise<String> getSuccesPromise() {
//    //future object creation
//    // Future<String> future = Future.future();
//    //Promise Object Creation
//    Promise<String> promise = Promise.promise();
//    //encapuslate data , no data here
//    promise.complete("Hello ? How are you ?");
//    return promise;
//  }
  public Future<String> getSuccesPromise() {
    //future object creation
    // Future<String> future = Future.future();
    //Promise Object Creation
    Promise<String> promise = Promise.promise();
    //encapuslate data , no data here
    promise.complete("Hello ? How are you ?");
    return promise.future();
  }

  @Override
  public void start() throws Exception {
    super.start();
//    getSuccesPromise().future().onComplete(asyncResult -> {
//      if (asyncResult.succeeded()) {
//        System.out.println(asyncResult.result());
//      } else {
//        System.out.println(asyncResult.cause());
//      }
//    });
//  }
    getSuccesPromise().onComplete(asyncResult -> {
      if (asyncResult.succeeded()) {
        System.out.println(asyncResult.result());
      } else {
        System.out.println(asyncResult.cause());
      }
    });
  }
}

public class PromiseMainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(PromiseMainVerticle.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    vertx.deployVerticle(new PromiseVerticle());
  }
}
