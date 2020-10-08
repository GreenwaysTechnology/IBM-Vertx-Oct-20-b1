package com.ibm.vertx.core.dataformat;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;

public class JsonVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(JsonVerticle.class);
  }

  public void createSimpleJSON() {

    JsonObject profile = new JsonObject();
    //add data; types
    profile.put("id", 1);
    profile.put("name", "Subramanian");
    profile.put("status", true);
    System.out.println(profile.getInteger("id"));
    System.out.println(profile.getString("name"));
    System.out.println(profile.getBoolean("status"));
    System.out.println(profile.encodePrettily());
  }

  public void createfluentJSON() {
    JsonObject profile = new JsonObject()
      .put("id", 1)
      .put("name", "ram")
      .put("status", true);
    System.out.println(profile.encodePrettily());
  }

  public void createNestedJSON() {
    JsonObject profile = new JsonObject()
      .put("id", 1)
      .put("name", "Subramanian")
      .put("status", true)
      .put("address", new JsonObject().put("street", "10th street").put("city", "coimbatore"));
    System.out.println(profile.encodePrettily());

  }

  public void createJSONArray() {
    JsonObject profile = new JsonObject()
      .put("id", 1)
      .put("name", "Subramanian")
      .put("status", true)
      .put("address", new JsonObject()
        .put("city", "Coimbatore")
        .put("state", "Tamil Nadu"));

    JsonArray jsonArray = new JsonArray()
      .add(profile)
      .add(new JsonObject()
        .put("id", 2)
        .put("name", "Ram")
        .put("status", true)
        .put("address", new JsonObject()
          .put("city", "Chennai")
          .put("state", "Tamil Nadu")));
    System.out.println(jsonArray.encodePrettily());

  }

  public void mergeJson(JsonObject jsonObject) {
    JsonObject config = new JsonObject()
      .put("http.port", 8080)
      .mergeIn(jsonObject);
    System.out.println(config.encodePrettily());
  }

  public Future<JsonObject> getJsonFuture() {
    Promise<JsonObject> jsonPromise = Promise.promise();
    JsonObject profile = new JsonObject()
      .put("id", 1)
      .put("name", "Subramanian")
      .put("status", true)
      .put("address", new JsonObject()
        .put("city", "Coimbatore")
        .put("state", "Tamil Nadu"));
    jsonPromise.complete(profile);

    return jsonPromise.future();
  }

  @Override
  public void start() throws Exception {
    super.start();
    createSimpleJSON();
    createfluentJSON();
    createNestedJSON();
    createJSONArray();
    mergeJson(new JsonObject().put("host", "localhost").put("ssl", true).put("http.port", 3000));
    getJsonFuture().onComplete(jsonObjectAsyncResult -> {
      if(jsonObjectAsyncResult.succeeded()){
        System.out.println(jsonObjectAsyncResult.result().encodePrettily());
      }
    });
  }
}
