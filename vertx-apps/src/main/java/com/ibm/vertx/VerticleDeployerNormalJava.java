package com.ibm.vertx;

import io.vertx.core.Vertx;

//Deployment via coding ; with normal java class
public class VerticleDeployerNormalJava {
  public static void main(String[] args) {
    //ways
    //create vertx instance
    Vertx myvertx = Vertx.vertx();
    //way 1
    myvertx.deployVerticle(new ProductVerticle());
    //way 2 ; passing class name
    myvertx.deployVerticle(ProductVerticle.class.getName());
    //way 3: passing class with inside string notation
    myvertx.deployVerticle("com.ibm.vertx.ProductVerticle");

  }
}
