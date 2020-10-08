package com.ibm.vertx.microservices.communication.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

public class HttpCommunicationDeployer extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(HttpCommunicationDeployer.class);
  }

  @Override
  public void start() throws Exception {
    vertx.deployVerticle(new ProviderServiceVerticle());
    vertx.deployVerticle(new ConsumerServiceVerticle());
  }
}
