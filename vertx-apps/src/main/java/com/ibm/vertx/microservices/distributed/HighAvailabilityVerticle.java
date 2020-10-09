package com.ibm.vertx.microservices.distributed;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;

public class HighAvailabilityVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    HttpServer server = vertx.createHttpServer();
    long instanceName = this.getClass().hashCode();

    server.requestHandler(req -> {
      long id = Thread.currentThread().getId();
      req.response().end("<h1> I am coming from " + id);
    });

    server.listen(8888, "localhost", handler -> {
      if (handler.succeeded()) {
        System.out.println("Server is Ready! " + Thread.currentThread().getId());
      } else {
        System.out.println("Server failed to Start");
      }
    });
  }
}
