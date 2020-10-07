package com.ibm.vertx;

import io.vertx.core.Vertx;

public class HelloWorldVertx {
  public static void main(String[] args) {
    //How to create Vertx instance. using factory apis
    Vertx myvertx = Vertx.vertx();
    System.out.println(myvertx);

  }
}
