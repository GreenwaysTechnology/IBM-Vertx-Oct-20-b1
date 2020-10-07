package com.ibm.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

public class VerticleDeployerViaVerticleItself extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(VerticleDeployerViaVerticleItself.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    System.out.println(Thread.currentThread().getName());
    //way 1
    vertx.deployVerticle(new ProductVerticle());
    //way 2 ; passing class name
    vertx.deployVerticle(ProductVerticle.class.getName());
    //way 3: passing class with inside string notation
    vertx.deployVerticle("com.ibm.vertx.ProductVerticle");
  }
}
