package com.ibm.vertx.rx;
import io.reactivex.Single;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.client.HttpRequest;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

class HelloConsumerMicroservice extends AbstractVerticle {
  private WebClient client;

  @Override
  public void start() throws Exception {
    super.start();
    client = WebClient.create(vertx);

    Router router = Router.router(vertx);
    router.get("/").handler(this::invokeMyFirstMicroservice);

    vertx.createHttpServer()
      .requestHandler(router)
      .rxListen(8081).subscribe(
      httpServer -> System.out.println("Client is Running @ " + httpServer.actualPort()),
      error -> System.out.println(error)
    );
  }

  private void invokeMyFirstMicroservice(RoutingContext rc) {

    HttpRequest<JsonObject> request1 = client
      .get(8082, "localhost", "/Srisha")
      .as(BodyCodec.jsonObject());

    HttpRequest<JsonObject> request2 = client
      .get(8082, "localhost", "/Subramanian")
      .as(BodyCodec.jsonObject());

    Single<HttpResponse<JsonObject>> s1 = request1.rxSend();
    Single<HttpResponse<JsonObject>> s2 = request2.rxSend();

    Single.zip(s1, s2, (apiOne, apiTwo) -> {
      // We have the result of both request in Luke and Leia
      return new JsonObject()
        .put("firstName", apiOne.body().getString("message"))
        .put("lastName", apiTwo.body().getString("message"));
    }).subscribe(
      x -> {
        rc.response().end(x.encode());
      },
      t -> {
        rc.response().end(new JsonObject().encodePrettily());
      });
  }

}

class HelloMicroservice extends AbstractVerticle {
  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.get("/").handler(this::hello);
    router.get("/:name").handler(this::hello);
    vertx.createHttpServer()
      .requestHandler(router)
      .rxListen(8082).subscribe(server -> {
      System.out.println("Provider is listening in " + server.actualPort());
    });
  }

  private void hello(RoutingContext rc) {
    String message = "hello";
    if (rc.pathParam("name") != null) {
      message += " " + rc.pathParam("name");
    }
    JsonObject json = new JsonObject().put("message", message);
    rc.response()
      .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
      .end(json.encode());
  }

}

public class WebClientReactiveMicroService extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(WebClientReactiveMicroService.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    vertx.rxDeployVerticle(new HelloMicroservice())
      .subscribe(r -> System.out.println("HelloMicroservice deployed"));
    vertx.rxDeployVerticle(new HelloConsumerMicroservice())
      .subscribe(r -> System.out.println("HelloConsumerMicroservice deployed"));
  }
}
