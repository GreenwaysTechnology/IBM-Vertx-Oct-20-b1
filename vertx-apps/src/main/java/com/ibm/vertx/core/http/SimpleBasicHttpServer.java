package com.ibm.vertx.core.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.example.util.Runner;


public class SimpleBasicHttpServer extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(SimpleBasicHttpServer.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    HttpServer server = vertx.createHttpServer();

    //handle client request async
    server.requestHandler(request -> {
      //send a reponse
      HttpServerResponse response = request.response();
      response.end("Hello Http Server");
    });


    //start server
    server.listen(3000, httpServerAsyncResult -> {
      if (httpServerAsyncResult.succeeded()) {
        System.out.println("Server is Ready at " + httpServerAsyncResult.result().actualPort());
      } else {
        System.out.println("Server failed " + httpServerAsyncResult.cause());
      }
    });


  }
}
