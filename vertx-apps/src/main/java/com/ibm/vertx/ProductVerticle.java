package com.ibm.vertx;

import io.vertx.core.AbstractVerticle;

public class ProductVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    System.out.println("Product Verticle Deployement - " + Thread.currentThread().getName());
  }
}
