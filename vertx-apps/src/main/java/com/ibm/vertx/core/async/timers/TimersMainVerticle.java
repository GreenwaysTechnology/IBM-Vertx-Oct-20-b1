package com.ibm.vertx.core.async.timers;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;

import java.util.Date;

class TimersVerticle extends AbstractVerticle {

  public void delay(long duration) {
    //timer api;
    vertx.setTimer(duration, timerHandler -> {
      //todo any task after timemout
      System.out.println("hello, i am delayed person");
    });
  }

  public Future<JsonObject> delayWithData(long duration) {
    Promise<JsonObject> promise = Promise.promise();

    vertx.setTimer(duration, timerHandler -> {
      //todo any task after timemout
      promise.complete(new JsonObject().put("id", 1).put("name", "subramaian"));
    });
    return promise.future();
  }

  public void heartBeat(Handler<AsyncResult<Date>> aHandler) {
    long timerid = vertx.setPeriodic(1000, phandler -> {
      //System.out.println(new Date());
      aHandler.handle(Future.succeededFuture(new Date()));
    });
    vertx.setTimer(10000, timerHandler -> {
      System.out.println("Stopping timer");
      vertx.cancelTimer(timerid);
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    System.out.println("start");
    delay(1000);
    delayWithData(5000).onComplete(jsonObjectAsyncResult -> {
      if (jsonObjectAsyncResult.succeeded()) {
        System.out.println(jsonObjectAsyncResult.result().encodePrettily());
      }
    });
    heartBeat(dateAsyncResult -> System.out.println(dateAsyncResult.result()));
    System.out.println("end");
  }
}

public class TimersMainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(TimersMainVerticle.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    vertx.deployVerticle(new TimersVerticle());
  }
}
