package com.ibm.vertx.future;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.example.util.Runner;

class FutureVerticle extends AbstractVerticle {

  //empty future
  public Future<Void> getEmptyFuture() {
    //future object creation
    Future<Void> future = Future.future();
    //encapuslate data , no data here
    future.complete();
    return future;
  }

  public Future<String> getSuccessFuture() {
    //future object creation
    Future<String> future = Future.future();
    //encapuslate data , no data here
    future.complete("Hello ? How are you ?");
    return future;
  }

  public Future<String> getFailureFuture() {
    //future object creation
    Future<String> future = Future.future();
    //encapuslate data , no data here
    future.fail("SOmething went wrong");
    return future;
  }

  //some logic
  public Future<String> someLogic() {
    boolean isValid = false;
    //future object creation
    Future<String> future = Future.future();
    if (isValid) {
      //encapuslate data , no data here
      future.complete("This is valid");
    } else {
      future.fail(new RuntimeException("This is invalid"));
    }
    return future;
  }

  //sending future via Static factory
  public Future<String> getFutureViaFactory() {
    boolean isValid = true;
    //future object creation
    if (isValid) {
      //encapuslate data , no data here
      return Future.succeededFuture("this is valid");
    } else {
      return Future.failedFuture(new RuntimeException("This is invalid"));
    }
  }

  //function as a parameter pattern ; callback pattern
  public void isValid(boolean valid, Handler<AsyncResult<String>> asyncResultHandler) {
    if (valid) {
      //encapuslate data into future.
      asyncResultHandler.handle(Future.succeededFuture("I am valid"));
    } else {
      asyncResultHandler.handle(Future.failedFuture("I am not valid valid"));
    }

  }


  @Override
  public void start() throws Exception {
    super.start();
    //registration
    if (getEmptyFuture().succeeded()) {
      System.out.println("Future is success");
    }
    //register with sethandler ; annmous class
    getSuccessFuture().setHandler(new Handler<AsyncResult<String>>() {
      @Override
      public void handle(AsyncResult<String> asyncResult) {
        //grab result
        if (asyncResult.succeeded()) {
          System.out.println(asyncResult.result());
        }
      }
    });
    getSuccessFuture().setHandler(asyncResult -> {
      //grab result
      if (asyncResult.succeeded()) {
        System.out.println(asyncResult.result());
      }
    });
    getSuccessFuture().onComplete(asyncResult -> {
      //grab result
      if (asyncResult.succeeded()) {
        System.out.println(asyncResult.result());
      }
    });
    getSuccessFuture().onSuccess(response -> {
      System.out.println(response);
    });
    getSuccessFuture().onSuccess(System.out::println);
    ///////////////////handle failures
    getFailureFuture().onComplete(asyncResult -> {
      if (asyncResult.failed()) {
        System.out.println(asyncResult.cause());
      }
    });
    getFailureFuture().onFailure(System.out::println);
    //////////////////////////////////////////////////////////////////////////////////////////
    someLogic().onComplete(asyncResult -> {
      if (asyncResult.succeeded()) {
        System.out.println(asyncResult.result());
      } else {
        System.out.println(asyncResult.cause());
      }
    });
    someLogic()
      .onSuccess(System.out::println)
      .onFailure(System.out::println);
    getFutureViaFactory()
      .onSuccess(System.out::println)
      .onFailure(System.out::println);

    //function as  a parameter
    isValid(true, asyncResult -> {
      if (asyncResult.succeeded()) {
        System.out.println(asyncResult.result());
      } else {
        System.out.println(asyncResult.cause());
      }

    });
    isValid(false, asyncResult -> {
      if (asyncResult.succeeded()) {
        System.out.println(asyncResult.result());
      } else {
        System.out.println(asyncResult.cause());
      }

    });

  }
}


public class FututureMainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(FututureMainVerticle.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    vertx.deployVerticle(new FutureVerticle());
  }
}
