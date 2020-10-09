package com.ibm.vertx.microservices.blocking;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;

class ExcuteBlockingApiVerticle extends AbstractVerticle {

  private void sayHello(Promise<String> promise) {
    System.out.println("Say Hello : " + Thread.currentThread().getName());
    try {
      Thread.sleep(4000);
      System.out.println("Wake Up, to send data to Non blocking Service");
      //this result will be accessed inside non blocking code
      promise.complete("Hey this is blocking Result");

    } catch (InterruptedException es) {
      promise.fail("Something went wrong in blocking service");
    }
  }

  private void resultHandler(AsyncResult<String> ar) {
    System.out.println("Result Handler" + Thread.currentThread().getName());
    if (ar.succeeded()) {
      System.out.println("Blocking api Result goes Ready Here");
      System.out.println(ar.result());
    } else {
      System.out.println(ar.cause().getMessage());
    }
  }

  public void exchange() {
    //run blocking ,blocking code result i need inside nonblocking
    vertx.executeBlocking(this::sayHello, this::resultHandler);
  }
  public void httpblocking() {
    vertx.createHttpServer().requestHandler(request -> {
      vertx.<String>executeBlocking(promise -> {
        // Do the blocking operation in here
        // Imagine this was a call to a blocking API to get the result
        try {
          Thread.sleep(5000);
        } catch (Exception ignore) {
        }
        String result = "hello , i am blocked";

        promise.complete(result);

      }, res -> {
        if (res.succeeded()) {
          request.response().putHeader("content-type", "text/plain").end(res.result());
        } else {
          res.cause().printStackTrace();
        }
      });

    }).listen(8080);
  }
  public void httpRouterBlocking() {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.get("/blocking").blockingHandler(routingContext -> {
      try {
        //blocking code
        Thread.sleep(5000);
      } catch (Exception e) {

      }
      routingContext.response().end("Blocking result");
    });

    server.requestHandler(router);


    server.listen(8081, handler -> {
      if (handler.succeeded()) {
        System.out.println(handler.result().actualPort());
      }
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    System.out.println(this.getClass().getName() + "is running on" + Thread.currentThread().getName());
    //exchange();
    //httpblocking();
    httpRouterBlocking();
  }
}

class MyBlockingVerticle extends AbstractVerticle {
  private void blockeMe() {
    try {
      Thread.sleep(10000);
      System.out.println("i am ready after blocking");
    } catch (InterruptedException es) {

    }
  }

  @Override
  public void start() throws Exception {
    super.start();
    System.out.println(Thread.currentThread().getName());
    blockeMe();
  }
}


public class BlockingVerticleMain extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(BlockingVerticleMain.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    //worker verticles
    DeploymentOptions options = new DeploymentOptions().setWorker(true);
    vertx.deployVerticle(new MyBlockingVerticle(), options);
    vertx.deployVerticle(new ExcuteBlockingApiVerticle());
  }
}
