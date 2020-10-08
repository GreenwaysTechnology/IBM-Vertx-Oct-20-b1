package com.ibm.vertx.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;


public class ResourceBasedRouting extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(ResourceBasedRouting.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    HttpServer server = vertx.createHttpServer();

    Router customerRouter = Router.router(vertx);
    Router productRouter = Router.router(vertx);

    //custoemrs
    customerRouter.get("/list").handler(routingContext -> {
      JsonArray users = new JsonArray();
      users.add(new JsonObject().put("id", 1).put("name", "subramanian"))
        .add(new JsonObject().put("id", 2).put("name", "Smith"));
      routingContext
        .response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(users.encodePrettily());
    });

    //prouust
    productRouter.get("/list").handler(routingContext -> {
      JsonArray users = new JsonArray();
      users.add(new JsonObject().put("id", 1).put("name", "phone"))
        .add(new JsonObject().put("id", 2).put("name", "tv"));
      routingContext
        .response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(users.encodePrettily());
    });
    //Application Router /route Router
    Router mainRouter = Router.router(vertx);
    mainRouter.route().handler(BodyHandler.create());

    //binding subrouters with main router
    mainRouter.mountSubRouter("/api/customers", customerRouter);
    mainRouter.mountSubRouter("/api/products", productRouter);

    server.requestHandler(mainRouter);

    server.listen(3000, httpServerAsyncResult -> {
      if (httpServerAsyncResult.succeeded()) {
        System.out.println("Server is Ready at " + httpServerAsyncResult.result().actualPort());
      } else {
        System.out.println("Server failed " + httpServerAsyncResult.cause());
      }
    });
  }
}
