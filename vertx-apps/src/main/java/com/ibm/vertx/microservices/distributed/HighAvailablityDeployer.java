package com.ibm.vertx.microservices.distributed;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.example.util.Runner;

public class HighAvailablityDeployer extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(HighAvailablityDeployer.class);
  }
  @Override
  public void start() throws Exception {
    super.start();
    DeploymentOptions options = new DeploymentOptions().setInstances(10);
    vertx.deployVerticle(HighAvailabilityVerticle.class.getName(), options);
  }
}
