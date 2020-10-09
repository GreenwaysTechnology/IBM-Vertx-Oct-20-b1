package com.ibm.vertx.rx;

import io.vertx.example.util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;

class ReactiveHttpServer extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    super.start();
    HttpServer httpServer = vertx.createHttpServer();
    httpServer.requestHandler(request -> {
      request.response().end("Hello");
    });
    httpServer.rxListen(3000)
      .subscribe(server -> System.out.println(server.actualPort()), System.out::println);


  }
}


public class ReactiveVerticleMain extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(ReactiveVerticleMain.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    vertx.rxDeployVerticle(new ReactiveHttpServer()).subscribe(
      success -> System.out.println(success),
      err -> System.out.println(err)
    );
  }
}
