package com.ibm.vertx.microservices.servicediscovery;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

class MyServer extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    vertx.createHttpServer().requestHandler(request -> {
      if (request.path().equals("/api/hello")) {
        request.response().end("Hello");
      }
    }).listen(3000);
  }
}

class ServiceDiscoveryVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    ServiceDiscoveryOptions discoveryOptions = new ServiceDiscoveryOptions();
    //enable discovery server : apache zoo keeper
    discoveryOptions.setBackendConfiguration(new JsonObject()
      .put("connection", "127.0.0.1:2181")
      .put("ephemeral", true)
      .put("guaranteed", true)
      .put("basePath", "/services/my-backend")
    );

    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, discoveryOptions);
//Record Creation,Record type is Resource Type;storing webclient Resource into registry
    Record httpEndPointRecord = HttpEndpoint.createRecord("myrecord", "localhost", 3000, "/api/hello");
    //publish the Record
    discovery.publish(httpEndPointRecord, ar -> {
      if (ar.succeeded()) {
        System.out.println("Successfully published ..>>>>" + ar.result().toJson());
      } else {
        System.out.println(" Not Published " + ar.cause());
      }
    });

    //how to consume it?
    vertx.createHttpServer().requestHandler(handler -> {
      HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", "myrecord"), sar -> {
        // get web client
        WebClient webClient = sar.result();
        webClient.get("/api/hello").send(res -> {
          System.out.println("Response is ready!");
          System.out.println(res.result().bodyAsString());
          handler.response().end(res.result().bodyAsString());
          //remove /release discovery record
          ServiceDiscovery.releaseServiceObject(discovery, webClient);
        });
      });
    }).listen(9000, h -> {
      System.out.println("Service Discovery Verticle is running At " + h.result().actualPort());
    });

  }
}

public class ServiceDiscoveryMainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(ServiceDiscoveryMainVerticle.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    vertx.deployVerticle(new MyServer());
    vertx.deployVerticle(new ServiceDiscoveryVerticle());
  }
}
