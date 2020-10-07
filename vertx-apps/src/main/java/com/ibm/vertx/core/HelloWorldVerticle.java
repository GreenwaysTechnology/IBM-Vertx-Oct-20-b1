package com.ibm.vertx.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

//this verticle can be deployed on vertx instance
public class HelloWorldVerticle extends AbstractVerticle {

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    System.out.println("init");
  }

  @Override
  public void start() throws Exception {
    super.start();
    System.out.println("start");
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    System.out.println("stop");
  }
}
