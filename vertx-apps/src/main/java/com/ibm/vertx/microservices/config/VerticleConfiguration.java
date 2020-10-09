package com.ibm.vertx.microservices.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;

class ExternalConfigVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    System.out.println(config().encodePrettily());

  }
}

class ApplicationConfigVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    super.start();
    //read config
    vertx
      .createHttpServer()
      .requestHandler(request -> {
        System.out.println(config().getString("name"));
        request.response().end(config().getString("message"));
      })
      .listen(config().getInteger("http.port", 8080), ar -> {
        System.out.println("Server is ready at " + ar.result().actualPort());
      });
  }
}


public class VerticleConfiguration extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(VerticleConfiguration.class);
  }

  public Future<JsonObject> getConfig() {
    Promise<JsonObject> promise = Promise.promise();
    //store options
    ConfigStoreOptions options = new ConfigStoreOptions();
    options.setType("file");
    options.setFormat("json");
    options.setConfig(new JsonObject().put("path", "conf/config.json"));
    //config reteriver
    ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(options));
    //read configuration
    retriever.getConfig(config -> {
      if (config.succeeded()) {
        promise.complete(config.result());
      } else {
        promise.fail("Config Error : " + config.cause());
      }
    });
    return promise.future();

  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    DeploymentOptions options = new DeploymentOptions();

    JsonObject applicationConfig = new JsonObject()
      .put("name", "Subramanian")
      .put("city", "Coimbatore")
      .put("state", "TN");

    JsonObject config = new JsonObject()
      .put("http.port", 3000)
      .put("message", "Hello")
      .mergeIn(applicationConfig);

    options.setConfig(config);
    //inject config data to verticlie
    vertx.deployVerticle(new ApplicationConfigVerticle(), options);

    getConfig().onSuccess(myconfig -> {
      //System.out.println(myconfig.encodePrettily());
//      JsonObject appconfig = new JsonObject()
//        .put("name", "Subramnaian")
//        .put("message", "Hello!!")
//        .mergeIn(myconfig);
      DeploymentOptions myoptions = new DeploymentOptions().setConfig(myconfig);
      vertx.deployVerticle(new ExternalConfigVerticle(), myoptions);
    });


  }
}
