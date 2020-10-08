package com.ibm.vertx.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class RouterVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(RouterVerticle.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    HttpServer server = vertx.createHttpServer();
    //Create Router; collection of routes, each each represents api.
    Router router = Router.router(vertx);
    //entry router//
    //this will be called for any request irrespective of method and url.
    router.route().handler(routingContext -> {
      System.out.println(routingContext.request().method() + " " + routingContext.request().path());
      //move the control to next route
      routingContext.next();
    });

    //other way of defining path
    Route myroute = router.route("/metrics");
    myroute.handler(routingContext -> {
      System.out.println("starting");
      routingContext.response().setChunked(true);
      routingContext.response().write("Staring....\n");
      routingContext.next();
    });
    myroute.handler(routingContext -> {
      System.out.println("going on");
      routingContext.response().write("Going on....\n");
      routingContext.next();
    });
    myroute.handler(routingContext -> {
      System.out.println("end");
      routingContext.response().write("End....\n");
      routingContext.next();
    });
    myroute.handler(routingContext -> {
      routingContext.response().write("Metrics done....\n");
      // Now end the response
      routingContext.response().end();
    });

    //fluent of above code
    router.route("/logs")
      .handler(routingContext -> {
        System.out.println("starting");
        routingContext.response().setChunked(true);
        routingContext.response().write("Staring....\n");
        routingContext.next();
      })
      .handler(routingContext -> {
        System.out.println("going on");
        routingContext.response().write("Going on....\n");
        routingContext.next();
      })
      .handler(routingContext -> {
        System.out.println("end");
        routingContext.response().write("End....\n");
        routingContext.next();
      })
      .handler(routingContext -> {
        routingContext.response().write("logs done....\n");
        // Now end the response
        routingContext.response().end();
      });


    //define routes
    //handler takes RoutingContext object, through which you can get Request and Response
    router.get("/api/message").handler(routingContext -> {
      JsonObject message = new JsonObject().put("message", "Hello");
      routingContext
        .response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(message.encodePrettily());
    });
    //dynmaic route
    router.get("/api/message/:name").handler(routingContext -> {
      String name = routingContext.request().getParam("name");
      JsonObject message = new JsonObject().put("message", "Hello").put("name", name);
      routingContext
        .response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(message.encodePrettily());
    });
    router.get("/api/users").handler(routingContext -> {
      JsonArray users = new JsonArray();
      users.add(new JsonObject().put("id", 1).put("name", "subramanian"))
        .add(new JsonObject().put("id", 2).put("name", "Smith"));
      routingContext
        .response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(users.encodePrettily());
    });

    server.requestHandler(router);
    server.listen(3000, httpServerAsyncResult -> {
      if (httpServerAsyncResult.succeeded()) {
        System.out.println("Server is Ready at " + httpServerAsyncResult.result().actualPort());
      } else {
        System.out.println("Server failed " + httpServerAsyncResult.cause());
      }
    });
  }
}
