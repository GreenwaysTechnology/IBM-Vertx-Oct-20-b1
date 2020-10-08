package com.ibm.vertx.microservices.communication.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class ConsumerServiceVerticle extends AbstractVerticle {

  WebClient webClient;

  public void home(RoutingContext routingContext) {
    //create WebClient object
    webClient = WebClient.create(vertx);
    Integer port = 3000;
    String host = "localhost";
    String url = "/";
    webClient.get(port, host, url).send(httpar -> {
        if(httpar.succeeded()){
          //Obtain Response
          HttpResponse<Buffer> response = httpar.result();
          //buffer is converted into string
          System.out.println(response.bodyAsString());
          routingContext.response().end(response.bodyAsString());
        }
    });

  }
  public void sendMessage(RoutingContext context){

    vertx.eventBus().publish("news.message","Hello");

    context.response().end("success");
  }
  public void hello(RoutingContext routingContext) {
    //create WebClient instance
    webClient = WebClient.create(vertx);
    Integer port = 3000;
    String host = "localhost";
    String url = "/api/hello";
    webClient.get(port, host, url).send(ar -> {
      if (ar.succeeded()) {
        //Obtain Response
        HttpResponse<Buffer> response = ar.result();
        System.out.println(response.bodyAsString());
        routingContext.response().end(response.bodyAsString());
      } else {
        System.out.println(ar.cause());
      }
    });


  }



  @Override
  public void start() throws Exception {
    super.start();
    HttpServer httpServer = vertx.createHttpServer();
    //Routers : method and url mapping using vertx-web module
    //router is entry, which has collection of routes
    Router router = Router.router(vertx);
    //routes
    //router.HTTPmethod("url)
    router.get("/").handler(this::home);
    router.get("/api/greeter/hello").handler(this::hello);
    router.get("/api/notification").handler(this::sendMessage);

    //request handling
    httpServer.requestHandler(router);

    httpServer.listen(3001, handler -> {
      if (handler.succeeded()) {
        System.out.println("Consumer Server is up " + handler.result().actualPort());
      } else {
        System.out.println("Consumer Server is down!!!" + handler.cause());
      }
    });
  }
}
