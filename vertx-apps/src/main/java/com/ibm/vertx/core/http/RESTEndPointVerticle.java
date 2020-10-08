package com.ibm.vertx.core.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;

public class RESTEndPointVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(RESTEndPointVerticle.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    HttpServer server = vertx.createHttpServer();
    //handle client request async
    server.requestHandler(request -> {
      //send a reponse
      if (request.method() == HttpMethod.GET && request.path().equals("/api/message")) {
        JsonObject message = new JsonObject().put("message", "Hello");
        request
          .response()
          .putHeader("content-type", "application/json")
          .setStatusCode(200)
          .end(message.encodePrettily());
      } else if (request.method() == HttpMethod.GET && request.path().equals("/api/users")) {
        JsonArray users = new JsonArray();
        users.add(new JsonObject().put("id", 1).put("name", "subramanian"))
          .add(new JsonObject().put("id", 2).put("name", "Smith"));
        request
          .response()
          .putHeader("content-type", "application/json")
          .setStatusCode(200)
          .end(users.encodePrettily());
      }

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
